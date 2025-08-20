package BlockDynasty.Economy.domain.entities.balance;

import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;

public class Money implements IMoney{
    private Currency currency;
    private BigDecimal amount;

    public Money(Currency currency){
        this.currency= currency;
        this.amount = currency.getDefaultBalance();
    }

    public Money(Currency currency, BigDecimal amount){
        this.currency= currency;
        this.amount = amount;
    }

    public Money(Money money){
        this.currency = new Currency(money.getCurrency());
        this.amount = money.getAmount();
    }

    public void setCurrency(Currency currency){
        this.currency = currency;
    }

    public BigDecimal getAmount(){
        return this.amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String format(){
        return this.currency.format(amount);
    }

    public boolean hasEnough(BigDecimal amount){
        return this.amount.compareTo(amount) >= 0;
    };

    public Result<Void> setAmount(BigDecimal amount){
        if(!this.currency.isValidAmount(amount)){
            return Result.failure("Invalid amount for currency", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }
        if(amount.doubleValue() < 0){
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }
        this.amount = amount;
        return Result.success(null);
    }

    public Result<Void> subtract(BigDecimal amount) {
        if(!this.currency.isValidAmount(amount)){
            return Result.failure("Invalid amount for currency", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }
        if (!this.hasEnough(amount)) {
            return Result.failure("Insufficient funds", ErrorCode.INSUFFICIENT_FUNDS);
        }
        this.amount = this.amount.subtract(amount);
        return Result.success(null);
    }

    public Result<Void> add(BigDecimal amount) {
        if(!this.currency.isValidAmount(amount)){
            return Result.failure("Invalid amount for currency", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }
        this.amount = this.amount.add(amount);
        return Result.success(null);
    }

    @Override
    public String toString() {
        return "Money{" +
                "currency=" + currency +
                ", amount=" + amount.toString() +
                '}';
    }
}
