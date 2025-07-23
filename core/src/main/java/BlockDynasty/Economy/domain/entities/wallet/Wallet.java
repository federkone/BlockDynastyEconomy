package BlockDynasty.Economy.domain.entities.wallet;

import BlockDynasty.Economy.domain.entities.balance.Balance;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Wallet implements IWallet {
    private List<Balance> balances;

    public Wallet() {
        this.balances = new ArrayList<>();
    }

    public Wallet(List<Balance> balances) {
        this.balances = balances;
    }

    public boolean hasCurrency( String currencyName){
        return balances.stream().anyMatch(b ->
                b.getCurrency().getSingular().equals(currencyName) || b.getCurrency().getPlural().equals(currencyName));
    }

    public Balance getBalance(Currency currency) {
        return balances.stream()
                .filter(b -> b.getCurrency().equals(currency))
                .findFirst()
                .orElse(null);
    }

    public Balance getBalance(){
        return balances.stream()
                .filter(b -> b.getCurrency().isDefaultCurrency())
                .findFirst()
                .orElse(null);
    }

    public Balance getBalance(String currencyName){
        return balances.stream()
                .filter(b -> b.getCurrency().getSingular().equalsIgnoreCase(currencyName) || b.getCurrency().getPlural().equalsIgnoreCase(currencyName))
                .findFirst()
                .orElse(null);
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public void createBalance(Currency currency, BigDecimal amount) {
        Balance balance = new Balance(currency, amount);
        balances.add(balance);
    }

    public List<Balance> getBalances() {
        return balances;
    }
}
