package repository.Mappers;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.wallet.Wallet;
import repository.Models.AccountDb;
import repository.Models.WalletDb;

public class AccountMapper {
    public static AccountDb toEntity(Account domain) {
        if (domain == null) {
            return null;
        }
        AccountDb accountDb = new AccountDb();
        accountDb.setUuid(domain.getUuid().toString());
        accountDb.setNickname(domain.getNickname());
        accountDb.setCanReceiveCurrency(domain.canReceiveCurrency());
        accountDb.setBlock(domain.isBlocked());

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
        return new Account(entity.getUuid(), entity.getNickname(), wallet, entity.isCanReceiveCurrency(),entity.isBlocked());
    }
}