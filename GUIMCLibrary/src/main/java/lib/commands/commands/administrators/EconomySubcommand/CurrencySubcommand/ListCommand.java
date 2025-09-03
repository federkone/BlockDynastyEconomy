package lib.commands.commands.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class ListCommand extends AbstractCommand {
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public ListCommand(SearchCurrencyUseCase searchCurrencyUseCase){
        super("list","BlockDynastyEconomy.command.currency");
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }
        List<Currency> currencies = searchCurrencyUseCase.getCurrencies();
        sender.sendMessage( "§7There are §f" + currencies.size() + "§7 currencies.");
        for (Currency currency : currencies) {
            sender.sendMessage("§a§l>> §e" + currency.getSingular());
        }
        return true;
    }
}
