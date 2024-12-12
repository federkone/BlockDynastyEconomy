package me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsOffer;

import me.BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.domain.Offers.Exceptions.OffertNotFoundException;
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
            sender.sendMessage(F.getOfferUsageCancel());
            return false;
        }
        String playerNme = args[0];
        Player playerFrom = Bukkit.getPlayer(playerNme);
        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage(F.getOfflinePlayer());
            return false;
        }

        try {
            cancelOfferUseCase.execute(playerFrom.getUniqueId());
            //playerFrom.sendMessage("La oferta de "+sender.getName()+" ha sido cancelada");
            playerFrom.sendMessage(messageService.getOfferCancelMessage(sender.getName()));
            //sender.sendMessage("La oferta para "+playerFrom.getName()+" ha sido cancelada");
            sender.sendMessage(messageService.getOfferCancelToMessage(playerFrom.getName()));
        } catch (OffertNotFoundException e) {
            sender.sendMessage(F.getNotOffers());
        }catch (Exception e){
            sender.sendMessage("§cError inesperado");
            playerFrom.sendMessage("§cError inesperado");
            e.printStackTrace();
        }
        return false;
    }
}
