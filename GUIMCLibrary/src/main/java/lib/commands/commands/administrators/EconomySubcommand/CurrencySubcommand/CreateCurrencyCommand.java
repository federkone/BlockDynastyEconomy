package lib.commands.commands.administrators.EconomySubcommand.CurrencySubcommand;

import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyAlreadyExist;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class CreateCurrencyCommand extends AbstractCommand {
    private final CreateCurrencyUseCase createCurrencyUseCase;

    public CreateCurrencyCommand(CreateCurrencyUseCase createCurrencyUseCase) {
        super("create", "BlockDynastyEconomy.command.currency",List.of("singular", "plural"));
        this.createCurrencyUseCase = createCurrencyUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage( "§cUsage: /eco currency create <singular> <plural>");
            return false;
        }

        String single = args[0];
        String plural = args[1];

        try {
            createCurrencyUseCase.createCurrency(single, plural);
            sender.sendMessage( "§7Created currency: §a" + single);
        } catch (CurrencyAlreadyExist e) {
            sender.sendMessage( "§cCurrency Already Exist.");
        } catch (TransactionException e) {
            sender.sendMessage( "§cAn error occurred while creating the currency.");
        }

        return true;
    }
}
