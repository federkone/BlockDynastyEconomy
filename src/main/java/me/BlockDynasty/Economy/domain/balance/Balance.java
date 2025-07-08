package me.BlockDynasty.Economy.domain.balance;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.result.ErrorCode;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.domain.currency.Currency;

import java.math.BigDecimal;

@Entity
@Table(name = "account_balances")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "amount", precision = 18, scale = 2)
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

    public boolean hasEnough(BigDecimal amount){
        return this.amount.compareTo(amount) >= 0;
    };

    public Result<Void> setBalance(BigDecimal amount){
        if(amount.doubleValue() < 0){
            return Result.failure("Amount must be greater than -1", ErrorCode.INVALID_AMOUNT);
        }
        this.amount = amount;
        return Result.success(null);
    }

}
