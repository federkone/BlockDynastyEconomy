package me.BlockDynasty.Placeholder;

import me.BlockDynasty.Economy.BlockDynastyEconomy;
import me.BlockDynasty.Economy.aplication.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.account.Account;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.BlockDynasty.Economy.domain.currency.Currency;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class BlockdynastyEconomyExpansion extends PlaceholderExpansion {
    private final GetAccountsUseCase getAccountsUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;
    private final BlockDynastyEconomy economy;

    public BlockdynastyEconomyExpansion(BlockDynastyEconomy plugin,GetAccountsUseCase getAccountsUseCase, GetCurrencyUseCase getCurrencyUseCase) {
        this.getAccountsUseCase = getAccountsUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;
        this.economy = plugin;
    }
    //private BlockDynastyEconomy economy = null;

    @Override
    public boolean register() {
        if(!canRegister()){
            return false;
        }

        if (economy == null) {
            return false;
        }

        return super.register();
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(this.getRequiredPlugin()) != null;
    }

    @Override
    public String getIdentifier() {
        return "BlockDynastyEconomy";
    }

    @Override
    public String getAuthor() {
        return "Nullplague";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getRequiredPlugin(){
        return "BlockDynastyEconomy";
    }

    @Override
    public String onRequest(OfflinePlayer player, String s) {
        if (player == null) {
            return "";
        }

        Result<Account> accountResult = getAccountsUseCase.getAccount(player.getUniqueId());
        if (!accountResult.isSuccess()) {
            return  "Player data not found";
        }
        Result<Currency> defaultcurrencyResult = getCurrencyUseCase.getDefaultCurrency();
        if (!defaultcurrencyResult.isSuccess()) {
            return  "Default currency not found";
        }

        Account a = accountResult.getValue();
        Currency dc = defaultcurrencyResult.getValue();

        s = s.toLowerCase();

        if(s.equalsIgnoreCase("balance_default")){
            String amount = "";
            return amount + Math.round(a.getBalance(dc).getBalance().doubleValue());
        }else if(s.equalsIgnoreCase("balance_default_formatted")){
            return dc.format(a.getBalance(dc).getBalance());
        }

        else if(s.startsWith("balance_") || !s.startsWith("balance_default")) {
            String[] currencyArray = s.split("_");
            Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currencyArray[1]);
            if (!currencyResult.isSuccess()) {
                return "Currency not found";
            }
            Currency c = currencyResult.getValue();
            if (s.equalsIgnoreCase("balance_" + currencyArray[1] + "_formatted")) {
                return c.format(a.getBalance(c).getBalance());
            } else {
                String amount = "";
                return amount + Math.round(a.getBalance(c).getBalance().doubleValue());
            }
        }

        return null;
    }
}