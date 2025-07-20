package me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate;

import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.wallet.Wallet;

public class AccountMapper {
    public static AccountDb toEntity(Account domain) {
        if (domain == null) {
            return null;
        }
        AccountDb accountDb = new AccountDb();
        accountDb.setUuid(domain.getUuid().toString());
        accountDb.setNickname(domain.getNickname());
        accountDb.setCanReceiveCurrency(domain.canReceiveCurrency());

        // Create wallet or find existing one (this would need repository access)
        WalletDb walletDb = WalletMapper.toEntity(domain.getWallet());
        accountDb.setWallet(walletDb);

        return accountDb;
    }

    public static Account toDomain(AccountDb entity) {
        if (entity == null) {
            return null;
        }
        Wallet wallet = WalletMapper.toDomain(entity.getWallet());
        return new Account(entity.getUuid(), entity.getNickname(), wallet, entity.isCanReceiveCurrency());
    }
}