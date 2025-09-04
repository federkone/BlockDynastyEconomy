package lib.commands.templates.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;

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
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }


        if (args.length == 0) {
            //Message.getManageHelp(sender); //todo el mensaje de eco help
            sender.sendMessage(" Usa /eco take [player] [amount] [currency]");
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(" Usa /eco take [player] [amount] [currency]");
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


            Result<Void> result = withdraw.execute(target, currencyName, BigDecimal.valueOf(finalMount));

            if(result.isSuccess()){
                //sender.sendMessage(messageService.getWithdrawMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                sender.sendMessage("withdraw ejecutado");
                Source targetPlayer = CommandsFactory.getPlatformAdapter().getPlayer(target);
                if (targetPlayer != null) {
                    // targetPlayer.sendMessage("§a Se ha descontado " + finalMount + " " + currencyName);
                     //targetPlayer.sendMessage(messageService.getWithdrawSuccess( currencyName, BigDecimal.valueOf(finalMount)));
                }
                }else{
                    sender.sendMessage("withdraw fallido"+ result.getErrorCode()+ result.getErrorMessage());
                    //messageService.sendErrorMessage(result.getErrorCode(),sender,currencyName);
            }
        return true;
    }

}
