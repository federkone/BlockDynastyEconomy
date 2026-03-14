package com.BlockDynasty.api.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Currency {
    private UUID uuid;
    private String singular;
    private String plural;
    private String symbol;
    private String color;
    private BigDecimal defaultBalance;
    private boolean defaultCurrency ;
    private double exchangeRate;

    public Currency() {}

    public Currency(UUID uuid, String singular,
                    String plural,String symbol,String color,BigDecimal defaultBalance,
                    double exchangeRate,
                    boolean defaultCurrency) {
        this.uuid = uuid;
        this.singular = singular;
        this.plural = plural;
        this.color = color;
        this.defaultCurrency = defaultCurrency;
        this.symbol = symbol;
        this.defaultBalance = defaultBalance;
        this.exchangeRate = exchangeRate;
    }

    public UUID getUuid() {
        return uuid;
    }
    public String getSingular() {
        return singular;
    }
    public String getPlural() {
        return plural;
    }
    public String getColor() {
        return color;
    }
    public boolean isDefaultCurrency() {
        return defaultCurrency;
    }

    public String getSymbol() {
        return symbol;
    }
    public double getExchangeRate() {
        return exchangeRate;
    }

    public String format(BigDecimal amount) {
        return amount.toString();

    }

    public BigDecimal getDefaultBalance() {
        return defaultBalance;
    }

}
