package me.BlockDynasty.Economy.domain.entities.account;

import me.BlockDynasty.Economy.domain.entities.wallet.Wallet;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.*;

public class Account implements IAccount {
    private String uuid;
    private String nickname;
    private Wallet wallet;
    private boolean canReceiveCurrency ;

    public Account(UUID uuid, String nickname) {
        this.uuid = uuid.toString();
        this.nickname = nickname;
        this.wallet = new Wallet();
        this.canReceiveCurrency = true;
    }

    public Account(UUID uuid, String nickname, List<Balance> balanceList, boolean canReceiveCurrency) {
        this.uuid = uuid.toString();
        this.nickname = nickname;
        this.wallet = new Wallet(balanceList);
        this.canReceiveCurrency = canReceiveCurrency;
    }

    public Account(String uuid, String nickname, Wallet wallet, boolean canReceiveCurrency) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.wallet = wallet;
        this.canReceiveCurrency = canReceiveCurrency;
    }

    public Result<Void> subtract(Currency currency, BigDecimal amount){
        Balance balance = getBalance(currency);
        if (balance == null) {
            return Result.failure("No balance found for currency" , ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }
        Result<Void> result = balance.subtract(amount);
        if (!result.isSuccess()) {
            return result;
        }
        return Result.success(null);
    }

    public Result<Void> add(Currency currency, BigDecimal amount) {
        Balance balance = getBalance(currency);
        if (balance == null) {
            return Result.failure("No balance found for currency" , ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        } else {
            Result<Void> result = balance.add(amount);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return Result.success(null);
    }

    public Result<Void> setBalance(Currency currency, BigDecimal amount) {
        Balance balance = getBalance(currency);
        if (balance == null) {
            createBalance(currency, amount);
        }else{
            Result<Void> result = balance.setAmount(amount);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return Result.success(null);
    }

    public void setBalances(List<Balance> balances) {
        this.wallet.setBalances(balances);
    }
    public List<Balance> getBalances() {
        return wallet.getBalances();
    }

    public boolean hasCurrency( String currencyName){
        return wallet.hasCurrency(currencyName);
    }

    public Balance getBalance(Currency currency) {
        return wallet.getBalance(currency);
    }

    public Balance getBalance(){
        return wallet.getBalance();
    }

    public Balance getBalance(String currencyName){
        return wallet.getBalance(currencyName);
    }

    public boolean hasEnough(Currency currency, BigDecimal amount){
        Balance balance = getBalance(currency);
        if (balance == null) {
            return false;
        }
        return balance.hasEnough(amount);
    }

    @Override
    public Wallet getWallet() {
        return this.wallet;
    }

    @Override
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    ;

    public boolean hasEnoughDefaultCurrency(BigDecimal amount){
        Balance balance = getBalance();
        if (balance == null) {
            return false;
        }
        return balance.hasEnough(amount);
    };

    private void createBalance(Currency currency, BigDecimal amount) {
        wallet.createBalance(currency, amount);
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
