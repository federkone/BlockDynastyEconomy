package me.BlockDynasty.Economy.aplication.useCase.transaction;

import me.BlockDynasty.Economy.domain.services.courier.Courier;
import me.BlockDynasty.Economy.domain.services.log.Log;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.result.TransferResult;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;

import java.math.BigDecimal;
import java.util.UUID;

//TODO ESTA FUNCIONALIDAD PERMITE TRADEAR 2 MONEDAS DISTINTAS ENTRE 2 PERSONAS DISTINTAS, NO NECESITA VALIDAR SI LA MONEDA ES PAGABLE O NO, YA QUE ES UN TRADE
//trade, que 2 personas intercambien 2 monedas distintas
//TODO: TAMBIEN SE PODRIA COBRAR IMPUESTO POR TRADE
public class TradeCurrenciesUseCase {

    private final GetCurrencyUseCase getCurrencyUseCase ;
    private final IRepository dataStore;
    private final Courier updateForwarder;
    private final Log economyLogger;
    private final GetAccountsUseCase getAccountsUseCase;

    public TradeCurrenciesUseCase( GetCurrencyUseCase getCurrencyUseCase,GetAccountsUseCase getAccountsUseCase, IRepository dataStore,
                                  Courier updateForwarder, Log economyLogger) {

        this.getCurrencyUseCase = getCurrencyUseCase;
        this.dataStore = dataStore;
        this.updateForwarder = updateForwarder;
        this.getAccountsUseCase = getAccountsUseCase;
        this.economyLogger = economyLogger;

    }

    public Result<Void> execute(UUID userFrom, UUID userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Result<Account> accountFromResult =  this.getAccountsUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            //messageService.sendMessage(userFrom, accountFromResult.getErrorMessage());
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult =  this.getAccountsUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            //messageService.sendMessage(userTo, accountToResult.getErrorMessage());
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyFromResult =  this.getCurrencyUseCase.getCurrency(currencyFromS);
        if (!currencyFromResult.isSuccess()) {
            //messageService.sendMessage(currencyFromS, currencyFromResult.getErrorMessage());
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult =  this.getCurrencyUseCase.getCurrency(currencyToS);
        if (!currencyToResult.isSuccess()) {
            //messageService.sendMessage(currencyToS, currencyToResult.getErrorMessage());
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performTrade(accountFromResult.getValue(), accountToResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);

    }

    public Result<Void> execute(String userFrom, String userTo, String currencyFromS, String currencyToS, BigDecimal amountFrom, BigDecimal amountTo){
        Result<Account> accountFromResult =  this.getAccountsUseCase.getAccount(userFrom);
        if (!accountFromResult.isSuccess()) {
            //messageService.sendMessage(userFrom, accountFromResult.getErrorMessage());
            return Result.failure(accountFromResult.getErrorMessage(), accountFromResult.getErrorCode());
        }

        Result<Account> accountToResult =  this.getAccountsUseCase.getAccount(userTo);
        if (!accountToResult.isSuccess()) {
            //messageService.sendMessage(userTo, accountToResult.getErrorMessage());
            return Result.failure(accountToResult.getErrorMessage(), accountToResult.getErrorCode());
        }

        Result<Currency> currencyFromResult =  this.getCurrencyUseCase.getCurrency(currencyFromS);
        if (!currencyFromResult.isSuccess()) {
            //messageService.sendMessage(currencyFromS, currencyFromResult.getErrorMessage());
            return Result.failure(currencyFromResult.getErrorMessage(), currencyFromResult.getErrorCode());
        }

        Result<Currency> currencyToResult =  this.getCurrencyUseCase.getCurrency(currencyToS);
        if (!currencyToResult.isSuccess()) {
            //messageService.sendMessage(currencyToS, currencyToResult.getErrorMessage());
            return Result.failure(currencyToResult.getErrorMessage(), currencyToResult.getErrorCode());
        }

        return performTrade(accountFromResult.getValue(), accountToResult.getValue(), currencyFromResult.getValue(), currencyToResult.getValue(), amountFrom, amountTo);
    }

    private Result<Void> performTrade (Account accountFrom, Account accountTo, Currency currencyFrom, Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){

        if(!accountTo.canReceiveCurrency()){
            //messageService.sendMessage(accountTo, "Account can't receive currency");
            return Result.failure("Account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(amountFrom.compareTo(BigDecimal.ZERO) <= 0){
            //messageService.sendMessage(currencyFrom, "Amount must be greater than 0");
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(amountTo.compareTo(BigDecimal.ZERO) <= 0){
            //messageService.sendMessage(currencyTo, "Amount must be greater than 0");
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currencyFrom.isValidAmount(amountFrom)){
            //messageService.sendMessage(currencyFrom, "Decimal not supported");
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if(!currencyTo.isValidAmount(amountTo)){
            //messageService.sendMessage(currencyTo, "Decimal not supported");
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        Result<TransferResult> result = this.dataStore.trade( accountFrom.getUuid().toString(), accountTo.getUuid().toString(), currencyFrom, currencyTo, amountFrom, amountTo);
        if(!result.isSuccess()){
            //messageService.sendMessage(accountFrom, result.getErrorMessage());
            this.economyLogger.log("[TRADE failed] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo) + " - Error: " + result.getErrorMessage() + " - Code: " + result.getErrorCode());
            return Result.failure(result.getErrorMessage(), result.getErrorCode());
        }

        this.getAccountsUseCase.updateAccountCache(result.getValue().getTo());
        this.getAccountsUseCase.updateAccountCache(result.getValue().getFrom());

        //messageService.sendMessage(TransferResult, ErrorCode.TRANSFER_SUCCESS);

        this.updateForwarder.sendUpdateMessage("account", accountFrom.getUuid().toString());
        this.updateForwarder.sendUpdateMessage("account", accountTo.getUuid().toString());
        this.economyLogger.log("[TRADE] Account: " + accountFrom.getNickname() + " traded " + currencyFrom.format(amountFrom) + " to " + accountTo.getNickname() + " for " + currencyTo.format(amountTo));

        return Result.success(null);
    }
}
