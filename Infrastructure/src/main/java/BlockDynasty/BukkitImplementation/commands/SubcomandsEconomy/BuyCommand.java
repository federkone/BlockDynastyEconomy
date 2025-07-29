package BlockDynasty.BukkitImplementation.commands.SubcomandsEconomy;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class BuyCommand implements CommandExecutor {
   private final WithdrawUseCase withdraw;
    private final BlockDynasty.BukkitImplementation.services.MessageService messageService;

    public BuyCommand(WithdrawUseCase withdraw, BlockDynasty.BukkitImplementation.services.MessageService messageService) {
        this.withdraw = withdraw;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("BlockDynastyEconomy.command.buycommand")) {
            sender.sendMessage(messageService.getPayNoPerms()); //no tiene permisos para ejecutar comando pagar
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(Message.getBuyCommandUsage());
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if(player==null) {
            sender.sendMessage(Message.getBuyCommandOffline());
            return false;
        }
        double cantidadDemoneda;
        try {
            cantidadDemoneda = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(Message.getUnvalidAmount());
            return false;
        }

        String tipoDemoneda = args[2];

        //constructor de comando a entregar a partir del argumento 3
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            cmdBuilder.append(args[i]).append(" ");
        }
        String cmd = cmdBuilder.toString().trim();

        Runnable AsyncRunnable = () -> {
            Result<Void> result =withdraw.execute(player.getName(),tipoDemoneda, BigDecimal.valueOf(cantidadDemoneda));
            Runnable runnable = ()->{
                if(result.isSuccess()){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                    player.sendMessage(Message.getBuyCommandSuccess());
                }else{
                    messageService.sendErrorMessage(result.getErrorCode(),player,tipoDemoneda);
                }
            };
            Scheduler.run(ContextualTask.build(runnable, player));
        };

        Scheduler.runAsync(ContextualTask.build(AsyncRunnable));
        return false;
    }
}
