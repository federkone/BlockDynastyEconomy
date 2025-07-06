
package me.BlockDynasty.Integrations.vault;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.UsesCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import me.BlockDynasty.Economy.domain.balance.Balance;
import me.BlockDynasty.Economy.domain.currency.Currency;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VaultHook extends AbstractEconomy {
    private final GetAccountsUseCase getAccountsUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final GetBalanceUseCase getBalanceUseCase;

    public VaultHook(UsesCase usesCase) {
        this.getAccountsUseCase = usesCase.getAccountsUseCase();
        this.getCurrencyUseCase = usesCase.getCurrencyUseCase();
        this.depositUseCase = usesCase.getDepositUseCase();
        this.withdrawUseCase = usesCase.getWithdrawUseCase();
        this.createAccountUseCase = usesCase.getCreateAccountUseCase();
        this.getBalanceUseCase = usesCase.getGetBalanceUseCase();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "BlockDynastyEconomy";
    }

    @Override
    public String format(double amount) {
        Result<Currency> currencyResult = getCurrencyUseCase.getDefaultCurrency();
        if(currencyResult.isSuccess()) return currencyResult.getValue().format(BigDecimal.valueOf(amount));
        return String.valueOf(amount);
    }

    @Override
    public String currencyNamePlural() {
        Result<Currency> currencyResult = getCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getPlural();
        }
        return "";
    }

    @Override
    public String currencyNameSingular() {
        Result<Currency> currencyResult = getCurrencyUseCase.getDefaultCurrency();
        if (currencyResult.isSuccess()) {
            return currencyResult.getValue().getSingular();
        }
        return "";
    }

    @Override
    public boolean has(String playerName, double amount) {  //El jugador X tiene X cantidad de "dinero default"
        Result<Account> accountResult = getAccountsUseCase.getAccount(playerName);
        if(accountResult.isSuccess() ){
            return accountResult.getValue().hasEnough(BigDecimal.valueOf(amount));
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public boolean hasAccount(String playerName) {
        return getAccountsUseCase.getAccount(playerName).isSuccess();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return getAccountsUseCase.getAccount(player.getUniqueId()).isSuccess();
    }

    //caso de uso obtener balance
    @Override
    public double getBalance(String playerName) {
        //UtilServer.consoleLog("Lookup name: " + playerName);
        Result<Balance> result = getBalanceUseCase.getBalance(playerName);
        if(result.isSuccess() ){
            return result.getValue().getBalance().doubleValue();
        }
        return 0;
    }

    //caso de uso obtener balance
    @Override
    public double getBalance(OfflinePlayer player) {
        Result<Balance> result = getBalanceUseCase.getBalance(player.getName());
        if(result.isSuccess() ){
            return result.getValue().getBalance().doubleValue();
        }
        return 0;
    }

    //solo caso de uso withdraw
    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        Result<Void> resultWithdraw = withdrawUseCase.execute(player.getUniqueId(), BigDecimal.valueOf(amount));
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+player.getName());
        }
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    //solo caso de uso deposit
    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        Result<Void> resultDeposit = depositUseCase.execute(player.getUniqueId(), BigDecimal.valueOf(amount));
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Deposit success for "+player.getName());
        }
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, double amount) {
        Result<Void> resultWithdraw = withdrawUseCase.execute(player, BigDecimal.valueOf(amount));
        if(resultWithdraw.isSuccess()){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "withdraw success for "+player);
        }
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultWithdraw.getErrorMessage());
    }

    @Override
    public EconomyResponse depositPlayer(String player, double amount) {
        Result<Void> resultDeposit = depositUseCase.execute(player,  BigDecimal.valueOf(amount));
        if(resultDeposit.isSuccess()){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Deposit success for "+player);
        }
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error"+resultDeposit.getErrorMessage());
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return false;
        }
        try{
            createAccountUseCase.execute(player.getUniqueId(),playerName);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    //crear cuenta bancaria
    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //borrar cuenta bancaria
    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //tiene x monto en el banco?
    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //extraer del banco
    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //depositar en el banco
    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //es propietario del banco?
    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //es miembro del banco?
    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //balance del banco de x persona
    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    //obtener bancos o cuentas bancarias??????????
    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    //si soporta bancos
    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

}
