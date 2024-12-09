/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.BlockDynasty.Economy.domain.currency;


import jakarta.persistence.*;
import me.BlockDynasty.Economy.utils.ChatColorConverter;
import me.BlockDynasty.Economy.utils.UUIDConverter;
import me.BlockDynasty.Economy.utils.UtilString;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.UUID;

@Entity
@Table(name = "currencies")
//@Converter(autoApply = true)
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "uuid", columnDefinition = "VARCHAR(60)")
    @Convert(converter = UUIDConverter.class)
    private UUID uuid;

    @Column(name = "name_singular")
    private String singular;

    @Column(name = "name_plural")
    private String plural;

    @Column(name = "symbol")
    private String symbol = "";

    @Column(name = "color")
    @Convert(converter = ChatColorConverter.class)
    private ChatColor color = ChatColor.WHITE;

    @Column(name = "decimal_supported")
    private boolean decimalSupported = true;

    @Column(name = "payable")
    private boolean payable = true;

    @Column(name = "default_currency")
    private boolean defaultCurrency = false;

    @Column(name = "default_balance")
    private BigDecimal defaultBalance = BigDecimal.ZERO;

    @Column(name = "exchange_rate")
    private double exchangeRate = 0.0;

    public Currency(UUID uuid, String singular, String plural) {
        this.uuid = uuid;
        this.singular = singular;
        this.plural = plural;
    }

    public Currency() {

    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public void setDefaultBalance(BigDecimal defaultBalance) {

        this.defaultBalance = defaultBalance;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getSingular() {
        return this.singular;
    }

    public String getPlural() {
        return this.plural;
    }

    public BigDecimal getDefaultBalance() {

        return this.defaultBalance;
    }

    public String format(BigDecimal amount) {
        StringBuilder amt = new StringBuilder();
        if (this.getSymbol() != null) {
            amt.append(this.getSymbol());
        }
        if (this.isDecimalSupported()) {
            amount = amount.setScale(2, RoundingMode.HALF_UP); // Limitar a 2 decimales
            amt.append(NumberFormat.getInstance().format(amount));
        } else {
            String s = amount.setScale(0, RoundingMode.HALF_UP).toPlainString();
            amt.append(NumberFormat.getInstance().format(Double.parseDouble(s)));
        }
        amt.append(" ");
        if (amount.compareTo(BigDecimal.ONE) != 0) {
            amt.append(this.getPlural().replace("_", " "));
        } else {
            amt.append(this.getSingular().replace("_", " "));
        }
        return amt.toString();
    }

    public boolean isDefaultCurrency() {
        return this.defaultCurrency;
    }

    public void setStartBalance(BigDecimal startBalance){
        this.defaultBalance =startBalance;
    }

    public void setDefaultCurrency(boolean defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public boolean isPayable() {
        return this.payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public boolean isDecimalSupported() {
        return this.decimalSupported;
    }

    public void setDecimalSupported(boolean decimalSupported) {
        this.decimalSupported = decimalSupported;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}

