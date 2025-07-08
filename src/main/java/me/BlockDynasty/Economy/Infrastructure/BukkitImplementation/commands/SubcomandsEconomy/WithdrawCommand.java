package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubcomandsEconomy;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.transaction.WithdrawUseCase;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.MessageService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class WithdrawCommand implements CommandExecutor {
    private final WithdrawUseCase withdraw;
    private final MessageService messageService;

    public WithdrawCommand(WithdrawUseCase withdraw, MessageService messageService) {
        this.withdraw = withdraw;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("gemseconomy.command.take")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }


        if (args.length == 0) {
            F.getManageHelp(sender); //todo el mensaje de eco help
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(F.getTakeUsage());
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
        SchedulerUtils.runAsync(() -> {
            Result<Void> result = withdraw.execute(target, currencyName, BigDecimal.valueOf(finalMount));
            SchedulerUtils.run( () -> {
                if(result.isSuccess()){
                    sender.sendMessage(messageService.getWithdrawMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                    Player targetPlayer = Bukkit.getPlayer(target);
                    if (targetPlayer != null) {
                       // targetPlayer.sendMessage("§a Se ha descontado " + finalMount + " " + currencyName);
                        targetPlayer.sendMessage(messageService.getWithdrawSuccess( currencyName, BigDecimal.valueOf(finalMount)));
                    }
                }else{
                    switch (result.getErrorCode()){
                        case ACCOUNT_NOT_FOUND:
                            sender.sendMessage(messageService.getAccountNotFoundMessage());
                            break;
                        case ACCOUNT_NOT_HAVE_BALANCE: //todo: insufficent founds
                            sender.sendMessage(F.getInsufficientFunds());
                            break;
                        case INVALID_AMOUNT:
                            sender.sendMessage(messageService.getUnvalidAmount());
                            break;
                        case DECIMAL_NOT_SUPPORTED:
                            sender.sendMessage(messageService.getUnvalidAmount());
                            break;
                        case INSUFFICIENT_FUNDS:
                            sender.sendMessage(messageService.getInsufficientFundsMessage(currencyName));
                            break;
                        case DATA_BASE_ERROR:
                            sender.sendMessage(messageService.getUnexpectedErrorMessage());
                            break;
                        default:
                            sender.sendMessage(messageService.getUnexpectedErrorMessage());
                            break;
                    }
                }
            });
        });
        return false;
    }
}
