package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.Placeholder;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.domain.entities.balance.Balance;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class BlockdynastyEconomyExpansion extends PlaceholderExpansion {
    private final GetAccountsUseCase getAccountsUseCase;
    private final GetCurrencyUseCase getCurrencyUseCase;

    public BlockdynastyEconomyExpansion(GetAccountsUseCase getAccountsUseCase, GetCurrencyUseCase getCurrencyUseCase) {
        this.getAccountsUseCase = getAccountsUseCase;
        this.getCurrencyUseCase = getCurrencyUseCase;
    }

    @Override
    public boolean register() {
        if(!canRegister()){
            return false;
        }
        return super.register();
    }

    @Override
    public boolean canRegister() {
        return true;
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
        if (player == null || s == null) {
            return "";
        }

        // Normalizar la entrada del placeholder
        s = s.toLowerCase();

        // Placeholder para "top" (ejemplo: %blockdynastyeconomy_top_dinero_10%)
        if (s.startsWith("top_")) {
            return handleTopPlaceholder(s);
        }

        // Obtener la cuenta del jugador
        Result<Account> accountResult = getAccountsUseCase.getAccount(player.getUniqueId());
        if (!accountResult.isSuccess()) {
            return "Player data not found";
        }

        // Obtener la moneda predeterminada
        Result<Currency> defaultCurrencyResult = getCurrencyUseCase.getDefaultCurrency();
        if (!defaultCurrencyResult.isSuccess()) {
            return "Default currency not found";
        }

        Account account = accountResult.getValue();
        Currency defaultCurrency = defaultCurrencyResult.getValue();

        // Placeholder para balance (ejemplo: %blockdynastyeconomy_balance_dinero%)
        if (s.startsWith("balance_")) {
            return handleBalancePlaceholder(s, account, defaultCurrency);
        }

        return null; // Placeholder desconocido
    }

    // Manejar el placeholder "top"
    //ejemplo de usos : %blockdynastyeconomy_top_dinero_10%
    // %blockdynastyeconomy_top_dinero_10_1%
    // %blockdynastyeconomy_top_dinero_10_2%
    // %blockdynastyeconomy_top_dinero_10_3%
    private String handleTopPlaceholder(String placeholder) {
        String[] parts = placeholder.split("_");
        if (parts.length < 3) {
            return "Invalid placeholder format";
        }

        String currencyName = parts[1];
        int limit;
        try {
            limit = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return "Invalid limit number";
        }

        // Determinar si se solicita una posición específica
        int position = -1; // -1 indica que no se solicitó una posición específica
        if (parts.length >= 4) {
            try {
                position = Integer.parseInt(parts[3]) - 1; // Ajustar a índice basado en 0
            } catch (NumberFormatException e) {
                return "Invalid position number";
            }
        }

        // Obtener las cuentas principales
        Result<List<Account>> resultTopAccounts =getAccountsUseCase.getTopAccounts(currencyName, limit, 0);
        if (!resultTopAccounts.isSuccess()){
            switch (resultTopAccounts.getErrorCode()){
                case ACCOUNT_NOT_FOUND, INVALID_ARGUMENT, DATA_BASE_ERROR,REPOSITORY_NOT_SUPPORT_TOP -> {
                    return resultTopAccounts.getErrorMessage();
                }
            }
        }

        List<Account> topAccounts = resultTopAccounts.getValue();
        if (topAccounts.isEmpty()) {
            return "No accounts found for currency: " + currencyName;
        }

        if (position >= 0) {
            // Validar que la posición solicitada esté dentro del rango
            if (position >= topAccounts.size()) {
                return "Position out of range";
            }

            // Retornar solo la cuenta en la posición específica
            Account account = topAccounts.get(position);
            /*return (position + 1) + ". " + account.getNickname() + ": " +
                    account.getBalance(currencyName).getBalance() + " " + currencyName;*/

            return F.getBalanceTop()
                    .replace("{number}", String.valueOf(position + 1))
                    .replace("{currencycolor}", "" + ChatColor.valueOf(account.getBalance(currencyName).getCurrency().getColor()) )
                    .replace("{player}", account.getNickname())
                    .replace("{balance}", account.getBalance(currencyName).getCurrency().format(account.getBalance(currencyName).getBalance()));
        }

        // Construir el resultado completo si no se solicitó una posición específica
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < topAccounts.size(); i++) {
            Account account = topAccounts.get(i);
            Balance balance = account.getBalance(currencyName);
            result.append(F.getBalanceTop()
                    .replace("{number}", String.valueOf(i + 1))
                    .replace("{currencycolor}", "" + ChatColor.valueOf(balance.getCurrency().getColor()))
                    .replace("{player}", account.getNickname())
                    .replace("{balance}", balance.getCurrency().format(balance.getBalance())));
            /*result.append(i + 1).append(". ")
                    .append(account.getNickname()).append(": ")
                    //.append(balance.getBalance()).append(" ").append(balance.getCurrency().format());
                    .append(balance.getCurrency().format(balance.getBalance()));
                    //.append(account.getBalance(currencyName).getBalance()).append(" ").append(currencyName);*/
            if (i < topAccounts.size() - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    // Manejar el placeholder "balance"
    //ejemplo de usos : %blockdynastyeconomy_balance_dinero%
    // %blockdynastyeconomy_balance_dinero_formatted%
    private String handleBalancePlaceholder(String placeholder, Account account, Currency defaultCurrency) {
        if (placeholder.equals("balance_default")) {
            return String.valueOf(Math.round(account.getBalance(defaultCurrency).getBalance().doubleValue()));
        }

        if (placeholder.equals("balance_default_formatted")) {
            return defaultCurrency.format(account.getBalance(defaultCurrency).getBalance());
        }

        // Manejar balances de otras monedas (ejemplo: %blockdynastyeconomy_balance_dinero%)
        String[] parts = placeholder.split("_");
        if (parts.length < 2) {
            return "Invalid placeholder format";
        }

        String currencyName = parts[1];
        Result<Currency> currencyResult = getCurrencyUseCase.getCurrency(currencyName);
        if (!currencyResult.isSuccess()) {
            return "Currency not found";
        }

        Currency currency = currencyResult.getValue();

        if (placeholder.equals("balance_" + currencyName + "_formatted")) {  //todo, permit use _symbol for formated with symbol
           // return String.valueOf(Math.round(account.getBalance(currency).getBalance().doubleValue()));
            return currency.format(account.getBalance(currency).getBalance());//%BlockDynastyEconomy_balance_Dinero_formatted%
        } else {
            //return currency.format(account.getBalance(currency).getBalance());
            return String.valueOf(account.getBalance(currency).getBalance().doubleValue());   //%BlockDynastyEconomy_balance_Dinero%
        }
    }

}