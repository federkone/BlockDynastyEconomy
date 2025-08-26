package BlockDynasty.Economy.aplication.useCase.transaction;

import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.domain.events.transactionsEvents.DepositEvent;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositUseCase {
    private final SearchCurrencyUseCase searchCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final EventManager eventManager;
    private final Log logger;
    private final SearchAccountUseCase searchAccountUseCase;
    private final IAccountService accountService;

    public DepositUseCase(SearchCurrencyUseCase searchCurrencyUseCase, SearchAccountUseCase searchAccountUseCase, IAccountService accountService,
                          IRepository dataStore, Courier updateForwarder, Log logger, EventManager eventManager) {
        this.searchCurrencyUseCase = searchCurrencyUseCase;
        this.accountService = accountService;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = logger;
        this.eventManager = eventManager;
        this.searchAccountUseCase = searchAccountUseCase;
    }

    public Result<Void> execute(UUID targetUUID, BigDecimal amount) {
        return this.execute(targetUUID, null, amount);
    }

    public Result<Void> execute(String targetName, BigDecimal amount) {
        return this.execute(targetName, null, amount);
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(targetUUID);
        return execute(accountResult, currencyName, amount);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = this.searchAccountUseCase.getAccount(targetName);
        return execute(accountResult, currencyName, amount);
    }

    private Result<Void> execute(Result<Account> accountResult, String currencyName, BigDecimal amount) {
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = currencyName == null ? this.searchCurrencyUseCase.getDefaultCurrency() : this.searchCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }
        return performDeposit(accountResult.getValue(), currencyResult.getValue(), amount);
    }

    private Result<Void> performDeposit(Account account, Currency currency, BigDecimal amount) {
        if (!account.canReceiveCurrency()) {
            return Result.failure("Account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if (account.isBlocked()){
            return Result.failure("Account is blocked", ErrorCode.ACCOUNT_BLOCKED);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isValidAmount(amount)){
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result = dataStore.deposit(account.getUuid().toString(), currency, amount);
        if(!result.isSuccess()){
            this.logger.log("[DEPOSIT failed] Account: " + account.getNickname() + " recibió un deposito de " + currency.format(amount) + " de " + currency.getSingular() + " pero falló: " + result.getErrorMessage() + " (" + result.getErrorCode() + ")");
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.accountService.syncOnlineAccount(result.getValue());
        this.updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
        this.logger.log("[DEPOSIT] Account: " + account.getNickname() + " recibió un deposito de " + currency.format(amount) + " de " + currency.getSingular());
        this.eventManager.emit(new DepositEvent(account.getPlayer(), currency, amount));

        return Result.success(null);
    }
}