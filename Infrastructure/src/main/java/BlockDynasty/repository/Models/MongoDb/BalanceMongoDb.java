package BlockDynasty.repository.Models.MongoDb;
import BlockDynasty.Economy.domain.entities.balance.Money;
import  dev.morphia.annotations.*;

import java.math.BigDecimal;

@Entity("account_balances") // Nombre de la colección en MongoDB.
public class BalanceMongoDb {

    @Id
    private int id; // Campo que será tratado como el _id en MongoDB.

    @Reference // Relación muchos a uno con Currency.
    private CurrencyMongoDb currency;

    @Property("amount") // Mapeo del campo con precisión decimal.
    private BigDecimal amount;


    public BalanceMongoDb(Money money) {
        this.currency = new CurrencyMongoDb(money.getCurrency());
        this.amount = money.getAmount();
    }

    public Money toEntity() {
        return  new Money(this.currency.toEntity(),this.amount);
    }


}