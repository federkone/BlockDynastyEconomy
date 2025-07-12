package me.BlockDynasty.Economy.Infrastructure.repository.Models.MongoDb;
import  dev.morphia.annotations.*;
import me.BlockDynasty.Economy.domain.entities.account.Account;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Entity("accounts") // Nombre de la colección
public class AccountMongoDb {
    @Id
    private UUID uuid; // El campo _id en MongoDB.

    @Property("nickname") // Mapea el campo nickname en la base de datos.
    private String nickname;

    @Reference // Relación uno a muchos con balances.
    private List<BalanceMongoDb> balances;

    @Property("can_receive_currency") // Mapea el campo booleano.
    private boolean canReceiveCurrency = true;


    // Constructor para convertir desde la entidad
    public AccountMongoDb(Account account) {
        this.uuid = account.getUuid();
        this.nickname = account.getNickname();
        this.balances = account.getBalances().stream()
                .map(balance -> new BalanceMongoDb(balance))
                .collect(Collectors.toList());
        this.canReceiveCurrency = account.canReceiveCurrency();
    }

    public Account toEntity() {
        Account account = new Account();
        account.setUuid(this.uuid);
        account.setNickname(this.nickname);
        account.setBalances(this.balances.stream()
                .map(BalanceMongoDb::toEntity)
                .collect(Collectors.toList()));
        account.setCanReceiveCurrency(this.canReceiveCurrency);
        return account;
    }

    // Getters y setters

}
