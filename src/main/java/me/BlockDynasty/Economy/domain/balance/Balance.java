package me.BlockDynasty.Economy.domain.balance;

import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.domain.currency.Currency;

import java.math.BigDecimal;

public class Balance {
    private int id;
    private Currency currency;
    private BigDecimal amount;

    public  Balance(){

    }
    public Balance(Currency currency){
        this.currency= currency;
        this.amount = currency.getDefaultBalance();
    }

    public Balance(Currency currency, BigDecimal amount){
        this.currency= currency;
        this.amount = amount;
    }

    public void setCurrency(Currency currency){
        this.currency = currency;
    }
    public BigDecimal getBalance(){
        return this.amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Result<Void> setBalance(BigDecimal amount){
        if(amount.doubleValue() < 0){
            //throw new CurrencyAmountNotValidException("Amount must be greater than -1");
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }
        this.amount = amount;
        return Result.success(null);
    }

    public Result<Void> deposit(BigDecimal amount){
        if(amount.doubleValue() <= 0){
            //throw new CurrencyAmountNotValidException("Amount must be greater than 0");
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }

        if(!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            //throw new CurrencyAmountNotValidException("Amount must be a whole number");
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        this.amount = this.amount.add(amount);
        return Result.success(null);
    }

    public Result<Void>  withdraw(BigDecimal amount){
        if(amount.doubleValue() <= 0){
            //throw new CurrencyAmountNotValidException("Amount must be greater than 0");
            return Result.failure("Amount must be greater than 0", ErrorCode.INVALID_AMOUNT);
        }
        if(!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            //throw new CurrencyAmountNotValidException("Amount must be a whole number");
            return Result.failure("Decimal not supported", ErrorCode.DECIMAL_NOT_SUPPORTED);
        }

        if(this.amount.doubleValue() < amount.doubleValue()){
            //throw new InsufficientFundsException("Insufficient balance for currency" );
            return Result.failure("Insufficient balance for currency", ErrorCode.INSUFFICIENT_FUNDS);
        }

        this.amount = this.amount.subtract(amount);
        return Result.success(null);
    }
}
