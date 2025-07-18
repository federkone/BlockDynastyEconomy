package me.BlockDynasty.Economy.domain.entities.wallet;

import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface IWallet {
    boolean hasCurrency( String currencyName);
    Balance getBalance(Currency currency);
    Balance getBalance();
    Balance getBalance(String currencyName);
    void setBalances(List<Balance> balances);
    void createBalance(Currency currency, BigDecimal amount);
    List<Balance> getBalances() ;
}
