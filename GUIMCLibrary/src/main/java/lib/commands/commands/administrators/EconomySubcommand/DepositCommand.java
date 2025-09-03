package lib.commands.commands.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

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
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length == 0) {
            //Message.getManageHelp(sender);
            sender.sendMessage(" Usa /eco give [player] [amount] [currency]");
            return false;
        }


        if (args.length < 3) {
            //sender.sendMessage(Message.getGiveUsage());
            sender.sendMessage(" args detectados: " + args.length);
            sender.sendMessage(" Usa /eco give [player] [amount] [currency]");
            return false;
        }

        String target = args[0];
        String montoString= args[1];
        String currencyName = args[2];

        double amount=0;
        try {
            amount = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            //sender.sendMessage(Message.getUnvalidAmount());
            return false;
        }

        double finalMount = amount;

        Result<Void> result =deposit.execute(target, currencyName, BigDecimal.valueOf(finalMount));

        if (result.isSuccess()) {
            //sender.sendMessage(messageService.getDepositMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
            //System.out.println("deposit successful");
        }//else {System.out.println("deposit failed "+  result.getErrorMessage()+ result.getErrorCode());};

        return true;
    }

}
