package BlockDynasty.BukkitImplementation.commands.SubcomandsEconomy;


import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.SetBalanceUseCase;
import BlockDynasty.BukkitImplementation.config.file.F;
import BlockDynasty.BukkitImplementation.config.file.MessageService;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
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

        if (!sender.hasPermission("gemseconomy.command.set")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

        if (args.length == 0) {
            F.getManageHelp(sender);  //todo el mensaje de eco help
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(F.getSetUsage());
            return true;
        }

        String target = args[0];
        String montoString= args[1];
        String currencyName = args[2];

        double amount=0;
        try {
            amount = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        double finalMount = amount;

        Runnable AsyncRunnable = () -> {
            Result<Void> result = setbalance.execute(target, currencyName, BigDecimal.valueOf(finalMount));

            SchedulerFactory.run( new ContextualTask(() -> {
                if(result.isSuccess()){
                    sender.sendMessage(messageService.getDepositMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                    Player targetPlayer = Bukkit.getPlayer(target);
                    if (targetPlayer != null) {
                        //targetPlayer.sendMessage("§7Se ha seteado tu balance a " + finalMount + " de " + currencyName);
                        targetPlayer.sendMessage(messageService.getSetSuccess( currencyName, BigDecimal.valueOf(finalMount)));
                    }
                }else{
                    messageService.sendErrorMessage(result.getErrorCode(),sender,target);
                }
            },(Player) sender));
        };
        SchedulerFactory.runAsync(new ContextualTask(AsyncRunnable));
        return false;
    }




}
