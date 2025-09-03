package lib.commands.commands.users;

import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.math.BigDecimal;
import java.util.List;

public class PayCommand extends AbstractCommand {
    private final PayUseCase pay;

    public PayCommand(PayUseCase pay) {
        super("pay","BlockDynastyEconomy.command.pay", List.of("player","amount","currency"));
        this.pay = pay;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage( " no tienes permisos"); //no tiene permisos para ejecutar comando pagar
            return true;
        }

        Source player = sender;
        if(player==null){
            sender.sendMessage("jugador  no encontrado");
            return true;
        }
        if (args.length < 3) {
            player.sendMessage("Use /pay <player> <amount> <currency>"); //informa como se usa el comando pagar
            return true;
        }

        //UUID playerUuid =player.getUniqueId(); //PLAYER UUID
        String targetName = args[0]; //nombre del jugador


        String currencyName = args[2];  //nombre de la moneda

        if (player.getName().equals(targetName)) {
            player.sendMessage("no puedes pagarte a si mismo"); //no puede pagarse a si mismo
            return true;
        }
        BigDecimal amount=BigDecimal.ZERO; //monto temporal
        try{
            amount = new BigDecimal(args[1]);  //intentar extraer monto
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                player.sendMessage(" invalid ammount");  // monto invalido en caso de ser menor o igual a 0
                return true;
            }
        }catch (NumberFormatException e){
            player.sendMessage("monto invalido");  //monto invalido en caso de ser menor o igual a 0
            return true;
        }

        BigDecimal finalAmount = amount;

            Result<Void> result = pay.execute(player.getName(), targetName, currencyName, finalAmount);

            // Volver al hilo principal para enviar mensajes, que usan la API de Bukkit
                if (!result.isSuccess()){
                    //messageService.sendErrorMessage( result.getErrorCode(), player, currencyName);
                }

        return true;
    }
}
