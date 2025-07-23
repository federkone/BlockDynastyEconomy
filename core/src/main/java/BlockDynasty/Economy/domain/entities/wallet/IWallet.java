package BlockDynasty.Economy.domain.entities.wallet;

import BlockDynasty.Economy.domain.entities.balance.Balance;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface IWallet {
    boolean hasCurrency( String currencyName);
    Balance getBalance(Currency currency);
    Balance getBalance();
    Balance getBalance(String currencyName);
    void setBalances(List<Balance> balances);
    void createBalance(Currency currency, BigDecimal amount);
    List<Balance> getBalances() ;
}
