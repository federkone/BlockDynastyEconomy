package me.BlockDynasty.Economy.domain.entities.account;

import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.*;

public class Account implements IAccount {
    private String uuid;
    private String nickname;
    private List<Balance> wallet;
    private boolean canReceiveCurrency = true;

    public Account(UUID uuid, String nickname) {
        this.uuid = uuid.toString();
        this.nickname = nickname;
        this.wallet = new ArrayList<>();
    }

    public Account(UUID uuid, String nickname, List<Balance> balanceList, boolean canReceiveCurrency) {
        this.uuid = uuid.toString();
        this.nickname = nickname;
        this.wallet = balanceList;
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

    public void setWallet(List<Balance> wallet) {
        this.wallet = wallet;
    }
    public List<Balance> getWallet() {
        return wallet;
    }

    public boolean hasCurrency( String currencyName){
        return wallet.stream().anyMatch(b ->
                b.getCurrency().getSingular().equals(currencyName) || b.getCurrency().getPlural().equals(currencyName));
    }

    public Balance getBalance(Currency currency) {
        return wallet.stream()
                .filter(b -> b.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }

    public Balance getBalance(){
        return wallet.stream()
                .filter(b -> b.getCurrency().isDefaultCurrency())
                .findFirst()
                .orElse(null);
    }

    public Balance getBalance(String currencyName){
        return wallet.stream()
                .filter(b -> b.getCurrency().getSingular().equalsIgnoreCase(currencyName) || b.getCurrency().getPlural().equalsIgnoreCase(currencyName))
                .findFirst()
                .orElse(null);
    }
//tiene monto
    public boolean hasEnough(Currency currency, BigDecimal amount){
        Balance balance = getBalance(currency);
        if (balance == null) {
            return false;
        }
        return balance.hasEnough(amount);
    };

    public boolean hasEnoughDefaultCurrency(BigDecimal amount){
        Balance balance = getBalance();
        if (balance == null) {
            return false;
        }
        return balance.hasEnough(amount);
    };

    private void createBalance(Currency currency, BigDecimal amount) {
        Balance balance = new Balance(currency, amount);
        wallet.add(balance);
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
