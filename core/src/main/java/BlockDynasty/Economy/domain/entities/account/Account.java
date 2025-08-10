package BlockDynasty.Economy.domain.entities.account;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.wallet.Wallet;
import BlockDynasty.Economy.domain.result.ErrorCode;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.entities.currency.Currency;

import java.math.BigDecimal;
import java.util.*;

public class Account implements IAccount {
    private final Player player;
    private Wallet wallet;
    private boolean canReceiveCurrency;
    private boolean blocked;

    public Account(UUID uuid, String nickname) {
        this.player = new Player(uuid.toString(), nickname);
        this.wallet = new Wallet();
        this.blocked = false;
        this.canReceiveCurrency = true;
    }

    public Account(UUID uuid, String nickname, List<Money> moneyList, boolean canReceiveCurrency,boolean blocked) {
        this.player = new Player(uuid.toString(), nickname);
        this.wallet = new Wallet(moneyList);
        this.canReceiveCurrency = canReceiveCurrency;
        this.blocked = blocked;
    }

    public Account(String uuid, String nickname, Wallet wallet, boolean canReceiveCurrency, boolean blocked) {
        this.player = new Player(uuid, nickname);
        this.wallet = wallet;
        this.canReceiveCurrency = canReceiveCurrency;
        this.blocked = blocked;
    }

    public Account(Account account) {
        this.player = new Player(account.getPlayer());
        this.wallet = new Wallet(account.getWallet());
        this.canReceiveCurrency = account.canReceiveCurrency();
        this.blocked = account.isBlocked();
    }

    public Result<Void> subtract(Currency currency, BigDecimal amount){
        Money money = getMoney(currency);
        if (money == null) {
            return Result.failure("No balance found for currency" , ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        }
        Result<Void> result = money.subtract(amount);
        if (!result.isSuccess()) {
            return result;
        }
        return Result.success(null);
    }

    public Result<Void> add(Currency currency, BigDecimal amount) {
        Money money = getMoney(currency);
        if (money == null) {
            return Result.failure("No balance found for currency" , ErrorCode.ACCOUNT_NOT_HAVE_BALANCE);
        } else {
            Result<Void> result = money.add(amount);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return Result.success(null);
    }

    public Result<Void> setMoney(Currency currency, BigDecimal amount) {
        Money money = getMoney(currency);
        if (money == null) {
            createBalance(currency, amount);
        }else{
            Result<Void> result = money.setAmount(amount);
            if (!result.isSuccess()) {
                return result;
            }
        }
        return Result.success(null);
    }

    public void setBalances(List<Money> monies) {
        this.wallet.setBalances(monies);
    }
    public List<Money> getBalances() {
        return wallet.getBalances();
    }

    public boolean hasCurrency( String currencyName){
        return wallet.hasCurrency(currencyName);
    }

    public Money getMoney(Currency currency) {
        return wallet.getMoney(currency);
    }

    public Money getMoney(){
        return wallet.getMoney();
    }

    public Money getMoney(String currencyName){
        return wallet.getMoney(currencyName);
    }

    public boolean hasEnough(Currency currency, BigDecimal amount){
        Money money = getMoney(currency);
        if (money == null) {
            return false;
        }
        return money.hasEnough(amount);
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public boolean hasEnoughDefaultCurrency(BigDecimal amount){
        Money money = getMoney();
        if (money == null) {
            return false;
        }
        return money.hasEnough(amount);
    };

    private void createBalance(Currency currency, BigDecimal amount) {
        wallet.createBalance(currency, amount);
    }

    public void setUuid(UUID uuid) {
        this.player.setUuid(uuid.toString());
    }
    public void setNickname(String nickname) {
        this.player.setNickname(nickname);
    }

    public String getNickname() {
        return player.getNickname();
    }
    public UUID getUuid() {
        return UUID.fromString(player.getUuid());
    }

    public void setCanReceiveCurrency(boolean canReceiveCurrency) {
        this.canReceiveCurrency = canReceiveCurrency;
    }
    public boolean canReceiveCurrency() {
        return canReceiveCurrency;
    }
    public boolean isBlocked() {
        return blocked;
    }
    public void block(){
        this.blocked = true;
    }
    public void unblock(){
        this.blocked = false;
    }
    public Player getPlayer() {
        return player;
    }
    @Override
    public String toString() {
        return "Account{" +
                "player=" + player +
                ", wallet=" + wallet +
                ", canReceiveCurrency=" + canReceiveCurrency +
                '}';
    }
}