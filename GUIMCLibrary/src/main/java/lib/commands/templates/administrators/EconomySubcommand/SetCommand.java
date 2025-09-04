package lib.commands.templates.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;

import java.math.BigDecimal;
import java.util.List;

public class SetCommand extends AbstractCommand {
    private final SetBalanceUseCase setbalance;

    public SetCommand(SetBalanceUseCase setbalance) {
        super("set", "BlockDynastyEconomy.command.set",List.of("player", "amount", "currency"));
        this.setbalance = setbalance;

    }
    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length == 0) {
            //Message.getManageHelp(sender);  //todo el mensaje de eco help
            sender.sendMessage(" Usa /eco set [player] [amount] [currency]");
            return false;
        }

        if (args.length < 3) {
            //sender.sendMessage(Message.getSetUsage());
            sender.sendMessage(" Usa /eco set [player] [amount] [currency]");
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


            Result<Void> result = setbalance.execute(target, currencyName, BigDecimal.valueOf(finalMount));


                if(result.isSuccess()){
                    //sender.sendMessage(messageService.getDepositMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                    Source targetPlayer = CommandsFactory.getPlatformAdapter().getPlayer(target);
                    if (targetPlayer != null) {
                        //targetPlayer.sendMessage("§7Se ha seteado tu balance a " + finalMount + " de " + currencyName);
                        //targetPlayer.sendMessage(messageService.getSetSuccess( currencyName, BigDecimal.valueOf(finalMount)));
                    }
                }else{
                    //messageService.sendErrorMessage(result.getErrorCode(),sender,currencyName);
                }





        return true;
    }

}
