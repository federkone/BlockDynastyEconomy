package me.BlockDynasty.Economy.Infrastructure.repository.Models.Hibernate;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.balance.Balance;

import java.math.BigDecimal;

@Entity
@Table(name = "account_balances")
public class BalanceDb  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private CurrencyDb currencyMapper;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    public BalanceDb() {
    }
    public BalanceDb(Balance balance){
        this.currencyMapper = new CurrencyDb(balance.getCurrency());
        this.amount = balance.getBalance();
    }

    public Balance toEntity(){
        return new Balance(currencyMapper.toEntity(), amount);
    }

}