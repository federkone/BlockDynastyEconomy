package me.BlockDynasty.Economy.Infrastructure.repository.Models.Hibernate;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;

import java.util.*;

@Entity
@Table(name = "accounts")
public class AccountDb {
    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(60)")
    private String uuid;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private List<BalanceDb> balances;

    @Column(name = "can_receive_currency")
    private boolean canReceiveCurrency = true;

    public AccountDb() {
    }

    public AccountDb(Account account){
        this.uuid = account.getUuid().toString();
        this.nickname = account.getNickname();
        this.balances = new ArrayList<>();
        for (Balance balance : account.getBalances()){
            this.balances.add(new BalanceDb(balance));
        }
        this.canReceiveCurrency = account.canReceiveCurrency();
    }

    public Account toEntity(){
        List<Balance> balanceList = new ArrayList<>();
        for (BalanceDb balanceMapper : balances){
            balanceList.add(balanceMapper.toEntity());
        }
        return new Account(UUID.fromString(uuid), nickname, balanceList, canReceiveCurrency);
    }

    public void updateFromEntity(Account account) {
        this.uuid = account.getUuid().toString();
        this.nickname = account.getNickname();
        this.canReceiveCurrency = account.canReceiveCurrency();
        this.balances.clear();
        for (Balance balance : account.getBalances()) {
            this.balances.add(new BalanceDb(balance));
        }
    }

}