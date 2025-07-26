package BlockDynasty.BukkitImplementation.commands.SubCommandsTransactions;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.PayUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import BlockDynasty.BukkitImplementation.services.MessageService;

import java.math.BigDecimal;

public class PayCommand implements CommandExecutor {
    private final PayUseCase pay;
    MessageService messageService;

    public PayCommand(PayUseCase pay, MessageService messageService) {
        this.pay = pay;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Message.getNoConsole());
            return true;
        }

        if (!sender.hasPermission("BlockDynastyEconomy.command.pay")) {
            sender.sendMessage(messageService.getPayNoPerms()); //no tiene permisos para ejecutar comando pagar
            return true;
        }

        Player player = ((Player) sender).getPlayer();
        if(player==null){
            sender.sendMessage(messageService.getAccountNotFoundMessage());
            return true;
        }
        if (args.length < 3) {
            player.sendMessage(messageService.getPayUsage()); //informa como se usa el comando pagar
            return true;
        }

        //UUID playerUuid =player.getUniqueId(); //PLAYER UUID
        String targetName = args[0]; //nombre del jugador


        String currencyName = args[2];  //nombre de la moneda

        if (player.getName().equals(targetName)) {
            player.sendMessage(messageService.getPayYourselfMessage()); //no puede pagarse a si mismo
            return true;
        }
        BigDecimal amount=BigDecimal.ZERO; //monto temporal
        try{
            amount = new BigDecimal(args[1]);  //intentar extraer monto
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                player.sendMessage(messageService.getUnvalidAmount());  // monto invalido en caso de ser menor o igual a 0
                return true;
            }
        }catch (NumberFormatException e){
            player.sendMessage(messageService.getUnvalidAmount());  //monto invalido en caso de ser menor o igual a 0
            return true;
        }

        BigDecimal finalAmount = amount;

        Scheduler.runAsync(ContextualTask.build(() -> {
            Result<Void> result = pay.execute(player.getName(), targetName, currencyName, finalAmount);

            // Volver al hilo principal para enviar mensajes, que usan la API de Bukkit
            Scheduler.run( ContextualTask.build(() -> {
                if (result.isSuccess()){
                    player.sendMessage(messageService.getSuccessMessage(player.getName(), targetName, currencyName, finalAmount));

                    Player targetPlayer = Bukkit.getPlayer(targetName);
                    if (targetPlayer != null) {
                        // Si el jugador objetivo está en línea, envía el mensaje de éxito
                        Scheduler.run( ContextualTask.build(() -> {targetPlayer.sendMessage(messageService.getReceivedMessage(player.getName(), currencyName, finalAmount));},targetPlayer));
                    }
                } else {
                    messageService.sendErrorMessage( result.getErrorCode(), player, currencyName);
                }
            },player));
        }));
        return true;
    }
}