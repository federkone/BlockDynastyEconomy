package me.BlockDynasty.Economy.domain.balance;

import jakarta.persistence.*;
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

    public void setBalance(BigDecimal amount){
        if(amount.doubleValue() < 0){
            throw new CurrencyAmountNotValidException("Amount must be greater than -1");
        }
        this.amount = amount;
    }

    public void deposit(BigDecimal amount){
        if(amount.doubleValue() <= 0){
            throw new CurrencyAmountNotValidException("Amount must be greater than 0");
        }
        if(!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            throw new CurrencyAmountNotValidException("Amount must be a whole number");
        }

        this.amount = this.amount.add(amount);
    }

    public void withdraw(BigDecimal amount){
        if(amount.doubleValue() <= 0){
            throw new CurrencyAmountNotValidException("Amount must be greater than 0");
        }
        if(!currency.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0){
            throw new CurrencyAmountNotValidException("Amount must be a whole number");
        }

        if(this.amount.doubleValue() < amount.doubleValue()){

            throw new InsufficientFundsException("Insufficient balance for currency" );
        }

        this.amount = this.amount.subtract(amount);
    }
}
