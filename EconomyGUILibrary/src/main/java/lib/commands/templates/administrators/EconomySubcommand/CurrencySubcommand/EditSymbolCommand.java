package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditSymbolCommand extends AbstractCommand {
    private EditCurrencyUseCase editCurrencyUseCase;

    public EditSymbolCommand(EditCurrencyUseCase editCurrencyUseCase) {
        super("symbol","BlockDynastyEconomy.command.currency", List.of("currency","symbol"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String currencyName = args[0];
        String currencySymbol = args[1];

        try {
            editCurrencyUseCase.editSymbol(currencyName, currencySymbol);
            sender.sendMessage("§7Currency symbol for §f" + currencyName + " §7updated: §a" + currencySymbol);
        } catch (TransactionException e) {
            sender.sendMessage("§7error in transaction.");
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage("Unknown currency: " + currencyName);
        }

        return true;
    }
}
