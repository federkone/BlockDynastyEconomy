package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.SetEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
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

public class SetBalanceUseCase {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final EventManager eventManager;
    private final Log economyLogger;
    private final SearchAccountUseCase searchAccountUseCase;
    private final IAccountService accountService;

    public SetBalanceUseCase(SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase,
                             IAccountService accountService,IRepository dataStore,
                             Courier updateForwarder, Log economyLogger, EventManager eventManager) {

        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.searchAccountUseCase = searchAccountUseCase;
        this.accountService = accountService;

        this.eventManager = eventManager;
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Result<Account> accountResult =  this.searchAccountUseCase.getAccount(targetUUID);
        return execute(accountResult, currencyName, amount);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult =  this.searchAccountUseCase.getAccount(targetName);
        return execute(accountResult,currencyName,amount);
    }

    private Result<Void> execute(Result<Account> accountResult,String currencyName, BigDecimal amount){
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }
        Result<Currency> currencyResult =  this.searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performSet(accountResult.getValue(), currencyResult.getValue(), amount);
    }

    private Result<Void> performSet(Account account, Currency currency, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0){
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isValidAmount(amount)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result =  this.dataStore.setBalance(account.getUuid().toString(), currency, amount);
        if (!result.isSuccess()) {
            this.economyLogger.log("[BALANCE SET failed] Account: " + account.getNickname() + " were set to: " + currency.format(amount) + " Error: " + result.getErrorMessage() + " Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.accountService.syncOnlineAccount(result.getValue());
        this.updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
        this.economyLogger.log("[BALANCE SET] Account: " + account.getNickname() + " were set to: " + currency.format(amount));
        this.eventManager.emit( new SetEvent(account.getPlayer(), currency, amount));

        return Result.success(null);
    }
}
