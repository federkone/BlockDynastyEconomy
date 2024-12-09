/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.BlockDynasty.Economy.aplication.vault;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.domain.account.Account;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyAmountNotValidException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.utils.UtilServer;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//TODO: REFACTORIZAR LOS CASOS DE USO EN VAULT, TRABAJAR SIEMPRE CON LA MONEDA POR DEFECTO DEL PROYECTO AQUI, NECESARIO PARA VAULT
public class GEVaultHook extends AbstractEconomy {
    private final GetAccountsUseCase getAccountsUseCase;
    private final CreateAccountUseCase createAccountUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final BlockDynastyEconomy plugin;

    public GEVaultHook(BlockDynastyEconomy plugin,CreateAccountUseCase createAccountUseCase, GetAccountsUseCase getAccountsUseCase, GetCurrencyUseCase getCurrencyUseCase,DepositUseCase depositUseCase, WithdrawUseCase withdrawUseCase) {
        this.getAccountsUseCase = getAccountsUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.createAccountUseCase = createAccountUseCase;
        this.plugin = plugin;
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
        Currency currency = getCurrencyUseCase.getDefaultCurrency();
        if(currency == null) return String.valueOf(amount);
        return currency.format(BigDecimal.valueOf(amount));
    }

    @Override
    public String currencyNamePlural() {
        Currency currency =getCurrencyUseCase.getDefaultCurrency();
        if(currency == null) return "";
        if(currency.getPlural() != null) {
            return currency.getPlural();
        }else{
            return "";
        }
    }

    @Override
    public String currencyNameSingular() {
        Currency currency = getCurrencyUseCase.getDefaultCurrency();
        if(currency == null) return "";
        if(currency.getSingular() != null) {
            return currency.getSingular();
        }else{
            return "";
        }
    }

    @Override
    public boolean has(String playerName, double amount) {
        Account user = getAccountsUseCase.getAccount(playerName);
        if(user != null){
            return user.hasEnough(getCurrencyUseCase.getDefaultCurrency(), BigDecimal.valueOf(amount));
        }
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return has(player.getName(), amount);
    }

    @Override
    public boolean hasAccount(String playerName) {
        return getAccountsUseCase.getAccount(playerName) != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return getAccountsUseCase.getAccount(player.getUniqueId()) != null;
    }

    @Override
    public double getBalance(String playerName) {
        if(plugin.isDebug())UtilServer.consoleLog("Lookup name: " + playerName);
        Account user = getAccountsUseCase.getAccount(playerName);
        Currency currency = getCurrencyUseCase.getDefaultCurrency();
        return user.getBalance(currency).doubleValue();
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        if(plugin.isDebug())UtilServer.consoleLog("Lookup name: " + player.getName() + "(" + player.getUniqueId() + ")");
        Account user = getAccountsUseCase.getAccount(player.getUniqueId());
        Currency currency = getCurrencyUseCase.getDefaultCurrency();
        return user.getBalance(currency).doubleValue();
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if(plugin.isDebug())UtilServer.consoleLog("Lookup name: " + player.getName() + "(" + player.getUniqueId() + ")");


        try{
            withdrawUseCase.execute(player.getUniqueId(), getCurrencyUseCase.getDefaultCurrency().getSingular(),BigDecimal.valueOf(amount));
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        }catch (AccountNotFoundException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Account not found");
        }catch (CurrencyNotFoundException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Currency not found");
        }catch (CurrencyAmountNotValidException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Amount not valid");
        }catch(DecimalNotSupportedException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Decimal not supported");
        }catch (TransactionException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error");
        }catch (InsufficientFundsException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Insufficient funds from "+player.getName());
        } catch (Exception e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Unknown error");
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if(plugin.isDebug())UtilServer.consoleLog("Lookup name: " + player.getName() + "(" + player.getUniqueId()+ ")");

        try {
            depositUseCase.execute(player.getUniqueId(), getCurrencyUseCase.getDefaultCurrency().getSingular(), BigDecimal.valueOf(amount));
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Deposit success for "+player.getName());
        }catch (AccountNotFoundException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Account not found");
        }catch (CurrencyNotFoundException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Currency not found");
        }catch (CurrencyAmountNotValidException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Amount not valid");
        }catch (DecimalNotSupportedException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Decimal not supported for default currency");
        }catch (TransactionException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error");
        } catch (Exception e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Unknown error");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String player, double amount) {
        if(plugin.isDebug())UtilServer.consoleLog("Lookup name: " + player);

        try{
            withdrawUseCase.execute(player, getCurrencyUseCase.getDefaultCurrency().getSingular(), BigDecimal.valueOf(amount));
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        }catch (AccountNotFoundException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Account not found");
        }catch (CurrencyNotFoundException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Currency not found");
        }catch (CurrencyAmountNotValidException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Amount not valid");
        }catch(DecimalNotSupportedException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Decimal not supported");
        }catch (TransactionException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error");
        }catch (InsufficientFundsException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Insufficient funds from "+player);
        } catch (Exception e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Unknown error");
        }
    }

    @Override
    public EconomyResponse depositPlayer(String player, double amount) {
        if(plugin.isDebug())UtilServer.consoleLog("Lookup name: " + player);
        try {
            depositUseCase.execute(player, getCurrencyUseCase.getDefaultCurrency().getSingular(),BigDecimal.valueOf(amount));
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Deposit success for "+player);
        }catch (AccountNotFoundException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Account not found");
        }catch (CurrencyNotFoundException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Currency not found");
        }catch (CurrencyAmountNotValidException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Amount not valid");
        }catch (DecimalNotSupportedException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Decimal not supported for default currency");
        }catch (TransactionException e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Transaction error");
        } catch (Exception e){
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Unknown error");
        }
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

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "BlockDynastyEconomy does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

}
