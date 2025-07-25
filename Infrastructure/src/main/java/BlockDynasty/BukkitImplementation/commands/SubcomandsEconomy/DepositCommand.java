package BlockDynasty.BukkitImplementation.commands.SubcomandsEconomy;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
import BlockDynasty.BukkitImplementation.config.file.F;
import BlockDynasty.BukkitImplementation.config.file.MessageService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;


public class DepositCommand implements CommandExecutor {
    private DepositUseCase deposit;
    private MessageService messageService;

    public DepositCommand(DepositUseCase deposit, MessageService messageService) {
        this.deposit = deposit;
        this.messageService = messageService;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("gemseconomy.command.give")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

        if (args.length == 0) {
            F.getManageHelp(sender);  //todo el mensaje de eco help
            return true;
        }


        if (args.length < 3) {
            sender.sendMessage(F.getGiveUsage());
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
            Result<Void> result =deposit.execute(target, currencyName, BigDecimal.valueOf(finalMount));

            Runnable runnable = () -> {
                if (result.isSuccess()) {
                    sender.sendMessage(messageService.getDepositMessage(target, currencyName, BigDecimal.valueOf(finalMount)));

                    Player targetPlayer = Bukkit.getPlayer(target);
                    if (targetPlayer != null) {
                        Scheduler.run(ContextualTask.build(() -> {
                            targetPlayer.sendMessage(messageService.getDepositSuccess(currencyName, BigDecimal.valueOf(finalMount)));
                        }, targetPlayer));
                    }
                } else {
                    messageService.sendErrorMessage(result.getErrorCode(), sender, target);
                }
            };

            if(sender instanceof Player player) {
                Scheduler.run( ContextualTask.build(runnable, player));
            }else{
                runnable.run();
            }

        };

        Scheduler.runAsync(ContextualTask.build( AsyncRunnable));
        return false;
    }
}
