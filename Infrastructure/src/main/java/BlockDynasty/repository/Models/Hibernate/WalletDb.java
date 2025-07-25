package BlockDynasty.repository.Models.Hibernate;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallet")
public class WalletDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<AccountDb> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BalanceDb> balances = new ArrayList<>();

    public WalletDb() {
    }

    // Helper method for managing the relationship
    public void addBalance(BalanceDb balance) {
        balances.add(balance);
        balance.setWallet(this);
    }

    public void removeBalance(BalanceDb balance) {
        balances.remove(balance);
        balance.setWallet(null);
    }

    public void setAccount(AccountDb account) {
        accounts.add(account);
        account.setWallet(this);
    }

    public void removeAccount(AccountDb account) {
        accounts.remove(account);
        account.setWallet(null);
    }

    public List<AccountDb> getAccounts() {
        return accounts;
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