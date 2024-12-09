package me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsOffer;

import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.domain.Offers.Exceptions.OffertNotFoundException;
import me.BlockDynasty.Economy.domain.account.Exceptions.InsufficientFundsException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
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
        try {
            acceptOfferUseCase.execute(player.getUniqueId(),playerFrom.getUniqueId());  //playerFrom.getUniqueId() //aca podemos mandar el id del player al que quiere aceptarle la oferta
            //player.sendMessage("Tu oferta para "+playerFrom.getName()+" ha sido aceptada");
            playerFrom.sendMessage(messageService.getOfferAcceptMessage(player.getName()));
            //playerFrom.sendMessage("Oferta de "+player.getName()+" aceptada");
            player.sendMessage(messageService.getOfferAcceptToMessage(playerFrom.getName()));
        } catch (OffertNotFoundException e) {
            sender.sendMessage(F.getNotOffers());
        }catch (InsufficientFundsException e){
            sender.sendMessage("No tienes suficiente dinero para aceptar la oferta");
            playerFrom.sendMessage("§cEl jugador no tiene suficiente dinero para aceptar la oferta");
        }catch (TransactionException e){
            sender.sendMessage("§cError al intentar la transaccion");
            playerFrom.sendMessage("§cError al intentar la transaccion");
        }


        return false;
    }
}
