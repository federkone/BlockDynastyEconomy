package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditPayableCommand extends AbstractCommand {
    private final EditCurrencyUseCase toggleFeaturesUseCase;

    public EditPayableCommand(EditCurrencyUseCase toggleFeaturesUseCase) {
        super("payable","BlockDynastyEconomy.command.currency", List.of("currency"));
        this.toggleFeaturesUseCase = toggleFeaturesUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage( " Usage: /eco currency payable <currencyName>");
            return false;
        }

        String currencyName = args[0];

        try {
            toggleFeaturesUseCase.togglePayable(currencyName);
            sender.sendMessage( "§7Toggled payability for §f" + currencyName);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage(" Unknown currency.");
        } catch (TransactionException e) {
            sender.sendMessage("§cAn error occurred while toggling payability for §f" + currencyName);
        }

        return true;
    }
}
