package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByNameUseCase;
import BlockDynasty.Economy.aplication.useCase.account.getAccountUseCase.GetAccountByUUIDUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class TransactionUseCase {
    protected final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final GetAccountByUUIDUseCase getAccountByUUIDUseCase;
    protected final GetAccountByNameUseCase getAccountByNameUseCase;
    protected final IRepository dataStore;
    protected final Courier updateForwarder;
    protected final EventManager eventManager;
    protected final Log logger;
    protected final IAccountService accountService;

    protected TransactionUseCase(IAccountService accountService, ICurrencyService currencyService,
                                 IRepository dataStore, Courier updateForwarder, Log logger, EventManager eventManager) {
        this.searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, dataStore);
        this.getAccountByNameUseCase = new GetAccountByNameUseCase(accountService);
        this.getAccountByUUIDUseCase = new GetAccountByUUIDUseCase(accountService);
        this.accountService = accountService;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = logger;
        this.eventManager = eventManager;
    }

    public Result<Void> execute(UUID targetUUID, BigDecimal amount) {
        return this.execute(targetUUID, null, amount);
    }

    public Result<Void> execute(String targetName, BigDecimal amount) {
        return this.execute(targetName, null, amount);
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        return this.execute( this.getAccountByUUIDUseCase.execute(targetUUID), currencyName, amount, Context.SYSTEM);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        return this.execute(this.getAccountByNameUseCase.execute(targetName), currencyName, amount,Context.SYSTEM);
    }

    public Result<Void> execute(UUID userFrom, UUID userTo, String currency, BigDecimal amount) {
        return execute(getAccountByUUIDUseCase.execute(userFrom), getAccountByUUIDUseCase.execute(userTo), currency, amount);
    }

    public Result<Void> execute (String userFrom, String userTo, String currency, BigDecimal amount) {
        return execute(this.getAccountByNameUseCase.execute(userFrom), this.getAccountByNameUseCase.execute(userTo), currency, amount);
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount, Context context) {
        return this.execute(getAccountByUUIDUseCase.execute(targetUUID), currencyName, amount,context);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount,Context context) {
        return this.execute(this.getAccountByNameUseCase.execute(targetName), currencyName, amount,context);
    }

    public Result<Void> execute(Result<Account> accountResult, String currencyName, BigDecimal amount,Context context) {
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = currencyName == null ? this.searchCurrencyUseCase.getDefaultCurrency() : this.searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }
        return performTransaction(accountResult.getValue(), currencyResult.getValue(), amount,context);
    }

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

    public Result<Void> execute(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        return execute(getAccountByUUIDUseCase.execute(userFrom), getAccountByUUIDUseCase.execute(userTo), currencyFromS, currencyToS, amountFrom, amountTo);
    }

    public Result<BigDecimal> execute(UUID accountUuid, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        return execute(getAccountByUUIDUseCase.execute(accountUuid),currencyFromName, currencyToname, amountFrom, amountTo);
    }

    public Result<Void> execute(String userFrom, String userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        return execute(this.getAccountByNameUseCase.execute(userFrom), this.getAccountByNameUseCase.execute(userTo), currencyFromS, currencyToS, amountFrom, amountTo);
    }

    public Result<BigDecimal> execute(String accountString, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        return execute(this.getAccountByNameUseCase.execute(accountString),currencyFromName, currencyToname, amountFrom, amountTo);
    }

    private Result<BigDecimal> execute(Result<Account> accountResult, String currencyFromName, String currencyToname, BigDecimal amountFrom, BigDecimal amountTo) {
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyFromResult = this.searchCurrencyUseCase.getCurrency(currencyFromName);
        if (!currencyFromResult.isSuccess()) {
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult = this.searchCurrencyUseCase.getCurrency(currencyToname);
        if (!currencyToResult.isSuccess()) {
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performTransaction(accountResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
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

        return performTransaction(accountFromResult.getValue(), accountToResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    protected Result<BigDecimal> performTransaction(Account account, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        return Result.failure( "Not implemented", ErrorCode.NOT_IMPLEMENTED);
    };

    protected Result<Void> performTransaction(Account accountFrom, Account accountTo, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        return Result.failure( "Not implemented", ErrorCode.NOT_IMPLEMENTED);
    };

    protected Result<Void> performTransaction(Account accountFrom, Account accountTo, Currency currency, BigDecimal amount){
        return Result.failure( "Not implemented", ErrorCode.NOT_IMPLEMENTED);
    };

    protected Result<Void> performTransaction(Account account, Currency currency, BigDecimal amount,Context context){
        return Result.failure( "Not implemented", ErrorCode.NOT_IMPLEMENTED);
    };
}