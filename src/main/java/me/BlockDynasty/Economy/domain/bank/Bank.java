package me.BlockDynasty.Economy.domain.bank;

import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/*
* crear banco(string banco) :void

borrar banco(string banco) : void

el banco tiene(string banco,double monto): boolean  //vaul del banco//si tiene dinero

extraer de banco(string banco, double monto) : void

depositar en banco(string,double monto) : void

es dueño del banco(string banco,string playerNmae) : boolean

balance del banco(string nombre): double

obtener bancos(): lista de bancos
* */
public class Bank {
    private String name;
    private String owner;
    private UUID ouwnerUUID;
    private List<Balance> balances;

    public Bank(String name,UUID ouwnerUUID, String owner) {
        this.ouwnerUUID = ouwnerUUID;
        this.name = name;
        this.owner = owner;
        this.balances = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addBalance(Balance balance) {
        this.balances.add(balance);
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public String getOwnerName() {
        return owner;
    }

    public UUID getOwnerUUID() {
        return ouwnerUUID;
    }

    public BigDecimal getBalance(Currency currency) {
        for (Balance balance : balances) {
            if (balance.getCurrency().equals(currency)) {
                return balance.getBalance();
            }
        }
        return BigDecimal.ZERO; // Return zero if no balance found for the currency
    }

    public void deposit(Currency currency,BigDecimal amount) {
        for (Balance balance : balances) {
            if (balance.getCurrency().equals(currency)) {
                balance.setBalance(balance.getBalance().add(amount));
                return;
            }
        }
    }

    public void withdraw(Currency currency,BigDecimal amount) {
        for (Balance balance : balances) {
            if (balance.getCurrency().equals(currency)) {
                BigDecimal newBalance = balance.getBalance().subtract(amount);
                if (newBalance.compareTo(BigDecimal.ZERO) >= 0) {
                    balance.setBalance(newBalance);
                }
                return;
            }
        }
    }
}
