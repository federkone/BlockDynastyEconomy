package lib.commands.templates.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.domain.events.Context;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.CommandsFactory;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class DepositCommand extends AbstractCommand {
    private final DepositUseCase deposit;

    public DepositCommand(DepositUseCase deposit) {
        super("give", "BlockDynastyEconomy.command.give",List.of("player", "amount", "currency"));
        this.deposit = deposit;
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

        Result<Void> result =deposit.execute(target, currencyName, BigDecimal.valueOf(finalMount), Context.COMMAND);

        if (!result.isSuccess()) {
            sender.sendMessage("Deposit failed "+  result.getErrorMessage()+" "+ result.getErrorCode());
        }else {
            sender.sendMessage("&aDeposit successful");
        }

        return true;
    }

}
