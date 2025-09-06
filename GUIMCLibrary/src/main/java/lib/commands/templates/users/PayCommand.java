package lib.commands.templates.users;

import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.CommandsFactory;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PayCommand extends AbstractCommand {
    private final PayUseCase pay;

    public PayCommand(PayUseCase pay) {
        super("pay","BlockDynastyEconomy.command.pay", List.of("player","amount","currency"));
        this.pay = pay;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String targetName = args[0]; //nombre del jugador

        Source target = CommandsFactory.getPlatformAdapter().getPlayer(targetName);
        if(target==null){
            sender.sendMessage("The player is not online");
            return false;
        }

        String currencyName = args[2];  //nombre de la moneda

        BigDecimal amount=BigDecimal.ZERO; //monto temporal
        try{
            amount = new BigDecimal(args[1]);  //intentar extraer monto
        }catch (NumberFormatException e){
            sender.sendMessage("Invalid amount");
            return false;
        }

        BigDecimal finalAmount = amount;

        Result<Void> result = pay.execute(sender.getName(), targetName, currencyName, finalAmount);

        if (!result.isSuccess()){
            sender.sendMessage(result.getErrorMessage()+" " +result.getErrorCode());
        }
        return true;
    }
}
