package me.BlockDynasty.Economy.Infrastructure.repositoryV2.Models.Hibernate;

import jakarta.persistence.*;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.BlockDynasty.Economy.domain.entities.wallet.Wallet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "wallet")
public class WalletDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private AccountDb account;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BalanceDb> balances;

    public WalletDb() {
    }


   public void setAccount(AccountDb account){
        this.account = account;
    }

    public AccountDb getAccount() {
        return this.account;
    }

    public Long getId() {
        return this.id;
    }

    public List<BalanceDb> getBalances() {
        return this.balances;
    }

    public void setBalances(List<BalanceDb> balances) {
        this.balances = balances;
   }
}