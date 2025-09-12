package lib.commands.templates.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;
import lib.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class WithdrawCommand extends AbstractCommand {
    private final WithdrawUseCase withdraw;

    public WithdrawCommand(WithdrawUseCase withdraw) {
        super("take", "BlockDynastyEconomy.command.take",List.of("player", "amount", "currency"));
        this.withdraw = withdraw;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String target = args[0];
        String montoString= args[1];
        String currencyName = args[2];

        double amount=0;
        try {
            amount = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageService.getMessage("invalidamount"));
            return false;
        }

        double finalMount = amount;

        Result<Void> result = withdraw.execute(target, currencyName, BigDecimal.valueOf(finalMount), Context.COMMAND);

        if(!result.isSuccess()){
            sender.sendMessage("Withdraw failed: "+ result.getErrorCode()+" "+ result.getErrorMessage());
        }
        return true;
    }

}
