package lib.commands.commands.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditSingularNameCommand extends AbstractCommand {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditSingularNameCommand(EditCurrencyUseCase editCurrencyUseCase) {
        super("singular", "BlockDynastyEconomy.command.currency",List.of("currency","newSingular"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }
        if(args.length < 2){
            sender.sendMessage("§cUsage: /currency singular <singular> <newSingular>");
            return false;
        }

        String plural = args[0];
        String newPlural = args[1];
        try {
            editCurrencyUseCase.setSingularName(plural, newPlural);
            sender.sendMessage("Plural name updated for " + plural + " to " + newPlural);
        }catch (CurrencyNotFoundException e) {
            sender.sendMessage("Unknown currency");
        } catch (TransactionException e) {
            sender.sendMessage("error while updating the plural name");
        }
        return true;
    }
}
