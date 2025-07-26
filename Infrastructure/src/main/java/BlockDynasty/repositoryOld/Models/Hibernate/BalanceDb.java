package BlockDynasty.repositoryOld.Models.Hibernate;

import BlockDynasty.Economy.domain.entities.balance.Money;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "account_balances")
public class BalanceDb  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private CurrencyDb currency;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    public BalanceDb() {
    }
    public BalanceDb(Money money){
        this.currency = new CurrencyDb(money.getCurrency());
        this.amount = money.getAmount();
    }


    public Money toEntity(){
        return new Money(currency.toEntity(), amount);
    }

    public CurrencyDb getCurrency() {
        return this.currency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}