package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.TradeEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.result.TransferResult;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class TradeCurrenciesUseCase {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log economyLogger;
    private final SearchAccountUseCase searchAccountUseCase;
    private final IAccountService accountService;
    private final EventManager eventManager;

    public TradeCurrenciesUseCase(SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase,
                                  IAccountService accountService,IRepository dataStore,
                                  Courier updateForwarder, Log economyLogger, EventManager eventManager) {

        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.accountService = accountService;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.searchAccountUseCase = searchAccountUseCase;
        this.economyLogger = economyLogger;
        this.eventManager = eventManager;
    }

    public Result<Void> execute(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Result<Account> accountFromResult =  this.searchAccountUseCase.getAccount(userFrom);
        Result<Account> accountToResult =  this.searchAccountUseCase.getAccount(userTo);
        return execute(accountFromResult, accountToResult, currencyFromS, currencyToS, amountFrom, amountTo);
    }

    public Result<Void> execute(String userFrom, String userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Result<Account> accountFromResult =  this.searchAccountUseCase.getAccount(userFrom);
        Result<Account> accountToResult =  this.searchAccountUseCase.getAccount(userTo);
        return execute(accountFromResult, accountToResult, currencyFromS, currencyToS, amountFrom, amountTo);
    }

    private Result<Void> execute(Result<Account> accountFromResult,Result<Account> accountToResult,  String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        if (!accountFromResult.isSuccess()) {
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        if (!accountToResult.isSuccess()) {
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyFromResult =  this.searchCurrencyUseCase.getCurrency(currencyFromS);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult =  this.searchCurrencyUseCase.getCurrency(currencyToS);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performTrade(accountFromResult.getValue(), accountToResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    private Result<Void> performTrade (Account accountFrom, Account accountTo, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        if(!currencyFrom.isTransferable() || !currencyTo.isTransferable()){
            return Result.failure("Currency not transferable", ErrorCode.CURRENCY_NOT_PAYABLE);
        }

        if (accountFrom.getUuid().equals(accountTo.getUuid()) || accountFrom.getNickname().equals(accountTo.getNickname())) {
            return Result.failure("You can't trade with yourself", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if (accountFrom.isBlocked()){
            return Result.failure("Sender account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        if (accountTo.isBlocked()){
            return Result.failure("Target account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        if(!accountTo.canReceiveCurrency()){
            return Result.failure("Account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(amountFrom.compareTo(BigDecimal.ZERO) <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(amountTo.compareTo(BigDecimal.ZERO) <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currencyFrom.isValidAmount(amountFrom)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if(!currencyTo.isValidAmount(amountTo)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<TransferResult> result = this.dataStore.trade( accountFrom.getUuid().toString(), accountTo.getUuid().toString(), currencyFrom, currencyTo, amountFrom, amountTo);
        if(!result.isSuccess()){
            this.economyLogger.log("[TRADE failed] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo) + " - Error: " + result.getErrorMessage() + " - Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.accountService.syncOnlineAccount(result.getValue().getTo());
        this.accountService.syncOnlineAccount(result.getValue().getFrom());

        //this.updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
        //this.updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
        this.economyLogger.log("[TRADE] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo));
        this.eventManager.emit(new TradeEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currencyFrom, currencyTo, amountFrom, amountTo));
        this.updateForwarder.sendUpdateMessage("event", new TradeEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currencyFrom, currencyTo, amountFrom, amountTo).toJson(), accountTo.getUuid().toString());
        this.updateForwarder.sendUpdateMessage("event", new TradeEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currencyFrom, currencyTo, amountFrom, amountTo).toJson(), accountFrom.getUuid().toString());

        return Result.success();
    }
}
