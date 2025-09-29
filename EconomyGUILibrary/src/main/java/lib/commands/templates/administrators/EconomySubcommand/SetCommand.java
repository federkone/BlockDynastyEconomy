package lib.commands.templates.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;
import lib.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class SetCommand extends AbstractCommand {
    private final SetBalanceUseCase setbalance;

    public SetCommand(SetBalanceUseCase setbalance) {
        super("set", "BlockDynastyEconomy.command.set",List.of("player", "amount", "currency"));
        this.setbalance = setbalance;

    }
    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
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

        Result<Void> result = setbalance.execute(target, currencyName, BigDecimal.valueOf(finalMount), Context.COMMAND);

        if(!result.isSuccess()){
            sender.sendMessage("Error on Set Balance: " + result.getErrorMessage()+" "+result.getErrorMessage());
        }else{
            sender.sendMessage("&aSet Balance successful");
        }

        return true;
    }

}
