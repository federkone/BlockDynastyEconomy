package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.PayEvent;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.TransferResult;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class PayUseCase {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log economyLogger;
    private final EventManager eventManager;
    private final SearchAccountUseCase searchAccountUseCase;

    public PayUseCase(SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase, IRepository dataStore,
                      Courier updateForwarder, Log economyLogger, EventManager eventManager) {
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.searchAccountUseCase = searchAccountUseCase;
        this.eventManager = eventManager;
    }

    public Result<Void> execute(UUID userFrom, UUID userTo, String currencyName, BigDecimal amount) {
        Result<Account> accountFromResult =  this.searchAccountUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            //messageservice.sendMessage(userFrom,result.getErrorMessage(), result.getErrorCode());
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult =  this.searchAccountUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            //messageservice.sendMessage(userTo,result.getErrorMessage(), result.getErrorCode());
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyResult =  this.searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            //messageservice.sendMessage(currencyName,result.getErrorMessage(), result.getErrorCode());
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performPay(accountFromResult.getValue(), accountToResult.getValue(), currencyResult.getValue(), amount);
    }

    public Result<Void> execute (String userFrom, String userTo, String currencyName, BigDecimal amount) {
        Result<Account> accountFromResult =  this.searchAccountUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            //messageservice.sendMessage(userFrom,result.getErrorMessage(), result.getErrorCode());
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult =  this.searchAccountUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            //messageservice.sendMessage(userTo,result.getErrorMessage(), result.getErrorCode());
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyResult =  this.searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            //messageservice.sendMessage(currencyName,result.getErrorMessage(), result.getErrorCode());
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performPay(accountFromResult.getValue(), accountToResult.getValue(), currencyResult.getValue(), amount);
    }


    private Result<Void> performPay (Account accountFrom, Account accountTo, Currency currency, BigDecimal amount) {
        if (!accountTo.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(!currency.isPayable()){
            return Result.failure("Currency is not payable", ErrorCode.CURRENCY_NOT_PAYABLE);
        }

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isValidAmount(amount)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<TransferResult> result = dataStore.transfer(accountFrom.getUuid().toString(),accountTo.getUuid().toString(),currency, amount);
        if(!result.isSuccess()){
            this.economyLogger.log("[Pay failed] Account: " + accountFrom.getNickname() + " pay " + currency.format(amount) + " to " + accountTo.getNickname() + " - Error: " + result.getErrorMessage() + " - Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        //actualizar cache con las cuentas contenidas en result
        this.searchAccountUseCase.syncCacheWithAccount(result.getValue().getTo());
        this.searchAccountUseCase.syncCacheWithAccount(result.getValue().getFrom());

        //.................
        this.updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
        this.updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
        this.economyLogger.log("[Pay] Account: " + accountFrom.getNickname() + " pay " + currency.format(amount) + " to " + accountTo.getNickname());
        this.eventManager.emit(new PayEvent(accountFrom.getPlayer(), accountTo.getPlayer(), currency, amount));
        //................

        return Result.success(null);
    }
}
