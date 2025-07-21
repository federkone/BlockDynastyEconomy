package me.BlockDynasty.Economy.Infrastructure.repository.Mappers;

import me.BlockDynasty.Economy.Infrastructure.repository.Models.Hibernate.BalanceDb;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;

public class BalanceMapper {
    public static BalanceDb toEntity(Balance domain) {
        if (domain == null) return null;

        BalanceDb entity = new BalanceDb();
        entity.setAmount(domain.getAmount());
        // El ID y relaciones se establecerán en el WalletMapper
        return entity;
    }


    public static Balance toDomain(BalanceDb entity) {
        if (entity == null) {
            return null;
        }
        return new Balance(CurrencyMapper.toDomain(entity.getCurrency()), entity.getAmount() );
    }
}