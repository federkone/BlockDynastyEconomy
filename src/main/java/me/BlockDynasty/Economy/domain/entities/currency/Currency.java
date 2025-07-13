package me.BlockDynasty.Economy.domain.entities.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.UUID;

public class Currency implements ICurrency{
    private String uuid;
    private String singular;
    private String plural;
    private String symbol ;
    private String color ;//extraer a interfaz para separar de capa de dominio
    private boolean decimalSupported ;
    private boolean payable ;
    private boolean defaultCurrency ;
    private BigDecimal defaultBalance ;
    private double exchangeRate ;

     //todo: cambiar nombre a transferible, ya que payable es mas una forma de transferir, por lo tanto necesitariamos bloquear la moneda para todo tipo de transacciones //transferable
    //todo una vez hecho el cambio, en account los metodo trade y transfer, tengo que validar el caso de payable, y eliminar la pregunta en el caso de uso payUsecase

    public Currency(UUID uuid, String singular, String plural) {
        this.defaultBalance = BigDecimal.ZERO;
        this.exchangeRate = 1; //1
        this.color = "WHITE";
        this.decimalSupported = true;
        this.payable = true;
        this.defaultCurrency = false;
        this.uuid = uuid.toString();
        this.singular = singular;
        this.plural = plural;
    }

    public Currency() {
        this.defaultBalance = BigDecimal.ZERO;
        this.exchangeRate = 1; //1
        this.color = "WHITE";
        this.decimalSupported = true;
        this.payable = true;
        this.defaultCurrency = false;
    }

    public Currency(UUID uuid, String singular, String plural, String symbol, String color, boolean decimalSupported, boolean payable, boolean defaultCurrency, BigDecimal defaultBalance, double exchangeRate) {
        this.uuid = uuid.toString();
        this.singular = singular;
        this.plural = plural;
        this.symbol = symbol;
        this.color = color;
        this.decimalSupported = decimalSupported;
        this.payable = payable;
        this.defaultCurrency = defaultCurrency;
        this.defaultBalance = defaultBalance;
        this.exchangeRate = exchangeRate;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public void setPlural(String plural) {
        this.plural = plural;
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
        if (this.isDecimalSupported()) {
            amount = amount.setScale(2, RoundingMode.HALF_UP); // Limitar a 2 decimales para mostrar
            amt.append(NumberFormat.getInstance().format(amount));
        } else {
            String s = amount.setScale(0, RoundingMode.HALF_UP).toPlainString();
            amt.append(NumberFormat.getInstance().format(Double.parseDouble(s)));
        }
        amt.append(" ");

        if (this.getSymbol() != null) {
            amt.append(this.getSymbol()); //si tiene simbolo usarlo
        }else { //sino usar nombre
            if (amount.compareTo(BigDecimal.ONE) != 0) {
                amt.append(this.getPlural().replace("_", " ")); //si es mayor a 1 usar nombre plural
            } else {
                amt.append(this.getSingular().replace("_", " ")); //si es igual a 1 usar nombre singular
            }
        }
        return amt.toString();
    }

    public boolean isValidAmount(BigDecimal amount) {
        if (!this.isDecimalSupported() && amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            return false; // Invalid if decimal not supported and has decimal part
        }
        return true;
    }

    public boolean isDefaultCurrency() {
        return this.defaultCurrency;
    }

    public void setStartBalance(BigDecimal startBalance){
        this.defaultBalance =startBalance;
    }

    public void setDefaultBalance(BigDecimal defaultBalance) {
        this.defaultBalance = defaultBalance;
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

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
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

