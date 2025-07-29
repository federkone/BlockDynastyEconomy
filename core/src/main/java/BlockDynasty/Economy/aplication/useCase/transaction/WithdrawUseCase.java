package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.WithdrawEvent;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class WithdrawUseCase {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final EventManager eventManager;
    private final Log logger;
    private final SearchAccountUseCase searchAccountUseCase;

    public WithdrawUseCase(SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase, IRepository dataStore, Courier updateForwarder, Log logger, EventManager eventManager){
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = logger;
        this.searchAccountUseCase = searchAccountUseCase;
        this.eventManager = eventManager;
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(targetUUID);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }
        return this.execute(accountResult.getValue(), currencyName, amount);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(targetName);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }
        return this.execute(accountResult.getValue(), currencyName, amount);
    }

    public Result<Void> execute(UUID targetUUID, BigDecimal amount) {
        return this.execute(targetUUID, null, amount);
    }

    public Result<Void> execute(String targetName, BigDecimal amount) {
        return this.execute(targetName, null, amount);
    }

    private Result<Void> execute(Account account, String currencyName, BigDecimal amount) {
        Result<Currency> currencyResult = this.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }
        return performWithdraw(account, currencyResult.getValue(), amount);
    }

    private Result<Currency> getCurrency(String currencyName) {
        if (currencyName == null) {
            return this.searchCurrencyUseCase.getDefaultCurrency();
        }
        return this.searchCurrencyUseCase.getCurrency(currencyName);
    }

    private Result<Void> performWithdraw(Account account, Currency currency, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isValidAmount(amount)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result = this.dataStore.withdraw(account.getUuid().toString(), currency, amount);
        if(!result.isSuccess()){
            this.logger.log("[WITHDRAW Failure] Account: " + account.getNickname() + " extrajo " + currency.format(amount) + " de " + currency.getSingular()+ " - Error: " + result.getErrorMessage() + " - Code: " + result.getErrorCode());
            return Result.failure( result.getErrorMessage(), result.getErrorCode());
        }

        this.searchAccountUseCase.syncCacheWithAccount(result.getValue());

        this.updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
        this.logger.log("[WITHDRAW] Account: " + account.getNickname() + " extrajo " + currency.format(amount) + " de " + currency.getSingular());
        this.eventManager.emit(new WithdrawEvent(account.getPlayer(), currency, amount));

    return Result.success(null);
    }
}