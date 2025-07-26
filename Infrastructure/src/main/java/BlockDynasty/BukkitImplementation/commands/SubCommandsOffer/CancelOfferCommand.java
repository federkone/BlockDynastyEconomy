package BlockDynasty.BukkitImplementation.commands.SubCommandsOffer;

import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.BukkitImplementation.services.MessageService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CancelOfferCommand implements CommandExecutor {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final MessageService messageService;

    public CancelOfferCommand(CancelOfferUseCase cancelOfferUseCase, MessageService messageService) {
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Message.getOfferUsageCancel());
            return false;
        }
        String playerNme = args[0];
        Player playerFrom = Bukkit.getPlayer(playerNme);
        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage(Message.getOfflinePlayer());
            return false;
        }

        Result<Void> result =cancelOfferUseCase.execute(playerFrom.getUniqueId());
        if(result.isSuccess()){
            //playerFrom.sendMessage("La oferta de "+sender.getName()+" ha sido cancelada");
            playerFrom.sendMessage(messageService.getOfferCancelMessage(sender.getName()));
            //sender.sendMessage("La oferta para "+playerFrom.getName()+" ha sido cancelada");
            sender.sendMessage(messageService.getOfferCancelToMessage(playerFrom.getName()));
        }else{
            switch (result.getErrorCode()){
                case OFFER_NOT_FOUND:
                    sender.sendMessage(Message.getNotOffers());
                    break;
                default:
                    sender.sendMessage(messageService.getUnexpectedErrorMessage());
                    playerFrom.sendMessage(messageService.getUnexpectedErrorMessage());
                    break;
            }
        }
        return false;
    }
}
