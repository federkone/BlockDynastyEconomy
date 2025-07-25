package BlockDynasty.repository.Models.MongoDb;
import  dev.morphia.annotations.*;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Balance;

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
    private boolean canReceiveCurrency ;


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
        return new Account(this.uuid,this.nickname,balancesToEntity(), this.canReceiveCurrency);
    }

    private List<Balance> balancesToEntity() {
        return this.balances.stream()
                .map(BalanceMongoDb::toEntity)
                .collect(Collectors.toList());
    }

    // Getters y setters

}
