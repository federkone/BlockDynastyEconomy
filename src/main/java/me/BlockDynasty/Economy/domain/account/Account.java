package me.BlockDynasty.Economy.domain.account;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(60)")
    private String uuid;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private List<Balance> balances;

    @Column(name = "can_receive_currency")
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

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }
    public List<Balance> getBalances() {
        return balances;
    }

    public boolean hasCurrency( String currencyName){
        return balances.stream().anyMatch(b ->
                b.getCurrency().getSingular().equals(currencyName) || b.getCurrency().getPlural().equals(currencyName));
    }

    public Balance getBalance(Currency currency) {
        return balances.stream()
                .filter(b -> b.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }

    public Balance getBalance(){
        return balances.stream()
                .filter(b -> b.getCurrency().isDefaultCurrency())
                .findFirst()
                .orElse(null);
    }

    public Balance getBalance(String currencyName){
        return balances.stream()
                .filter(b -> b.getCurrency().getSingular().equalsIgnoreCase(currencyName) || b.getCurrency().getPlural().equalsIgnoreCase(currencyName))
                .findFirst()
                .orElse(null);
    }
//tiene monto
    public boolean hasEnough(Currency currency, BigDecimal amount){
        Balance balance = getBalance(currency);
        return balance != null && balance.getBalance().compareTo(amount) >= 0;
    };

    public boolean hasEnough(BigDecimal amount){
        Balance balance = getBalance();
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
