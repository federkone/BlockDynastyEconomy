package me.BlockDynasty.Economy.domain.account;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountCanNotReciveException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.utils.UUIDConverter;

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

    public void deposit(Currency currency, BigDecimal amount) {
        if(!canReceiveCurrency){
            throw new AccountCanNotReciveException("Account can't receive currency");
        }
        Balance balance = getBalance(currency);
        if (balance == null) {
            createBalance(currency, amount);
        } else {
            balance.deposit(amount);
        }
    }

    public void withdraw(Currency currency, BigDecimal amount) {
        Balance balance = getBalance(currency);
        if (balance == null) {
            throw new CurrencyNotFoundException("Currency not found in account");
        }
        balance.withdraw(amount);
    }

    public void setBalance(Currency currency, BigDecimal amount) {
        Balance balance = getBalance(currency);
        if (balance == null) {
            createBalance(currency, amount);
        }else{
            balance.setBalance(amount);
        }
    }

    //---- si el objetivo puede recibir currencies
    //---- si ambos cuentan con los montos necesarios
    //-----y ejecutar el tradeo, creo que deberia hacer dos withdraw primero y luego los dos deposit para evirar problemas
    //-----tengo los hasEnough/tiene monto para preguntarle a cada cuenta antes de hacer el tradeo
    public void trade(Account account,Currency currencyFrom,Currency currencyTo, BigDecimal amountFrom, BigDecimal amountTo){
        if(!account.canReceiveCurrency()){
            throw new AccountCanNotReciveException("Account can't receive currency");
        }
        if(!hasEnough(currencyFrom, amountFrom) || !account.hasEnough(currencyTo, amountTo)){
            throw new InsufficientFundsException("Accounts doesn't have sufficient founds");
        }

        withdraw(currencyFrom, amountFrom);
        account.withdraw(currencyTo, amountTo);
        deposit(currencyTo, amountTo);
        account.deposit(currencyFrom, amountFrom);
    }

    public void transfer(Account account, Currency currency, BigDecimal amount) {
        if(!account.canReceiveCurrency()){
            throw new AccountCanNotReciveException("Account can't receive currency");
        }
        withdraw(currency, amount);
        account.deposit(currency, amount);
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
