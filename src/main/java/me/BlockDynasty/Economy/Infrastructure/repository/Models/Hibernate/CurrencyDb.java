package me.BlockDynasty.Economy.Infrastructure.repository.Models.Hibernate;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "currency")
public class CurrencyDb {
    @Id
    @Column(name = "uuid", unique = true) // Asegurar unicidad
    private String uuid;

    @Column(name = "name_singular")
    private String singular;

    @Column(name = "name_plural")
    private String plural;

    @Column(name = "symbol")
    private String symbol ;

    @Column(name = "color")
    private String color ;

    @Column(name = "decimal_supported")
    private boolean decimalSupported ;

    @Column(name = "payable")
    private boolean payable ;

    @Column(name = "default_currency")
    private boolean defaultCurrency ;

    @Column(name = "default_balance")
    private BigDecimal defaultBalance ;

    @Column(name = "exchange_rate")
    private double exchangeRate ;

    //esto se agrega para que hibernate elimine en cascada los balances asociados a esta moneda
    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BalanceDb> balances = new ArrayList<>();

    public CurrencyDb() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDecimalSupported() {
        return decimalSupported;
    }

    public void setDecimalSupported(boolean decimalSupported) {
        this.decimalSupported = decimalSupported;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public boolean isDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public BigDecimal getDefaultBalance() {
        return defaultBalance;
    }

    public void setDefaultBalance(BigDecimal defaultBalance) {
        this.defaultBalance = defaultBalance;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    // Actualizar campos (excepto ID/UUID)
    public void update(Currency currency) {
        setSingular(currency.getSingular());
        setPlural(currency.getPlural());
        setColor(currency.getColor());
        setDecimalSupported(currency.isDecimalSupported());
        setPayable(currency.isPayable());
        setDefaultCurrency(currency.isDefaultCurrency());
        setDefaultBalance(currency.getDefaultBalance());
        setExchangeRate(currency.getExchangeRate());
         setSymbol(currency.getSymbol());
    }
}