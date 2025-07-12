package me.BlockDynasty.Economy.Infrastructure.repository.Models.MongoDb;
import  dev.morphia.annotations.*;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;

import java.math.BigDecimal;

@Entity("account_balances") // Nombre de la colección en MongoDB.
public class BalanceMongoDb {

    @Id
    private int id; // Campo que será tratado como el _id en MongoDB.

    @Reference // Relación muchos a uno con Currency.
    private CurrencyMongoDb currency;

    @Property("amount") // Mapeo del campo con precisión decimal.
    private BigDecimal amount;

    // Constructor para convertir desde la entidad
    public BalanceMongoDb(Balance balance) {
        this.currency = new CurrencyMongoDb(balance.getCurrency());
        this.amount = balance.getBalance();
    }

    public Balance toEntity() {
        Balance balance = new Balance();
        balance.setCurrency(this.currency.toEntity());
        balance.setBalance(this.amount);
        return balance;
    }
    // Getters y Setters

}