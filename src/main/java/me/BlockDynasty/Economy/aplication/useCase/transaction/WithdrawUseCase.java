package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

//TODO, FUNCIONALIDAD PARA EXTRACCION DE DINERO
public class WithdrawUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger logger;
    private final GetAccountsUseCase getAccountsUseCase;

    public WithdrawUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                           UpdateForwarder updateForwarder, AbstractLogger logger){
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.logger = logger;
        this.getAccountsUseCase = getAccountsUseCase;
    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = getAccountsUseCase.getAccount(targetUUID);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }
        return performWithdraw(accountResult.getValue(), currencyResult.getValue(), amount);
    }

    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        Result<Account> accountResult = getAccountsUseCase.getAccount(targetName);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performWithdraw(accountResult.getValue(), currencyResult.getValue(), amount);
    }

    private Result<Void> performWithdraw(Account account, Currency currency, BigDecimal amount) {
        Result<Void> result = account.withdraw(currency, amount); //todo: revisar metodos de actualizar valores antes de guardar en db, verificar condiciones de carrera
        if(!result.isSuccess()){
            return result;
        }

        try {
            dataStore.saveAccount(account);
            if(updateForwarder != null && logger != null) { //todo , lo puse para testear y ommitir esto
                updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
                logger.log("[WITHDRAW] Account: " + account.getNickname() + " extrajo " + currency.format(amount) + " de " + currency.getSingular());
            }
        } catch (TransactionException e) {
            //throw new TransactionException("Error saving account",e);
            return Result.failure("Error saving account", ErrorCode.DATA_BASE_ERROR);
        }
    return Result.success(null);
    }

}
