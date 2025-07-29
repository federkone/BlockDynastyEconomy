package BlockDynasty.BukkitImplementation.commands.SubCommandsOffer;

import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class CreateOfferCommand implements CommandExecutor {
    private final CreateOfferUseCase createOfferUseCase;
    private final BlockDynasty.BukkitImplementation.services.MessageService messageService;

    public CreateOfferCommand(CreateOfferUseCase createOfferUseCase , BlockDynasty.BukkitImplementation.services.MessageService messageService) {
        this.createOfferUseCase = createOfferUseCase;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //SI TIENE PERMISO

        //SI LOS ARGUMENTOS SON MENOR A 5
        if (args.length != 5) {
            sender.sendMessage(Message.getOfferUsageCreate()); //subcomando de eco
            return false;
        }
        //INTENTAR PARSEAR LOS MONTOS SINO INFORMAR ERROR
        double cantidad;
        double monto;
        try {
           cantidad= Double.parseDouble(args[0]);
            monto =Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(messageService.getUnvalidAmount());
            return false;
        }

        //CAPTURAR LOS TIPOS DE MONEDA y target
        String tipoCantidad = args[1];
        String tipoMonto = args[3];
        Player target = Bukkit.getPlayer(args[4]);

        //si target no existe no se puede ofertar o no esta en linea
        if (target == null || !target.isOnline()) {
            sender.sendMessage(Message.getOfflinePlayer());
            return false;
        }

        Player player = (Player) sender;

        if(Message.getEnableDistanceLimitOffer()){
            double distance = Message.getDistanceLimitOffer();
            if(player.getLocation().distance(target.getLocation())>distance){
                sender.sendMessage(Message.getTooFar(distance));
                return false;
            }
        }

        if(player.getName().equals(target.getName())){        // SI SE ESTA INTENTANDO OFRECER A SI MISMO
            sender.sendMessage(Message.getOfferYourself());
            return false;
        }

        //si entre el target y el vendedor no hay una distancia de 5 bloques informar error

        //INTENTA CREAR LA OFERTA
        Result<Void> result =createOfferUseCase.execute(player.getUniqueId(), target.getUniqueId(),tipoCantidad, BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto));
        if(result.isSuccess()){
            player.sendMessage(messageService.getOfferSendMessage(target.getName(),tipoCantidad,BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto)));
            //player.sendMessage("has ofertado a "+target.getName() +" "+cantidad+" "+tipoCantidad+" por "+monto+" "+tipoMonto);
            target.sendMessage(messageService.getOfferReceiveMessage(player.getName(),tipoCantidad,BigDecimal.valueOf(cantidad),tipoMonto,BigDecimal.valueOf(monto)));
            //target.sendMessage("Has recibido una oferta de "+player.getName()+" por "+cantidad+" "+tipoCantidad+" por "+monto+" "+tipoMonto);
            //target.sendMessage("§7Para aceptarla usa §a/offer accept §b"+player.getName()+ " o §a/offer deny §b"+player.getName());
        }else{
            messageService.sendErrorMessage(result.getErrorCode(),player,tipoCantidad);
        }
        return false;
    }
}
