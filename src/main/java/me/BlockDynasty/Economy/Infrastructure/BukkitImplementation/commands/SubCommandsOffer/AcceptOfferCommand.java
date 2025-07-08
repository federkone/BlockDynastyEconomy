package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsOffer;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.MessageService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import me.BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;

public class AcceptOfferCommand  implements CommandExecutor {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final MessageService messageService;

    public AcceptOfferCommand(AcceptOfferUseCase acceptOfferUseCase, MessageService messageService) {
        this.acceptOfferUseCase = acceptOfferUseCase;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length != 1) {
            sender.sendMessage(F.getOfferUsageAccept());
            return false;
        }

        String playerNme = args[0];
        Player playerFrom = Bukkit.getPlayer(playerNme);
        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage(F.getOfflinePlayer());
            return false;
        }

        if(F.getEnableDistanceLimitOffer()){
            double distance =F.getDistanceLimitOffer();
            if(player.getLocation().distance(playerFrom.getLocation())>distance){
                sender.sendMessage(F.getTooFar(distance));
                return false;
            }
        }
        SchedulerUtils.runAsync(() -> {
            Result<Void> result = acceptOfferUseCase.execute(player.getUniqueId(),playerFrom.getUniqueId());  //playerFrom.getUniqueId() //aca podemos mandar el id del player al que quiere aceptarle la oferta
            SchedulerUtils.run( () -> {
                if(result.isSuccess()){
                    //player.sendMessage("Tu oferta para "+playerFrom.getName()+" ha sido aceptada");
                    playerFrom.sendMessage(messageService.getOfferAcceptMessage(player.getName()));
                    //playerFrom.sendMessage("Oferta de "+player.getName()+" aceptada");
                    player.sendMessage(messageService.getOfferAcceptToMessage(playerFrom.getName()));
                }else{
                    switch (result.getErrorCode()){
                        case OFFER_NOT_FOUND:
                            sender.sendMessage(F.getNotOffers());
                            break;
                        case INSUFFICIENT_FUNDS:
                            sender.sendMessage("No tienes suficientes fondos para esta oferta");

                            playerFrom.sendMessage( "El jugador no tiene suficiente dinero para  la oferta");
                            break;
                        case DATA_BASE_ERROR:
                            sender.sendMessage(messageService.getUnexpectedErrorMessage());
                            playerFrom.sendMessage(F.getOfferCancel());
                            break;
                        default:
                            sender.sendMessage(messageService.getUnexpectedErrorMessage());
                            playerFrom.sendMessage(messageService.getUnexpectedErrorMessage());
                            break;
                    }
                }

            });
        });
        return false;
    }
}
