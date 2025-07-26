package BlockDynasty.BukkitImplementation.commands.SubcomandsEconomy;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.BukkitImplementation.services.MessageService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class SetCommand implements CommandExecutor {
    private final SetBalanceUseCase setbalance;
    private final MessageService messageService;

    public SetCommand(SetBalanceUseCase setbalance, MessageService messageService) {
        this.setbalance = setbalance;
        this.messageService = messageService;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("BlockDynastyEconomy.command.set")) {
            sender.sendMessage(Message.getNoPerms());
            return true;
        }

        if (args.length == 0) {
            Message.getManageHelp(sender);  //todo el mensaje de eco help
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(Message.getSetUsage());
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

        Runnable AsyncRunnable = () -> {
            Result<Void> result = setbalance.execute(target, currencyName, BigDecimal.valueOf(finalMount));

            Runnable runnable = () ->{
                if(result.isSuccess()){
                    sender.sendMessage(messageService.getDepositMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                    Player targetPlayer = Bukkit.getPlayer(target);
                    if (targetPlayer != null) {
                        //targetPlayer.sendMessage("§7Se ha seteado tu balance a " + finalMount + " de " + currencyName);
                        targetPlayer.sendMessage(messageService.getSetSuccess( currencyName, BigDecimal.valueOf(finalMount)));
                    }
                }else{
                    messageService.sendErrorMessage(result.getErrorCode(),sender,currencyName);
                }
            };

            if (sender instanceof Player player) {
                Scheduler.run( ContextualTask.build(runnable, player));
            }else{
                runnable.run();
            }

        };
        Scheduler.runAsync(ContextualTask.build(AsyncRunnable));
        return false;
    }




}
