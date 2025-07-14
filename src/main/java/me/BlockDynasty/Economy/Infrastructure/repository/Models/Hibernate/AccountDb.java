package me.BlockDynasty.Economy.Infrastructure.repository.Models.Hibernate;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;

import java.util.*;
import java.util.stream.Collectors;

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
    private boolean canReceiveCurrency;

    public AccountDb() {
    }

    public AccountDb(Account account){
        this.uuid = account.getUuid().toString();
        this.nickname = account.getNickname();
        this.balances = new ArrayList<>();
        for (Balance balance : account.getWallet()){
            this.balances.add(new BalanceDb(balance));
        }
        this.canReceiveCurrency = account.canReceiveCurrency();
    }

    public Account toEntity(){
        return new Account(UUID.fromString(uuid), nickname, balancesToEntity(), canReceiveCurrency);
    }

    private List<Balance> balancesToEntity() {
        return  this.balances.stream()
                .map(BalanceDb::toEntity)
                .collect(Collectors.toList());
    }

    public void updateFromEntity(Account account) {
        Map<String, Balance> balanceMap = account.getWallet().stream()
                .collect(Collectors.toMap(b -> b.getCurrency().getUuid().toString(), b -> b));

        for (BalanceDb balanceDb : this.balances) {
            Balance updatedBalance = balanceMap.get(balanceDb.getCurrency().getUuid());
            if (updatedBalance != null) {
                balanceDb.setAmount(updatedBalance.getBalance());
                balanceMap.remove(balanceDb.getCurrency().getUuid());
            }
        }

        for (Balance newBalance : balanceMap.values()) {
            this.balances.add(new BalanceDb(newBalance));
        }
    }

}