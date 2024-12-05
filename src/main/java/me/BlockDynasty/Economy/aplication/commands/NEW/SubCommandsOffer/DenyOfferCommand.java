package me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsOffer;

import me.BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.domain.Offers.Exceptions.OffertNotFoundException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Bukkit;

public class DenyOfferCommand  implements CommandExecutor {
    private final CancelOfferUseCase cancelOfferUseCase;
    private final MessageService messageService;

    public DenyOfferCommand(CancelOfferUseCase cancelOfferUseCase, MessageService messageService) {
        this.cancelOfferUseCase = cancelOfferUseCase;
        this.messageService = messageService;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(F.getOfferUsageDeny());
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
            //playerFrom.sendMessage("La oferta de "+sender.getName()+" ha sido rechazada");
            playerFrom.sendMessage(messageService.getOfferDenyMessage(sender.getName()));
            //sender.sendMessage("La oferta para "+playerFrom.getName()+" ha sido rechazada");
            sender.sendMessage(messageService.getOfferDenyToMessage(playerFrom.getName()));
        } catch (OffertNotFoundException e) {
            sender.sendMessage("No hay ofertas pendientes de este jugador");
        }
        return false;
    }
}
