package BlockDynasty.Economy.domain.entities.balance;

import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;

import java.math.BigDecimal;

public interface IBalance {
    Result<Void> subtract(BigDecimal amount);
    Result<Void> add(BigDecimal amount);
    Result<Void> setAmount(BigDecimal amount);

    void setCurrency(Currency currency);
    BigDecimal getAmount();
    Currency getCurrency();
    boolean hasEnough(BigDecimal amount);
}
