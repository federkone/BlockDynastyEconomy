package lib.commands.templates.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class DeleteCurrencyCommand extends AbstractCommand {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public DeleteCurrencyCommand( DeleteCurrencyUseCase deleteCurrencyUseCase) {
        super("delete", "BlockDynastyEconomy.command.currency",List.of("currency"));
        this.deleteCurrencyUseCase = deleteCurrencyUseCase;

    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage( "§cUsage: /economy currency delete <currency_name>");
            return true;
        }

        String currencyName = args[0];

        try {
            deleteCurrencyUseCase.deleteCurrency(currencyName);
            sender.sendMessage( "§7Deleted currency: §a" + currencyName);
        } catch (CurrencyNotFoundException e) {
            sender.sendMessage("§7"+ e.getMessage()+" asegurate de tener otra moneda por defecto antes de eliminarla");
        } catch (TransactionException e) {
            sender.sendMessage( "§cError while deleting currency: §4" + e.getMessage());
        }

        return false;
    }
}
