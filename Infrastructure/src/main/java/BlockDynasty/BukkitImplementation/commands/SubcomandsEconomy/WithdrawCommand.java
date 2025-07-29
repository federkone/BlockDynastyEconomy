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

public class WithdrawCommand implements CommandExecutor {
    private final WithdrawUseCase withdraw;
    private final BlockDynasty.BukkitImplementation.services.MessageService messageService;

    public WithdrawCommand(WithdrawUseCase withdraw, BlockDynasty.BukkitImplementation.services.MessageService messageService) {
        this.withdraw = withdraw;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("BlockDynastyEconomy.command.take")) {
            sender.sendMessage(Message.getNoPerms());
            return true;
        }


        if (args.length == 0) {
            Message.getManageHelp(sender); //todo el mensaje de eco help
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(Message.getTakeUsage());
            return true;
        }

        String target = args[0];
        String montoString= args[1];
        String currencyName = args[2];

        double amount=0;
        try {
            amount = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            sender.sendMessage(Message.getUnvalidAmount());
            return true;
        }

        double finalMount = amount;

        Runnable AsyncRunnable = () ->{
                Result<Void> result = withdraw.execute(target, currencyName, BigDecimal.valueOf(finalMount));

                Runnable runnable = () -> {
                    if(result.isSuccess()){
                        sender.sendMessage(messageService.getWithdrawMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                        Player targetPlayer = Bukkit.getPlayer(target);
                        if (targetPlayer != null) {
                            // targetPlayer.sendMessage("§a Se ha descontado " + finalMount + " " + currencyName);
                            targetPlayer.sendMessage(messageService.getWithdrawSuccess( currencyName, BigDecimal.valueOf(finalMount)));
                        }
                    }else{
                        messageService.sendErrorMessage(result.getErrorCode(),sender,currencyName);
                    }
                };

                if( sender instanceof Player player) {
                    Scheduler.run( ContextualTask.build(runnable, player));
                }else {
                    runnable.run();
                }
        };

        Scheduler.runAsync(ContextualTask.build(AsyncRunnable));
        return false;
    }
}
