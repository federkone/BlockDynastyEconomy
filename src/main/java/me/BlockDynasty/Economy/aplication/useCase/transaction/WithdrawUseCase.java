package me.BlockDynasty.Economy.aplication.useCase.transaction;

import com.mysql.cj.jdbc.SuspendableXAConnection;
import me.BlockDynasty.Economy.domain.services.courier.Courier;
import me.BlockDynasty.Economy.domain.services.log.Log;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

//TODO, FUNCIONALIDAD PARA EXTRACCION DE DINERO
public class WithdrawUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log logger;
    private final GetAccountsUseCase getAccountsUseCase;

    public WithdrawUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase, IRepository dataStore, Courier updateForwarder, Log logger){
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = logger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(targetUUID);
        if (!accountResult.isSuccess()) {
            //messageservice.sendMessage(result.getErrorMessage(), result.getErrorCode());
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }
        return this.execute(accountResult.getValue(), currencyName, amount);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = this.getAccountsUseCase.getAccount(targetName);
        if (!accountResult.isSuccess()) {
            //messageservice.sendMessage(result.getErrorMessage(), result.getErrorCode());
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

    private Result<Currency> getCurrency(String currencyName) {
        if (currencyName == null) {
            return this.getCurrencyUseCase.getDefaultCurrency();
        }
        return this.getCurrencyUseCase.getCurrency(currencyName);
    }

    private Result<Void> execute(Account account, String currencyName, BigDecimal amount) {
        Result<Currency> currencyResult = this.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            //messageservice.sendMessage(result.getErrorMessage(), result.getErrorCode());
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }
        return performWithdraw(account, currencyResult.getValue(), amount);
    }

    private Result<Void> performWithdraw(Account account, Currency currency, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            //messageservice.sendMessage(account,currency,amount, result.getErrorCode(), "Amount must be greater than 0");
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isValidAmount(amount)){
            //messageservice.sendMessage(account,currency,amount, ErrorCode.DECIMAL_NOT_SUPPORTED, "Decimal not supported");
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<Account> result = this.dataStore.withdraw(account.getUuid().toString(), currency, amount);
        if(!result.isSuccess()){
            //messageservice.sendMessage(account,currency,amount, result.getErrorCode(), "Withdraw failed: " + result.getErrorMessage());
            this.logger.log("[WITHDRAW Failure] Account: " + account.getNickname() + " extrajo " + currency.format(amount) + " de " + currency.getSingular()+ " - Error: " + result.getErrorMessage() + " - Code: " + result.getErrorCode());
            return Result.failure( result.getErrorMessage(), result.getErrorCode());
        }

        this.getAccountsUseCase.syncCacheWithAccount(result.getValue());
        //messageservice.sendMessage(account,currency,amount, ErrorCode.SUCCESS, "Withdraw successful");
        this.updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
        this.logger.log("[WITHDRAW] Account: " + account.getNickname() + " extrajo " + currency.format(amount) + " de " + currency.getSingular());

    return Result.success(null);
    }
}