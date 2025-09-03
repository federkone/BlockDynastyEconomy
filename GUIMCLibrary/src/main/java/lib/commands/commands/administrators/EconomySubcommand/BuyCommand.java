package lib.commands.commands.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.commands.CommandsFactory;

import java.math.BigDecimal;
import java.util.List;

public class BuyCommand extends AbstractCommand {
    private final WithdrawUseCase withdraw;

    public BuyCommand(WithdrawUseCase withdraw) {
        super("buy","BlockDynastyEconomy.command.buycommand",List.of("player", "amount", "currency", "command..."));
        this.withdraw = withdraw;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length < 3) {
            //sender.sendMessage(Message.getBuyCommandUsage());
            return false;
        }

        Source player = CommandsFactory.getPlatformAdapter().getPlayer(args[0]);

        if(player==null) {
            sender.sendMessage("player is offline");
            return false;
        }
        double cantidadDemoneda;
        try {
            cantidadDemoneda = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            //player.sendMessage(Message.getUnvalidAmount());
            return false;
        }

        String tipoDemoneda = args[2];

        //constructor de comando a entregar a partir del argumento 3
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            cmdBuilder.append(args[i]).append(" ");
        }
        String cmd = cmdBuilder.toString().trim();

        Result<Void> result =withdraw.execute(player.getName(),tipoDemoneda, BigDecimal.valueOf(cantidadDemoneda));

        if(result.isSuccess()){
            CommandsFactory.getPlatformAdapter().dispatchCommand(cmd);
                    //player.sendMessage(Message.getBuyCommandSuccess());
            player.sendMessage("buy command success");
        }else{
                    //messageService.sendErrorMessage(result.getErrorCode(),player,tipoDemoneda);
        }

        return true;
    }

}
