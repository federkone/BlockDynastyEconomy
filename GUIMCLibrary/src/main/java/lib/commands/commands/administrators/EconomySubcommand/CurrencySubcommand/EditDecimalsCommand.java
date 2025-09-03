package lib.commands.commands.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditDecimalsCommand extends AbstractCommand {
    public final EditCurrencyUseCase toggleFeaturesUseCase;

    public EditDecimalsCommand(EditCurrencyUseCase toggleFeaturesUseCase) {
        super("decimal","BlockDynastyEconomy.command.currency", List.of("currency"));
        this.toggleFeaturesUseCase = toggleFeaturesUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }
        if (args.length <1) {
            sender.sendMessage("§cUsage: /economy currency decimal <currencyName>");
            return false;
        }

        String currencyName = args[0];

        try {
            toggleFeaturesUseCase.toggleDecimals(currencyName);
            sender.sendMessage ("§7Toggled Decimal Support for §f" + currencyName);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage(" §cUnknown currency.§f " + currencyName);
        } catch (TransactionException e) {
            sender.sendMessage( "§cError toggling decimals for currency §f" + currencyName);
        }
        return true;
    }
}
