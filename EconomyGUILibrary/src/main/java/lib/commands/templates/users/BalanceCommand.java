package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Money;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.CommandsFactory;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.messages.ChatColor;
import lib.messages.MessageService;

import java.util.List;
import java.util.Map;

public class BalanceCommand extends AbstractCommand {
    private final GetBalanceUseCase balance;

    public BalanceCommand(GetBalanceUseCase balance) {
        super("balance","BlockDynastyEconomy.command.balance");
        this.balance = balance;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        Result<List<Money>> resultBalances = balance.getBalances(sender.getName());
        if (!resultBalances.isSuccess()) {
            sender.sendMessage(resultBalances.getErrorMessage()+ " "+resultBalances.getErrorCode());
        }else{
            for (Money entry : resultBalances.getValue()) {
                Currency currency = entry.getCurrency();
                sender.sendMessage(MessageService.getMessage("Messages.balance.list", Map.of("currencycolor", ChatColor.stringValueOf(currency.getColor()),"format",entry.format()) ));
            }
        }
        return true;
    }
}
