package me.BlockDynasty.Economy.domain.account;


import jakarta.persistence.*;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.aplication.result.ErrorCode;

import java.math.BigDecimal;
import java.util.*;


@Entity
@Table(name = "accounts")
public class Account {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private int id;

    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(60)")
   //@Convert(converter = UUIDConverter.class)
    private String uuid;

    @Column(name = "nickname")
    private String nickname;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private List<Balance> balances;

    @Column(name = "can_receive_currency")
    private boolean canReceiveCurrency = true;


    public Account(){

    }
    public Account(UUID uuid, String nickname) {
        this.uuid = uuid.toString();
        this.nickname = nickname;
        this.balances = new ArrayList<>();
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

    public Result<Void> exchange(Currency currencyFrom, BigDecimal amountFrom, Currency currencyTo,BigDecimal amountTo){
        if (!this.canReceiveCurrency()) {
            return Result.failure("Target account can't receive currency", ErrorCode.ACCOUNT_CAN_NOT_RECEIVE);
        }
        if(!hasEnough(currencyFrom, amountFrom)){
            return Result.failure("Account doesn't have sufficient founds for exchange", ErrorCode.INSUFFICIENT_FUNDS);
        }

                Result<Void> withdrawResult = withdraw(currencyFrom, amountFrom);
                if (!withdrawResult.isSuccess()) {
                    return withdrawResult;
                }

                // Depositar en la moneda objetivo
                Result<Void> depositResult = deposit(currencyTo, amountTo);
                if (!depositResult.isSuccess()) {
                    return depositResult;
                }

            return Result.success(null);

    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public boolean haveCurrency( String currencyName){
        return balances.stream().anyMatch(b ->
                b.getCurrency().getSingular().equals(currencyName) ||
                        b.getCurrency().getPlural().equals(currencyName)
        );
    }

    public Balance getBalance(Currency currency) {
        return balances.stream()
                .filter(b -> b.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }

    public Balance getBalance(String currencyName){
        return balances.stream()
                .filter(b -> b.getCurrency().getSingular().equals(currencyName) || b.getCurrency().getPlural().equals(currencyName))
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
