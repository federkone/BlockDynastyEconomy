package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeCommand extends AbstractCommand {
    private ExchangeUseCase exchange;

    public ExchangeCommand(ExchangeUseCase exchange) {
        super("exchange","BlockDynastyEconomy.command.exchange", List.of(""));
        this.exchange = exchange;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(" No tienes permiso para usar este comando.");
            return false;
        }

       /* if (args.length < 4) {
            F.getExchangeHelp(sender);
            return true;
        }

        String player;
        if (args.length > 4) {
            player = args[4];
        }else {
            player =  sender.getName();
        }

        double toExchangeAmount = 0;
        double toReceiveAmount = 0;
        try {
            toExchangeAmount = Double.parseDouble(args[1]);
            toReceiveAmount = Double.parseDouble(args[3]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[2];

        double finalToExchangeAmount = toExchangeAmount;
        double finalToReceiveAmount = toReceiveAmount;
        */
        if (args.length < 3) {
            //Message.getExchangeHelp(sender);
            return false;
        }
        String player;
        if (args.length > 3) {
            player = args[3];
        }else {
            player =  sender.getName();
        }

        double toReceiveAmount = 0;

        try {
            toReceiveAmount = Double.parseDouble(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(" Monto inválido.");
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[1];

        double finalToReceiveAmount = toReceiveAmount;
        Source targetPlayer = CommandsFactory.getPlatformAdapter().getPlayer(player);

        Result<BigDecimal> result = exchange.execute(player, toExchange, toReceive, null, BigDecimal.valueOf(finalToReceiveAmount));
        if (!result.isSuccess()) {
                //messageService.sendErrorMessage(result.getErrorCode(),targetPlayer,toReceive);
        }

        return true;
    }
}
