package me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.wallet.Wallet;

public class AccountMapper {
    public static AccountDb toEntity(Account domain) {
        if (domain == null) {
            return null;
        }
        AccountDb entity = new AccountDb();
        entity.setUuid(domain.getUuid().toString());
        entity.setNickname(domain.getNickname());
        entity.setCanReceiveCurrency(domain.canReceiveCurrency());

        // Convertir Wallet del dominio a WalletDb
        if (domain.getWallet() != null) {
            WalletDb wallet = WalletMapper.toEntity(domain.getWallet());
            wallet.setAccount(entity); // Establece la relación bidireccional
            entity.setWallet(wallet);
        }
        return entity;
    }

    public static Account toDomain(AccountDb entity) {
        if (entity == null) {
            return null;
        }
        Wallet wallet = WalletMapper.toDomain(entity.getWallet());
        return new Account( entity.getUuid(),entity.getNickname() , wallet, entity.isCanReceiveCurrency()
        );
    }
}