package me.BlockDynasty.Economy.domain.repository.Models.Hibernate;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.balance.Balance;
import java.util.*;

@Entity
@Table(name = "accounts")
public class AccountMapper {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private int id;

    @Id
    @Column(name = "uuid", columnDefinition = "VARCHAR(60)")
    //@Convert(converter = UUIDConverter.class)
    private String uuid;

    @Column(name = "nickname")
    private String nickname;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private List<BalanceMapper> balances;

    @Column(name = "can_receive_currency")
    private boolean canReceiveCurrency = true;

    public AccountMapper() {
    }

    public AccountMapper(Account account){
        this.uuid = account.getUuid().toString();
        this.nickname = account.getNickname();
        this.balances = new ArrayList<>();
        for (Balance balance : account.getBalances()){
            this.balances.add(new BalanceMapper(balance));
        }
        this.canReceiveCurrency = account.canReceiveCurrency();
    }

    public Account toEntity(){
        List<Balance> balanceList = new ArrayList<>();
        for (BalanceMapper balanceMapper : balances){
            balanceList.add(balanceMapper.toEntity());
        }
        return new Account(UUID.fromString(uuid), nickname, balanceList, canReceiveCurrency);
    }

}