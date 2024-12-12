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
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "currencies")
//@Converter(autoApply = true)
public class Currency {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private int id;

    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(60)")
    //@Convert(converter = UUIDConverter.class)
    private String uuid;

    @Column(name = "name_singular")
    private String singular;

    @Column(name = "name_plural")
    private String plural;

    @Column(name = "symbol")
    private String symbol ;

    @Column(name = "color")
    @Convert(converter = ChatColorConverter.class)
    private ChatColor color ;

    @Column(name = "decimal_supported")
    private boolean decimalSupported ;

    @Column(name = "payable")
    private boolean payable ; //todo: cambiar nombre a transferible, ya que payable es mas una forma de transferir, por lo tanto necesitariamos bloquear la moneda para todo tipo de transacciones //transferable
                                             //todo una vez hecho el cambio, en account los metodo trade y transfer, tengo que validar el caso de payable, y eliminar la pregunta en el caso de uso payUsecase
    @Column(name = "default_currency")
    private boolean defaultCurrency ;

    @Column(name = "default_balance")
    private BigDecimal defaultBalance ;

    @Column(name = "exchange_rate")
    private double exchangeRate ;

    public Currency(UUID uuid, String singular, String plural) {
        this.defaultBalance = BigDecimal.ZERO;
        this.exchangeRate = 0.0;
        this.color = ChatColor.WHITE;
        this.decimalSupported = true;
        this.payable = true;
        this.defaultCurrency = false;
        this.uuid = uuid.toString();
        this.singular = singular;
        this.plural = plural;
    }

    public Currency() {
        this.defaultBalance = BigDecimal.ZERO;
        this.exchangeRate = 0.0;
        this.color = ChatColor.WHITE;
        this.decimalSupported = true;
        this.payable = true;
        this.defaultCurrency = false;
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
        return UUID.fromString(this.uuid);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return uuid.equals(currency.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}

