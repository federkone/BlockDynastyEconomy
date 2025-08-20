package BlockDynasty.repository.Models.MongoDb;
import dev.morphia.annotations.*;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;

@Entity("currencies") // Nombre de la colección en MongoDB
public class CurrencyMongoDb {

    @Id
    private String uuid; // Identificador único para la moneda.

    @Property("name_singular") // Campo singular.
    private String singular;

    @Property("name_plural") // Campo plural.
    private String plural;

    @Property("symbol") // Símbolo de la moneda.
    private String symbol;

    @Transient // Ignorado si no se almacena directamente. O puedes manejarlo manualmente.
    private String color;

    @Property("decimal_supported") // Indicador si soporta decimales.
    private boolean decimalSupported;

    @Property("payable") // Indicador si es pagable.
    private boolean payable;

    @Property("default_currency") // Indicador si es la moneda predeterminada.
    private boolean defaultCurrency;

    @Property("default_balance") // Balance predeterminado.
    private BigDecimal defaultBalance;

    @Property("exchange_rate") // Tipo de cambio.
    private double exchangeRate;

    // Constructor para convertir desde la entidad
    public CurrencyMongoDb(Currency currency) {
        this.uuid = currency.getUuid().toString();
        this.singular = currency.getSingular();
        this.plural = currency.getPlural();
        this.symbol = currency.getSymbol();
        this.color = currency.getColor();
        this.decimalSupported = currency.isDecimalSupported();
        this.payable = currency.isTransferable();
        this.defaultCurrency = currency.isDefaultCurrency();
        this.defaultBalance = currency.getDefaultBalance();
        this.exchangeRate = currency.getExchangeRate();
    }

    public Currency toEntity() {
        Currency currency = new Currency();
        currency.setUuid(java.util.UUID.fromString(this.uuid));
        currency.setSingular(this.singular);
        currency.setPlural(this.plural);
        currency.setSymbol(this.symbol);
        currency.setColor(this.color);
        currency.setDecimalSupported(this.decimalSupported);
        currency.setTransferable(this.payable);
        currency.setDefaultCurrency(this.defaultCurrency);
        currency.setDefaultBalance(this.defaultBalance);
        currency.setExchangeRate(this.exchangeRate);
        return currency;
    }
    // Getters y Setters

}