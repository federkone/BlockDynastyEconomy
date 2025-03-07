package me.BlockDynasty.Economy.domain.account;

import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.aplication.result.ErrorCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Account {
    private String uuid;
    private String nickname;
    private List<Balance> balances;
    private boolean canReceiveCurrency = true;

    public Account() {
    }
    public Account(UUID uuid, String nickname) {
        this.uuid = uuid.toString();
        this.nickname = nickname;
        this.balances = new ArrayList<>();
    }

    public Account(UUID uuid, String nickname, List<Balance> balanceList, boolean canReceiveCurrency) {
        this.uuid = uuid.toString();
        this.nickname = nickname;
        this.balances = balanceList;
        this.canReceiveCurrency = canReceiveCurrency;
    }

    public Result<Void> deposit(Currency currency, BigDecimal amount) {
        if(!canReceiveCurrency){
            return Result.failure("Account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
        Balance balance = getBalance(currency);
        if (balance == null) {
            createBalance(currency, amount);
        } else {
            Result<Void> result = balance.deposit(amount);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return Result.success(null);
    }

    public Result<Void> withdraw(Currency currency, BigDecimal amount) {
        Balance balance = getBalance(currency);
        if (balance == null) {
            return Result.failure("Currency not found in account", ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }
        Result<Void> result = balance.withdraw(amount);
        if (!result.isSuccess()) {
            return result;
        }
        return Result.success(null);
    }

    public Result<Void> setBalance(Currency currency, BigDecimal amount) {
        Balance balance = getBalance(currency);
        if (balance == null) {
            createBalance(currency, amount);
        }else{
            Result<Void> result = balance.setBalance(amount);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return Result.success(null);
    }

    public Result<Void> trade(Account targetAccount,Currency currencyFrom,Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        if(!targetAccount.canReceiveCurrency()){
            return Result.failure("Account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
        if(!hasEnough(currencyFrom, amountFrom) || !targetAccount.hasEnough(currencyTo, amountTo)){
            return Result.failure("Accounts doesn't have sufficient founds for trade", ErrorCode.INSUFFICIENT_FUNDS);
        }

                Result<Void> withdrawFromResult = withdraw(currencyFrom, amountFrom);
                if (!withdrawFromResult.isSuccess()) {
                    return withdrawFromResult;
                }

                Result<Void> withdrawToResult = targetAccount.withdraw(currencyTo, amountTo);
                if (!withdrawToResult.isSuccess()) {
                    return withdrawToResult;
                }

                Result<Void> depositToResult = deposit(currencyTo, amountTo);
                if (!depositToResult.isSuccess()) {
                    return depositToResult;
                }

                Result<Void> depositFromResult = targetAccount.deposit(currencyFrom, amountFrom);
                if (!depositFromResult.isSuccess()) {
                    return depositFromResult;
                }

            return Result.success(null);


    }

    public Result<Void> transfer(Account targetAccount, Currency currency, BigDecimal amount) {
        // Verificar si la cuenta objetivo puede recibir la moneda
        if (!targetAccount.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
                // Intentar retirar de la cuenta actual
                Result<Void> withdrawResult = withdraw(currency, amount);
                if (!withdrawResult.isSuccess()) {
                    return withdrawResult; // Propagar el error del withdraw
                }

                // Intentar depositar en la cuenta objetivo
                Result<Void> depositResult = targetAccount.deposit(currency, amount);
                if (!depositResult.isSuccess()) {
                    return depositResult; // Propagar el error del depósito
                }

            return Result.success(null);

    }

    public Result<BigDecimal> exchange(Currency currencyFrom, BigDecimal amountFrom, Currency currencyTo,BigDecimal amountTo){
        if(!currencyTo.isDecimalSupported() && amountTo.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){  //todo, el mismo error sucede en transfer, corregir, comprobar todo aqui antes de llamar a withdraw y deposit
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if (!this.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
        if(!hasEnough(currencyFrom, amountFrom)){
            return Result.failure("Account doesn't have sufficient founds for exchange", ErrorCode.INSUFFICIENT_FUNDS);
        }

                Result<Void> withdrawResult = withdraw(currencyFrom, amountFrom);
                if (!withdrawResult.isSuccess()) {
                    return Result.failure(amountFrom,withdrawResult.getErrorMessage(),withdrawResult.getErrorCode());
                }

                // Depositar en la moneda objetivo
                Result<Void> depositResult = deposit(currencyTo, amountTo);
                if (!depositResult.isSuccess()) {
                    return Result.failure(amountFrom,depositResult.getErrorMessage(),depositResult.getErrorCode());
                }

            return Result.success(amountFrom);

    }

    public Result<BigDecimal> exchange(Currency currencyFrom ,Currency currencyTo, BigDecimal amountTo){
        if (!this.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }

        if(!currencyTo.isDecimalSupported() && amountTo.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){  //todo, el mismo error sucede en transfer, corregir, comprobar todo aqui antes de llamar a withdraw y deposit
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        BigDecimal amountFrom = amountTo.multiply(BigDecimal.valueOf(currencyFrom.getExchangeRate()))
                .divide(BigDecimal.valueOf(currencyTo.getExchangeRate()),4, RoundingMode.HALF_UP);


        System.out.println("amountFrom: "+amountFrom);
        if(!hasEnough(currencyFrom, amountFrom)){
            return Result.failure("Account doesn't have sufficient founds for exchange", ErrorCode.INSUFFICIENT_FUNDS);
        }

        Result<Void> withdrawResult = withdraw(currencyFrom, amountFrom);
        if (!withdrawResult.isSuccess()) {
            return Result.failure(amountFrom,withdrawResult.getErrorMessage(),withdrawResult.getErrorCode());
        }

        // Depositar en la moneda objetivo
        Result<Void> depositResult = deposit(currencyTo, amountTo);
        if (!depositResult.isSuccess()) {
            return Result.failure(amountFrom,depositResult.getErrorMessage(),depositResult.getErrorCode());
        }

        return Result.success(amountFrom);

    }



    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public boolean haveCurrency( String currencyName){
        return balances.stream().anyMatch(b ->
                b.getCurrency().getSingular().equals(currencyName) || b.getCurrency().getPlural().equals(currencyName));
    }

    public Balance getBalance(Currency currency) {
        return balances.stream()
                .filter(b -> b.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }

    public Balance getBalance(String currencyName){
        return balances.stream()
                .filter(b -> b.getCurrency().getSingular().equalsIgnoreCase(currencyName) || b.getCurrency().getPlural().equalsIgnoreCase(currencyName))
                .findFirst()
                .orElse(null);
    }

    public boolean hasEnough(Currency currency, BigDecimal amount){
        Balance balance = getBalance(currency);
        return balance != null && balance.getBalance().compareTo(amount) >= 0;
    };
    public void createBalance(Currency currency, BigDecimal amount) {
        Balance balance = new Balance(currency, amount);
        balances.add(balance);
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }
    public void setNickname(String nickname) {
         this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void changeName(String newName){
        this.nickname = newName;
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public void setCanReceiveCurrency(boolean canReceiveCurrency) {
        this.canReceiveCurrency = canReceiveCurrency;
    }

    public boolean canReceiveCurrency() {
        return canReceiveCurrency;
    }
}
