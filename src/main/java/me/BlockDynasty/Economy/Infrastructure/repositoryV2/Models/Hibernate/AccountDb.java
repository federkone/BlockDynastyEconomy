package me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.BalanceDb;
import me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate.WalletDb;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "account")
public class AccountDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "nickname")
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private WalletDb wallet;

    @Column(name = "can_receive_currency")
    private boolean canReceiveCurrency;

    public AccountDb() {
    }

    public void updateFromEntity(Account account) {
        // No need to update balances directly here since wallet is now separate
        this.setNickname(account.getNickname());
        this.setCanReceiveCurrency(account.canReceiveCurrency());
        // UUID shouldn't change
    }

    public void setWallet(WalletDb wallet) {
        this.wallet = wallet;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public WalletDb getWallet() {
        return this.wallet;
    }

    public boolean isCanReceiveCurrency() {
        return this.canReceiveCurrency;
    }

    public void setCanReceiveCurrency(boolean canReceiveCurrency) {
        this.canReceiveCurrency = canReceiveCurrency;
    }
}