package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class BalanceCommand extends AbstractCommand {
    private final GetBalanceUseCase balance;

    public BalanceCommand(GetBalanceUseCase balance) {
        super("balance","BlockDynastyEconomy.command.balance");
        this.balance = balance;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(" No tienes permiso para usar este comando.");
            return true;
        }

        //if (args.length == 0) {
        //    F.getManageHelp(sender); //todo el mensaje de eco help
        //    return true;
        //}

        final String target;
        if (args.length > 0) {
            target = args[0];
        }else {
            target = sender.getName();
        }


            Result<List<Money>> resultBalances = balance.getBalances(target);
            if (!resultBalances.isSuccess()) {
                //messageService.sendErrorMessage(resultBalances.getErrorCode(),sender,target);
            }else{
                //sender.sendMessage(Message.getBalanceMultiple().replace("{player}", target));
                for (Money entry : resultBalances.getValue()) {
                    Currency currency = entry.getCurrency();
                    //sender.sendMessage(Message.getBalanceList().replace("{currencycolor}", ChatColor.valueOf(currency.getColor()) + "").replace("{format}", entry.format()));
                }
            }

        return true;
    }
}
