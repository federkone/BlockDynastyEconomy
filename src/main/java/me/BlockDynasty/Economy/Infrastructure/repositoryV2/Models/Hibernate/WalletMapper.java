package me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate;

import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.wallet.Wallet;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WalletMapper {
    public static WalletDb toEntity(Wallet domain) {
    if (domain == null) {
        return null;
    }
        WalletDb entity = new WalletDb();
        entity.setBalances(
                domain.getBalances().stream()
                        .map(BalanceMapper::toEntity)
                        .collect(Collectors.toList())
        );
        return entity;
    }


    public static Wallet toDomain(WalletDb entity) {
    if (entity == null) {
        return null;
    }
        List<Balance> balances = entity.getBalances().stream()
                .map(BalanceMapper::toDomain)
                .collect(Collectors.toList());
        return new Wallet(balances);
    }
}