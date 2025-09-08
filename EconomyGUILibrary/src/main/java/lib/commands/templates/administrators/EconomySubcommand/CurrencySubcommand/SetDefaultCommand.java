package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class SetDefaultCommand extends AbstractCommand {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public SetDefaultCommand(EditCurrencyUseCase editCurrencyUseCase) {
        super("default","BlockDynastyEconomy.command.currency", List.of("currency"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String currencyName =args[0];
        try {
            editCurrencyUseCase.setDefaultCurrency(currencyName);
            sender.sendMessage("§7Set default currency to §f" + currencyName);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage( "§cUnknown currency");
        } catch (TransactionException e) {
            sender.sendMessage( "§cError while setting default currency");
        }

        return true;
    }
}
