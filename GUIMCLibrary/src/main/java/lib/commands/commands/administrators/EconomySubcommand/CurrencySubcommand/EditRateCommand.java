package lib.commands.commands.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EditRateCommand extends AbstractCommand {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditRateCommand(EditCurrencyUseCase editCurrencyUseCase) {
        super("rate","BlockDynastyEconomy.command.currency", List.of("currency","rate"));
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }
        if(args.length < 2){
            sender.sendMessage("Usage: /eco currency rate <currencyName> <newRate>");
            return false;
        }

        String currencyName = args[0];
        String rate = args[1];

        double amount;
        try {
            amount = Double.parseDouble(rate);
            if (amount <= 0.0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            sender.sendMessage("invalid amount");
            return false;
        }

        double finalAmount = amount;

        try {
            editCurrencyUseCase.setCurrencyRate(currencyName, finalAmount);
            sender.sendMessage("Rate currency updated for " + currencyName + " to " + rate);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage(" Unknown currency");
        } catch (TransactionException e) {
            sender.sendMessage("error while updating the rate");
        }

        return true;
    }
}
