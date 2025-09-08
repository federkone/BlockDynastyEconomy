package BlockDynasty.BukkitImplementation.Integrations.Placeholder;

import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.Economy.domain.entities.account.Account;
import api.EconomyResponse;
import api.IApi;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class PlaceHolderExpansion extends PlaceholderExpansion {
    private IApi api;

    public PlaceHolderExpansion(IApi api) {
        this.api = api;
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
        Account accountResult = api.getAccount(player.getUniqueId());
        if (accountResult == null) {
            return "Player data not found";
        }

        // Obtener la moneda predeterminada
        Currency defaultCurrencyResult = api.getDefaultCurrency();
        if (defaultCurrencyResult == null) {
            return "Default currency not found";
        }

        Account account = accountResult;
        Currency defaultCurrency = defaultCurrencyResult;

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
        List<Account> accounts = api.getTopAccounts(limit, currencyName);
        if (accounts.isEmpty()) {
            return "No accounts found";
        }


        if (position >= 0) {
            // Validar que la posición solicitada esté dentro del rango
            if (position >= accounts.size()) {
                return "Position out of range";
            }

            // Retornar solo la cuenta en la posición específica
            Account account = accounts.get(position);
            /*return (position + 1) + ". " + account.getNickname() + ": " +
                    account.getBalance(currencyName).getBalance() + " " + currencyName;*/

            Money money = account.getMoney(currencyName);
            return Message.getBalanceTop()
                    .replace("{number}", String.valueOf(position + 1))
                    .replace("{currencycolor}", "" + ChatColor.valueOf(money.getCurrency().getColor()))
                    .replace("{player}", account.getNickname())
                    .replace("{balance}", money.format());
        }

        // Construir el resultado completo si no se solicitó una posición específica
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            Money money = account.getMoney(currencyName);
            result.append(Message.getBalanceTop()
                    .replace("{number}", String.valueOf(i + 1))
                    .replace("{currencycolor}", "" + ChatColor.valueOf(money.getCurrency().getColor()))
                    .replace("{player}", account.getNickname())
                    .replace("{balance}", money.format()));
            /*result.append(i + 1).append(". ")
                    .append(account.getNickname()).append(": ")
                    //.append(balance.getBalance()).append(" ").append(balance.getCurrency().format());
                    .append(balance.getCurrency().format(balance.getBalance()));
                    //.append(account.getBalance(currencyName).getBalance()).append(" ").append(currencyName);*/
            if (i < accounts.size() - 1) {
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
            return String.valueOf(Math.round(account.getMoney(defaultCurrency).getAmount().doubleValue()));
        }

        if (placeholder.equals("balance_default_formatted")) {
            return defaultCurrency.format(account.getMoney(defaultCurrency).getAmount());
        }

        // Manejar balances de otras monedas (ejemplo: %blockdynastyeconomy_balance_dinero%)
        String[] parts = placeholder.split("_");
        if (parts.length < 2) {
            return "Invalid placeholder format";
        }

        String currencyName = parts[1];

        Currency currency = api.getCurrency(currencyName);
        if (currency == null) {
            return "Currency not found";
        }

        if (placeholder.equals("balance_" + currencyName + "_formatted")) {  //todo, permit use _symbol for formated with symbol
           // return String.valueOf(Math.round(account.getBalance(currency).getBalance().doubleValue()));
            return ChatColor.valueOf(currency.getColor()) + currency.format(account.getMoney(currency).getAmount());//%BlockDynastyEconomy_balance_Dinero_formatted%
        } else {
            //return currency.format(account.getBalance(currency).getBalance());
            return String.valueOf(account.getMoney(currency).getAmount().doubleValue());   //%BlockDynastyEconomy_balance_Dinero%
        }
    }

}