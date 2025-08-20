package BlockDynasty.BukkitImplementation.Integrations.Placeholder;

import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import org.bukkit.Bukkit;

public class PlaceHolder {
    private static PlaceHolderExpansion expansion;

    public static void register(SearchAccountUseCase searchAccountUseCase, SearchCurrencyUseCase searchCurrencyUseCase){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Console.log("PlaceholderAPI not found. Expansion won't be loaded.");
            return;
        }
        expansion = new PlaceHolderExpansion(searchAccountUseCase, searchCurrencyUseCase);
        expansion.register();
        Console.log("PlaceholderAPI Expansion registered successfully!");
    }

    public static void unregister(){
        if(expansion != null){
            expansion.unregister();
        }
    }
}
