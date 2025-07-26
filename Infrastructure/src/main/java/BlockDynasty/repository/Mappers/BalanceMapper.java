package BlockDynasty.repository.Mappers;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.repository.Models.Hibernate.BalanceDb;

public class BalanceMapper {
    public static BalanceDb toEntity(Money domain) {
        if (domain == null) return null;

        BalanceDb entity = new BalanceDb();
        entity.setAmount(domain.getAmount());
        // El ID y relaciones se establecerán en el WalletMapper
        return entity;
    }


    public static Money toDomain(BalanceDb entity) {
        if (entity == null) {
            return null;
        }
        return new Money(CurrencyMapper.toDomain(entity.getCurrency()), entity.getAmount() );
    }
}