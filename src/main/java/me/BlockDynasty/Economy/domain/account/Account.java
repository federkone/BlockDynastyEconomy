
package me.BlockDynasty.Economy.domain.account;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.utils.UUIDConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "uuid", columnDefinition = "VARCHAR(60)")
    @Convert(converter = UUIDConverter.class)
    private UUID uuid;

    @Column(name = "nickname")
    private String nickname;

    @ElementCollection(fetch = FetchType.EAGER) //TODO TEST para ver si se carga bien y hibernate no tire excepcion de lazy
    @CollectionTable(name = "account_balances", joinColumns = @JoinColumn(name = "account_uuid"))
    @MapKeyJoinColumn(name = "currency_uuid")
    @Column(name = "balance")
    private Map<Currency, Double> balances;

    @Column(name = "can_receive_currency")
    private boolean canReceiveCurrency = true;

    public Account(UUID uuid, String nickname) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.balances = new HashMap<>();
    }

    public Account() {

    }

    public void deposit(Currency currency, double amount) {
        if (getBalances().containsKey(currency)) {
            getBalances().put(currency, getBalances().get(currency) + amount);
        } else {
            getBalances().put(currency, amount);
        }
    }

    public void withdraw(Currency currency, double amount) {
        if (getBalances().containsKey(currency)) {
            getBalances().put(currency, getBalances().get(currency) - amount);
        }// else {
         //   getBalances().put(currency, -amount);
        //}
    }

    public void setBalance(Currency currency, double amount) {
        getBalances().put(currency, amount);
    }

    public double getBalance(Currency currency) {
        if (getBalances().containsKey(currency)) {
            return getBalances().get(currency);
        }
        return currency.getDefaultBalance();
    }

    public double getBalance(String identifier){
        for(Currency currency : getBalances().keySet()){
            if(currency.getPlural().equalsIgnoreCase(identifier) || currency.getSingular().equalsIgnoreCase(identifier)){
                return getBalances().get(currency);
            }
        }
        return 0; // Do not edit this
    }

    //public boolean exchange(Currency currencyFrom, Currency currencyTo, double amountFrom, double amountTo){
    //    if(hasEnough(currencyFrom, amountFrom)){
    //        withdraw(currencyFrom, amountFrom);
    //        deposit(currencyTo, amountTo);
    //        return true;
    //    }
    //    return false;
    //}

    public String getDisplayName() {
        return getNickname() != null ? getNickname() : getUuid().toString();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean hasEnough(Currency currency, double amount){
        return getBalance(currency) >= amount;
    }

    public boolean canReceiveCurrency() {
        return canReceiveCurrency;
    }

    public void setCanReceiveCurrency(boolean canReceiveCurrency) {
        this.canReceiveCurrency = canReceiveCurrency;
    }

    public void setBalances(Map<Currency,Double> balances){
        this.balances = balances;
    }

    public Map<Currency, Double> getBalances() {
        return balances;
    }
}

