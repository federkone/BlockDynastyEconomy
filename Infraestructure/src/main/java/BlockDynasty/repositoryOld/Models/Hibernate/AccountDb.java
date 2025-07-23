package BlockDynasty.repositoryOld.Models.Hibernate;

import jakarta.persistence.*;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.entities.balance.Balance;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "accounts")
public class AccountDb {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;
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
        for (Balance balance : account.getBalances()){
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
        Map<String, Balance> balanceMap = account.getBalances().stream()
                .collect(Collectors.toMap(b -> b.getCurrency().getUuid().toString(), b -> b));

        for (BalanceDb balanceDb : this.balances) {
            Balance updatedBalance = balanceMap.get(balanceDb.getCurrency().getUuid());
            if (updatedBalance != null) {
                balanceDb.setAmount(updatedBalance.getAmount());
                balanceMap.remove(balanceDb.getCurrency().getUuid());
            }
        }

        for (Balance newBalance : balanceMap.values()) {
            this.balances.add(new BalanceDb(newBalance));
        }
    }

}
/*@Entity
@Table(name = "accounts")
public class AccountDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", columnDefinition = "VARCHAR(60)", unique = true)
    private String uuid;

    @Column(name = "nickname")
    private String nickname;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<WalletDb> wallet;

    @Column(name = "can_receive_currency")
    private boolean canReceiveCurrency;

    public AccountDb() {
    }

    public AccountDb(Account account) {
        this.uuid = account.getUuid().toString();
        this.nickname = account.getNickname();
        this.wallet = new ArrayList<>();
        for (Balance balance : account.getWallet()) {
            this.wallet.add(new WalletDb(this, balance));
        }
        this.canReceiveCurrency = account.canReceiveCurrency();
    }

    public Account toEntity() {
        return new Account(UUID.fromString(uuid), nickname, walletToBalances(), canReceiveCurrency);
    }

    private List<Balance> walletToBalances() {
        return this.wallet.stream()
                .map(WalletDb::toEntity)
                .collect(Collectors.toList());
    }

    public void updateFromEntity(Account account) {
        Map<String, Balance> balanceMap = account.getWallet().stream()
                .collect(Collectors.toMap(b -> b.getCurrency().getUuid().toString(), b -> b));

        for (WalletDb walletItem : this.wallet) {
            Balance updatedBalance = balanceMap.get(walletItem.getCurrency().getUuid());
            if (updatedBalance != null) {
                walletItem.setAmount(updatedBalance.getAmount());
                balanceMap.remove(walletItem.getCurrency().getUuid());
            }
        }

        for (Balance newBalance : balanceMap.values()) {
            this.wallet.add(new WalletDb(this, newBalance));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}*/