package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubcomandsEconomy;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.transaction.DepositUseCase;
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
        SchedulerUtils.runAsync(() -> {
            Result<Void> result =deposit.execute(target, currencyName, BigDecimal.valueOf(finalMount));

            SchedulerUtils.run( () -> {
                if(result.isSuccess()){
                    sender.sendMessage(messageService.getDepositMessage(target, currencyName, BigDecimal.valueOf(finalMount)));
                    Player targetPlayer = Bukkit.getPlayer(target);
                    if (targetPlayer != null) {
                        targetPlayer.sendMessage(messageService.getDepositSuccess( currencyName, BigDecimal.valueOf(finalMount)));
                        //targetPlayer.sendMessage("§aHas recibido " + finalMount + " " + currencyName);
                    }
                }else{
                    switch (result.getErrorCode()){
                        case ACCOUNT_NOT_FOUND:
                            sender.sendMessage(messageService.getAccountNotFoundMessage());
                            break;
                        case CURRENCY_NOT_FOUND:
                            sender.sendMessage(F.getUnknownCurrency());
                            break;
                        case INVALID_AMOUNT:
                            sender.sendMessage(F.getUnvalidAmount());
                            break;
                        case DECIMAL_NOT_SUPPORTED:
                            sender.sendMessage(F.getUnvalidAmount());
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
