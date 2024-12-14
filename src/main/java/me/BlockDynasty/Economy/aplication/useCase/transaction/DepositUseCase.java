package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.domain.repository.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositUseCase {
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final IRepository dataStore;
    private final UpdateForwarder updateForwarder;
    private final AbstractLogger economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public DepositUseCase(GetCurrencyUseCase getCurrencyUseCase, GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                          UpdateForwarder updateForwarder, AbstractLogger economyLogger){
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.economyLogger = economyLogger;
        this.getAccountsUseCase = getAccountsUseCase;

    }

    public Result<Void> execute(UUID targetUUID, String currencyName, BigDecimal amount) {
        //Account account = getAccountsUseCase.getAccount(targetUUID);
        //Currency currency = getCurrencyUseCase.getCurrency(currencyName);

        Result<Account> accountResult = getAccountsUseCase.getAccount(targetUUID);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performDeposit(accountResult.getValue(), currencyResult.getValue(), amount);
    }


    public Result<Void> execute(String targetName, String currencyName, BigDecimal amount) {
        //Account account = getAccountsUseCase.getAccount(targetName);
        //Currency currency = getCurrencyUseCase.getCurrency(currencyName);

        Result<Account> accountResult = getAccountsUseCase.getAccount(targetName);
        if (!accountResult.isSuccess()) {
            return Result.failure(accountResult.getErrorMessage(), accountResult.getErrorCode());
        }

        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return Result.failure(currencyResult.getErrorMessage(), currencyResult.getErrorCode());
        }

        return performDeposit(accountResult.getValue(), currencyResult.getValue(), amount);
    }


    //TODO: PREGUNTAR SI EL USUSARIO TIENE LA MONEDA? .DE MOMENTO TODAS LAS CUENTAS CUENTAN CON TODOS LOS TIPOS DE MONEDAS INICIALIZADAS
    private Result<Void> performDeposit(Account account, Currency currency, BigDecimal amount) {

//todo: revisar metodos de actualizar valores antes de guardar en db, verificar condiciones de carrera
        Result<Void> result = account.deposit(currency, amount);
        if(!result.isSuccess()){
            return result;
        }

        try {
            dataStore.saveAccount(account);
            if(updateForwarder != null && economyLogger != null) { //todo , lo puse para testear y ommitir esto
                updateForwarder.sendUpdateMessage("account", account.getUuid().toString());
                economyLogger.log("[DEPOSIT] Account: " + account.getNickname() + " recibió un deposito de " + currency.format(amount) + " de " + currency.getSingular());
            }
        } catch (TransactionException e) {
            //throw new TransactionException("Error saving account",e);
            return Result.failure("Error saving account", ErrorCode.DATA_BASE_ERROR);
        }

        return Result.success(null);
    }

}
