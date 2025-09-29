package BlockDynasty.Economy.domain.entities.wallet;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface IWallet {
    boolean hasCurrency( String currencyName);
    Money getMoney(Currency currency);
    Money getMoney();
    Money getMoney(String currencyName);
    void setBalances(List<Money> monies);
    void createBalance(Currency currency, BigDecimal amount);
    List<Money> getBalances() ;
}
