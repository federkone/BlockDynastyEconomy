package BlockDynasty.Economy.domain.entities.currency;

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
    private String color ;
    private boolean decimalSupported ;
    private boolean transferable;
    private boolean defaultCurrency ;
    private BigDecimal defaultBalance ;
    private double exchangeRate ;

    public Currency(UUID uuid, String singular, String plural) {
        this.defaultBalance = BigDecimal.ZERO;
        this.exchangeRate = 1;
        this.color = "WHITE";
        this.symbol = "";
        this.decimalSupported = true;
        this.transferable = true;
        this.defaultCurrency = false;
        this.uuid = uuid.toString();
        this.singular = singular;
        this.plural = plural;
    }

    public Currency() {
        this.defaultBalance = BigDecimal.ZERO;
        this.exchangeRate = 1;
        this.color = "WHITE";
        this.symbol = "";
        this.decimalSupported = true;
        this.transferable = true;
        this.defaultCurrency = false;
    }

    public Currency(UUID uuid, String singular, String plural, String symbol, String color, boolean decimalSupported, boolean transferable, boolean defaultCurrency, BigDecimal defaultBalance, double exchangeRate) {
        this.uuid = uuid.toString();
        this.singular = singular;
        this.plural = plural;
        this.symbol = symbol;
        this.color = color;
        this.decimalSupported = decimalSupported;
        this.transferable = transferable;
        this.defaultCurrency = defaultCurrency;
        this.defaultBalance = defaultBalance;
        this.exchangeRate = exchangeRate;
    }

    public Currency(Currency currency) {
        this.uuid = currency.uuid;
        this.singular = currency.singular;
        this.plural = currency.plural;
        this.symbol = currency.symbol;
        this.color = currency.color;
        this.decimalSupported = currency.decimalSupported;
        this.transferable = currency.transferable;
        this.defaultCurrency = currency.defaultCurrency;
        this.defaultBalance = currency.defaultBalance;
        this.exchangeRate = currency.exchangeRate;
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

        if (!this.getSymbol().isEmpty()) {
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

    public boolean isTransferable() {
        return this.transferable;
    }

    public void setTransferable(boolean transferable) {
        this.transferable = transferable;
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
    public String toString() {
        return "Currency{" +
                "uuid='" + uuid + '\'' +
                ", singular='" + singular + '\'' +
                ", plural='" + plural + '\'' +
                ", symbol='" + symbol + '\'' +
                ", color='" + color + '\'' +
                ", decimalSupported=" + decimalSupported +
                ", transferable=" + transferable +
                ", defaultCurrency=" + defaultCurrency +
                ", defaultBalance=" + defaultBalance +
                ", exchangeRate=" + exchangeRate +
                '}';
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

