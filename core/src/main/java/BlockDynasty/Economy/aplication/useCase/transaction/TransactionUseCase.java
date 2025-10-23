package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.account.IAccount;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;

import java.math.BigDecimal;
import java.util.UUID;

public abstract class TransactionUseCase {
    protected final SearchCurrencyUseCase searchCurrencyUseCase;
    protected final SearchAccountUseCase searchAccountUseCase;
    protected final IRepository dataStore;
    protected final Courier updateForwarder;
    protected final EventManager eventManager;
    protected final Log logger;
    protected final IAccountService accountService;

    protected TransactionUseCase(IAccountService accountService, ICurrencyService currencyService,
                                 IRepository dataStore, Courier updateForwarder, Log logger, EventManager eventManager) {
        this.searchCurrencyUseCase = new SearchCurrencyUseCase(currencyService, dataStore);
        this.searchAccountUseCase = new SearchAccountUseCase(accountService, dataStore);
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
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(targetUUID);
        return this.execute(accountResult, currencyName, amount, Context.SYSTEM);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(targetName);
        return this.execute(accountResult, currencyName, amount,Context.SYSTEM);
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount, Context context) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(targetUUID);
        return this.execute(accountResult, currencyName, amount,context);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount,Context context) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(targetName);
        return this.execute(accountResult, currencyName, amount,context);
    }

    private Result<Void> execute(Result<Account> accountResult, String currencyName, BigDecimal amount,Context context) {
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = currencyName == null ? this.searchCurrencyUseCase.getDefaultCurrency() : this.searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }
        return performTransaction(accountResult.getValue(), currencyResult.getValue(), amount,context);
    }

    protected abstract Result<Void> performTransaction(Account account, Currency currency, BigDecimal amount,Context context);
}
