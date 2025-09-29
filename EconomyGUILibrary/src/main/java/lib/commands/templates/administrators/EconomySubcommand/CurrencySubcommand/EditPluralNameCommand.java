package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditPluralNameCommand extends AbstractCommand {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditPluralNameCommand(EditCurrencyUseCase editCurrencyUseCase) {
        super("plural", "BlockDynastyEconomy.command.currency",List.of("currency","newPlural"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String plural = args[0];
        String newPlural = args[1];
        try {
            editCurrencyUseCase.setPluralName(plural, newPlural);
            sender.sendMessage("Plural name updated for " + plural + " to " + newPlural);
        }catch (CurrencyNotFoundException e) {
                sender.sendMessage( "Unknown currency" );
        } catch (TransactionException e) {
            sender.sendMessage("Error while updating the plural name");
        }
        return false;
    }
}
