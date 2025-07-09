package me.BlockDynasty.Economy.domain.account;

import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IAccount {
    Result<Void> subtract(Currency currency, BigDecimal amount);
    Result<Void> add(Currency currency, BigDecimal amount);
    Result<Void> setBalance(Currency currency, BigDecimal amount);

    void setBalances(List<Balance> balances);
    boolean hasCurrency( String currencyName);
    Balance getBalance(Currency currency);
    Balance getBalance();
    Balance getBalance(String currencyName);
    boolean hasEnough(Currency currency, BigDecimal amount);
    boolean hasEnough(BigDecimal amount);
    void createBalance(Currency currency, BigDecimal amount);
    void setUuid(UUID uuid);
    void setNickname(String nickname);
    String getNickname();
    UUID getUuid();
    void setCanReceiveCurrency(boolean canReceiveCurrency);
    boolean canReceiveCurrency();

}
