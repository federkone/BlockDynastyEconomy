package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.DecimalNotSupportedException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditStartBalCommand extends AbstractCommand {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditStartBalCommand(EditCurrencyUseCase editCurrencyUseCase){
        super("startBal", "BlockDynastyEconomy.command.currency",List.of("currency","amount"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Invalid amount");
            return false;
        }

        String currencyName = args[0];
        double finalAmount = amount;

        try {
            editCurrencyUseCase.editStartBal(currencyName, finalAmount);
            sender.sendMessage("§7Starting balance for §f" + currencyName + " §7set: §a" + finalAmount);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage("unknown currency");
        } catch (TransactionException e) {
            sender.sendMessage( "§cError: §7" + e.getMessage());
        } catch (DecimalNotSupportedException e) {
            sender.sendMessage("No decimal support for " + currencyName);
        }

        return true;
    }
}
