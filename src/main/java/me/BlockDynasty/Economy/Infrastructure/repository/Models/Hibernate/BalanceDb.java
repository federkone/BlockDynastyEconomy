package me.BlockDynasty.Economy.Infrastructure.repository.Models.Hibernate;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "balance")
public class BalanceDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private WalletDb wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", referencedColumnName = "uuid") // Referenciar UUID
    private CurrencyDb currency;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    public BalanceDb() {
    }

    public Long getId() {
        return this.id;
    }

    public void setWallet(WalletDb wallet) {
        this.wallet = wallet;
    }
    public WalletDb getWallet() {
        return this.wallet;
    }
    public void setCurrency(CurrencyDb currency) {
        this.currency = currency;
    }
    public CurrencyDb getCurrency() {
        return this.currency;
    }
    public  BigDecimal getAmount() {
        return this.amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}