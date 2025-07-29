package BlockDynasty.BukkitImplementation.commands.SubCommandsOffer;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;

public class AcceptOfferCommand  implements CommandExecutor {
    private final AcceptOfferUseCase acceptOfferUseCase;
    private final BlockDynasty.BukkitImplementation.services.MessageService messageService;

    public AcceptOfferCommand(AcceptOfferUseCase acceptOfferUseCase, BlockDynasty.BukkitImplementation.services.MessageService messageService) {
        this.acceptOfferUseCase = acceptOfferUseCase;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length != 1) {
            sender.sendMessage(Message.getOfferUsageAccept());
            return false;
        }

        String playerNme = args[0];
        Player playerFrom = Bukkit.getPlayer(playerNme);
        if (playerFrom == null || !playerFrom.isOnline()) {
            sender.sendMessage(Message.getOfflinePlayer());
            return false;
        }

        if(Message.getEnableDistanceLimitOffer()){
            double distance = Message.getDistanceLimitOffer();
            if(player.getLocation().distance(playerFrom.getLocation())>distance){
                sender.sendMessage(Message.getTooFar(distance));
                return false;
            }
        }
        Scheduler.runAsync(ContextualTask.build(() -> {
            Result<Void> result = acceptOfferUseCase.execute(player.getUniqueId(), playerFrom.getUniqueId());

            Runnable mainThreadTask = () -> {
                if (result.isSuccess()) {
                    // Mensaje para playerFrom (jugador que hizo la oferta)
                    Scheduler.run(ContextualTask.build(() -> {
                        playerFrom.sendMessage(messageService.getOfferAcceptMessage(player.getName()));
                    }, playerFrom));

                    // Mensaje para player (quien acepta la oferta)
                    Scheduler.run(ContextualTask.build(() -> {
                        player.sendMessage(messageService.getOfferAcceptToMessage(playerFrom.getName()));
                    }, player));

                } else {
                    switch (result.getErrorCode()) {
                        case OFFER_NOT_FOUND:
                            sender.sendMessage(Message.getNotOffers());
                            break;

                        case INSUFFICIENT_FUNDS:
                            sender.sendMessage("No tienes suficientes fondos para esta oferta");

                            // playerFrom debe ser informado en su propio contexto
                            Scheduler.run(ContextualTask.build(() -> {
                                playerFrom.sendMessage("El jugador no tiene suficiente dinero para la oferta");
                            }, playerFrom));
                            break;

                        case DATA_BASE_ERROR:
                            sender.sendMessage(messageService.getUnexpectedErrorMessage());

                            Scheduler.run(ContextualTask.build(() -> {
                                playerFrom.sendMessage(Message.getOfferCancel());
                            }, playerFrom));
                            break;

                        default:
                            sender.sendMessage(messageService.getUnexpectedErrorMessage());
                            break;
                    }
                }
            };

            Scheduler.run(ContextualTask.build(mainThreadTask, player)); // player como contexto principal
        }));
        return false;
    }
}
