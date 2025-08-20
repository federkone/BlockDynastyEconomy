package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.TransferEvent;
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

public class TransferFundsUseCase {
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log economyLogger;
    private final EventManager eventManager;
    private final SearchAccountUseCase searchAccountUseCase;
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public TransferFundsUseCase(SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase, IRepository dataStore,
                                Courier updateForwarder, Log economyLogger, EventManager eventManager) {
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.searchAccountUseCase = searchAccountUseCase;
        this.eventManager = eventManager;
    }

    public Result<Void> execute(UUID userFrom, UUID userTo, String currency, BigDecimal amount) {
        Result<Account> accountFromResult = this.searchAccountUseCase.getAccount(userFrom);
        Result<Account> accountToResult = this.searchAccountUseCase.getAccount(userTo);
        return execute(accountFromResult, accountToResult, currency, amount);
    }

    public Result<Void> execute (String userFrom, String userTo, String currency, BigDecimal amount) {
        Result<Account> accountFromResult = this.searchAccountUseCase.getAccount(userFrom);
        Result<Account> accountToResult = this.searchAccountUseCase.getAccount(userTo);
        return execute(accountFromResult, accountToResult, currency, amount);
    }
    //new
    public Result<Void> execute(Result<Account> accountFromResult, Result<Account> accountToResult, String currency, BigDecimal amount) {
        if (!accountFromResult.isSuccess()) {
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }
        if (!accountToResult.isSuccess()) {
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }
        Result<Currency> currencyResult = this.searchCurrencyUseCase.getCurrency(currency);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performTransaction(accountFromResult.getValue(), accountToResult.getValue(), currencyResult.getValue(), amount);
    }

    private Result<Void> performTransaction(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount) {
        if(!currency.isTransferable()){
            return Result.failure("Currency is not transferable", ErrorCode.CURRENCY_NOT_PAYABLE);
        }
        if (accountFrom.getUuid().equals(accountTo.getUuid()) || accountFrom.getNickname().equals(accountTo.getNickname())) {
            return Result.failure("You can't transfer to yourself", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
        if (!accountTo.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
        if (accountFrom.isBlocked()) {
            return Result.failure("Sender account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }
        if (accountTo.isBlocked()) {
            return Result.failure("Target account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if (!currency.isValidAmount(amount)) {
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<TransferResult> result = this.dataStore.transfer(accountFrom.getUuid().toString(), accountTo.getUuid().toString(), currency, amount);
        if (!result.isSuccess()) {
            logFailure(accountFrom, accountTo, currency, amount, result);
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        updateCacheAndEmitEvents(accountFrom, accountTo, currency, amount, result);
        return Result.success(null);
    }

    private void updateCacheAndEmitEvents(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount, Result<TransferResult> result) {
        this.searchAccountUseCase.syncCacheWithAccount(result.getValue().getTo());
        this.searchAccountUseCase.syncCacheWithAccount(result.getValue().getFrom());
        this.updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
        this.updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
        logSuccess(accountFrom, accountTo, currency, amount);
        emitEvent(accountFrom, accountTo, currency, amount);
    }

    protected void logSuccess(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount){
        this.economyLogger.log("[TRANSFER] Account: " + accountFrom.getNickname() + " send " + currency.format(amount) + " to " + accountTo.getNickname());
    };

    protected void logFailure(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount, Result<TransferResult> result){
        this.economyLogger.log("[TRANSFER Failed] Account: " + accountFrom.getNickname() + " pay " + currency.format(amount) + " to " + accountTo.getNickname() + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
    };

    protected void emitEvent(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount){
        this.eventManager.emit(new TransferEvent(accountFrom.getPlayer(),accountTo.getPlayer(), currency, amount));
    };
}