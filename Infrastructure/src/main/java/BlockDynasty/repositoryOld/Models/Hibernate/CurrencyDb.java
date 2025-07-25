package BlockDynasty.repositoryOld.Models.Hibernate;

import jakarta.persistence.*;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "currencies")
public class CurrencyDb {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;
    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(60)")
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

    public CurrencyDb() {
    }

    public CurrencyDb(Currency currency) {
        this.uuid = currency.getUuid().toString();
        this.singular = currency.getSingular();
        this.plural = currency.getPlural();
        this.symbol = currency.getSymbol();
        this.color = currency.getColor();
        this.decimalSupported = currency.isDecimalSupported();
        this.payable = currency.isPayable();
        this.defaultCurrency = currency.isDefaultCurrency();
        this.defaultBalance = currency.getDefaultBalance();
        this.exchangeRate = currency.getExchangeRate();
    }

    public Currency toEntity(){
        return new Currency(UUID.fromString(uuid), singular, plural, symbol, color, decimalSupported, payable, defaultCurrency, defaultBalance, exchangeRate);
    }

    public void updateFromEntity(Currency currency) {
        this.singular = currency.getSingular();
        this.plural = currency.getPlural();
        this.symbol = currency.getSymbol();
        this.color = currency.getColor();
        this.decimalSupported = currency.isDecimalSupported();
        this.payable = currency.isPayable();
        this.defaultCurrency = currency.isDefaultCurrency();
        this.defaultBalance = currency.getDefaultBalance();
        this.exchangeRate = currency.getExchangeRate();
    }

    public String getUuid() {
        return uuid;
    }

}