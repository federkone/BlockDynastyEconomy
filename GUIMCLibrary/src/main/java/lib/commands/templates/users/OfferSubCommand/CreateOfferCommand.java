package lib.commands.templates.users.OfferSubCommand;

import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;

import java.math.BigDecimal;
import java.util.List;

public class CreateOfferCommand extends AbstractCommand {
    private final CreateOfferUseCase createOfferUseCase;

    public CreateOfferCommand(CreateOfferUseCase createOfferUseCase ) {
        super("create","", List.of(""));
        this.createOfferUseCase = createOfferUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        //SI TIENE PERMISO

        //SI LOS ARGUMENTOS SON MENOR A 5
        if (args.length != 5) {
            sender.sendMessage(""); //subcomando de eco
            return false;
        }
        //INTENTAR PARSEAR LOS MONTOS SINO INFORMAR ERROR
        double cantidad;
        double monto;
        try {
            cantidad= Double.parseDouble(args[0]);
            monto =Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("");
            return false;
        }

        //CAPTURAR LOS TIPOS DE MONEDA y target
        String tipoCantidad = args[1];
        String tipoMonto = args[3];
        Source target = CommandsFactory.getPlatformAdapter().getPlayer(args[4]);

        //si target no existe no se puede ofertar o no esta en linea
        if (target == null || !target.isOnline()) {
            sender.sendMessage("");
            return false;
        }

        Source player = sender;

        //if(Message.getEnableDistanceLimitOffer()){
        //    double distance = Message.getDistanceLimitOffer();
         //   if(player.getLocation().distance(target.getLocation())>distance){
         //       sender.sendMessage(Message.getTooFar(distance));
         //       return false;
          //  }
        //}

        if(player.getName().equals(target.getName())){        // SI SE ESTA INTENTANDO OFRECER A SI MISMO
            sender.sendMessage("");
            return false;
        }

        //si entre el target y el vendedor no hay una distancia de 5 bloques informar error

        //INTENTA CREAR LA OFERTA
        Result<Void> result =createOfferUseCase.execute(player.getUniqueId(), target.getUniqueId(),tipoCantidad, BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto));
        if(result.isSuccess()){
            //player.sendMessage(messageService.getOfferSendMessage(target.getName(),tipoCantidad,BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto)));
            //player.sendMessage("has ofertado a "+target.getName() +" "+cantidad+" "+tipoCantidad+" por "+monto+" "+tipoMonto);
            //target.sendMessage(messageService.getOfferReceiveMessage(player.getName(),tipoCantidad,BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto)));
            //target.sendMessage("Has recibido una oferta de "+player.getName()+" por "+cantidad+" "+tipoCantidad+" por "+monto+" "+tipoMonto);
            //target.sendMessage("§7Para aceptarla usa §a/offer accept §b"+player.getName()+ " o §a/offer deny §b"+player.getName());
        }else{
            //messageService.sendErrorMessage(result.getErrorCode(),player,tipoCantidad);
        }
        return true;

    }
}
