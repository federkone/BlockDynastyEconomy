package me.BlockDynasty.Economy.domain.balance;

import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;

public interface IBalance {
    Result<Void> subtract(BigDecimal amount);
    Result<Void> add(BigDecimal amount);
    Result<Void> setBalance(BigDecimal amount);

    void setCurrency(Currency currency);
    BigDecimal getBalance();
    Currency getCurrency();
    boolean hasEnough(BigDecimal amount);
}
