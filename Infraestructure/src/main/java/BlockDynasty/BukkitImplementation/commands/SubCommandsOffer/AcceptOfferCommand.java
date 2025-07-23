package BlockDynasty.BukkitImplementation.commands.SubCommandsOffer;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.BukkitImplementation.config.file.F;
import BlockDynasty.BukkitImplementation.config.file.MessageService;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;

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
        SchedulerFactory.runAsync(new ContextualTask(() -> {
            Result<Void> result = acceptOfferUseCase.execute(player.getUniqueId(), playerFrom.getUniqueId());

            Runnable mainThreadTask = () -> {
                if (result.isSuccess()) {
                    // Mensaje para playerFrom (jugador que hizo la oferta)
                    SchedulerFactory.run(new ContextualTask(() -> {
                        playerFrom.sendMessage(messageService.getOfferAcceptMessage(player.getName()));
                    }, playerFrom));

                    // Mensaje para player (quien acepta la oferta)
                    SchedulerFactory.run(new ContextualTask(() -> {
                        player.sendMessage(messageService.getOfferAcceptToMessage(playerFrom.getName()));
                    }, player));

                } else {
                    switch (result.getErrorCode()) {
                        case OFFER_NOT_FOUND:
                            sender.sendMessage(F.getNotOffers());
                            break;

                        case INSUFFICIENT_FUNDS:
                            sender.sendMessage("No tienes suficientes fondos para esta oferta");

                            // playerFrom debe ser informado en su propio contexto
                            SchedulerFactory.run(new ContextualTask(() -> {
                                playerFrom.sendMessage("El jugador no tiene suficiente dinero para la oferta");
                            }, playerFrom));
                            break;

                        case DATA_BASE_ERROR:
                            sender.sendMessage(messageService.getUnexpectedErrorMessage());

                            SchedulerFactory.run(new ContextualTask(() -> {
                                playerFrom.sendMessage(F.getOfferCancel());
                            }, playerFrom));
                            break;

                        default:
                            sender.sendMessage(messageService.getUnexpectedErrorMessage());
                            break;
                    }
                }
            };

            SchedulerFactory.run(new ContextualTask(mainThreadTask, player)); // player como contexto principal
        }));
        return false;
    }
}
