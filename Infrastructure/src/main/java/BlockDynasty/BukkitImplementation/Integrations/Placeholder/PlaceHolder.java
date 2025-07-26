package BlockDynasty.BukkitImplementation.Integrations.Placeholder;

import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import org.bukkit.Bukkit;

public class PlaceHolder {
    private static PlaceHolderExpansion expansion;

    public static void register(GetAccountsUseCase getAccountsUseCase, GetCurrencyUseCase getCurrencyUseCase){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            UtilServer.consoleLog("PlaceholderAPI not found. Expansion won't be loaded.");
        }
        expansion = new PlaceHolderExpansion(getAccountsUseCase, getCurrencyUseCase);
        expansion.register();
        UtilServer.consoleLog("PlaceholderAPI Expansion registered successfully!");
    }

    public static void unregister(){
        if(expansion != null){
            expansion.unregister();
        }
    }
}
