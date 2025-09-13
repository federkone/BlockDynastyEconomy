package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class DeleteCurrencyCommand extends AbstractCommand {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public DeleteCurrencyCommand( DeleteCurrencyUseCase deleteCurrencyUseCase) {
        super("delete", "BlockDynastyEconomy.command.currency",List.of("currency"));
        this.deleteCurrencyUseCase = deleteCurrencyUseCase;

    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String currencyName = args[0];

        try {
            deleteCurrencyUseCase.deleteCurrency(currencyName);
            sender.sendMessage( "§7Deleted currency: §a" + currencyName);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage("§7"+ e.getMessage()+" ensure you has another currency setted as default.");
        } catch (TransactionException e) {
            sender.sendMessage( "§cError while deleting currency: §4" + e.getMessage());
        }

        return false;
    }
}
