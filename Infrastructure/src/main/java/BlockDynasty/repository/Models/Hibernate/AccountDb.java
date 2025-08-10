package BlockDynasty.repository.Models.Hibernate;

import jakarta.persistence.*;
import BlockDynasty.Economy.domain.entities.account.Account;
import net.bytebuddy.implementation.bind.annotation.Default;

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

    @Column(name = "block") //default false
    @org.hibernate.annotations.ColumnDefault("false")
    private boolean block;

    public AccountDb() {
    }

    //public void updateFromEntity(Account account) {
      //  // No need to update balances directly here since wallet is now separate
        //this.setNickname(account.getNickname());
        //this.setCanReceiveCurrency(account.canReceiveCurrency());
        //this.block = account.isBlocked();
        // UUID shouldn't change
    //}

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
    public boolean isBlocked() {
        return this.block;
    }
    public void setBlock(boolean block) {
        this.block = block;
    }
    public void setCanReceiveCurrency(boolean canReceiveCurrency) {
        this.canReceiveCurrency = canReceiveCurrency;
    }
}