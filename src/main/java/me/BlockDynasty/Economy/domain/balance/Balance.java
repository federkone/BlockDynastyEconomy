package me.BlockDynasty.Economy.domain.balance;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.aplication.result.ErrorCode;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAmountNotValidException;
import org.checkerframework.checker.units.qual.C;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

//todo: in the future, this class will be used to store the balance of the account

@Entity
@Table(name = "account_balances")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "amount", precision = 18, scale = 2) //todo, for now we will use a fixed scale of 2, but probably can be change this in the future
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
