package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;
import lib.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeCommand extends AbstractCommand {
    private ExchangeUseCase exchange;

    public ExchangeCommand(ExchangeUseCase exchange) {
        super("exchange","BlockDynastyEconomy.command.exchange", List.of("fromCurrency","toCurrency","toAmount" ));
        this.exchange = exchange;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if(!super.execute( sender, args)){
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

        //si hay un argumento demas es para otro jugador


        double toReceiveAmount = 0;

        try {
            toReceiveAmount = Double.parseDouble(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(MessageService.getMessage("invalidamount"));
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[1];

        double finalToReceiveAmount = toReceiveAmount;

        Result<BigDecimal> result = exchange.execute(sender.getName(), toExchange, toReceive, null, BigDecimal.valueOf(finalToReceiveAmount));
        if (!result.isSuccess()) {
            sender.sendMessage(result.getErrorMessage()+" " +result.getErrorCode());
        }

        return true;
    }
}
