package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.util.colors.ChatColor;

import java.util.List;

public class ListCommand extends AbstractCommand {
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public ListCommand(SearchCurrencyUseCase searchCurrencyUseCase){
        super("list","BlockDynastyEconomy.command.currency");
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }
        List<Currency> currencies = searchCurrencyUseCase.getCurrencies();
        sender.sendMessage( "There are " + currencies.size() + " currencies.");
        for (Currency currency : currencies) {
            sender.sendMessage(">> " + ChatColor.stringValueOf(currency.getColor()) + currency.getSingular());
        }
        return true;
    }
}
