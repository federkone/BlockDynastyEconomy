package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.entities.account.Account;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.MessageService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;


//TODO PENSAR SI USAR CACHE O NO. QUIZAS NO ES NECESARIO
public class BalanceTopCommand implements CommandExecutor {
    private final GetAccountsUseCase getAccountsUseCase;
    private final MessageService messageService;

    public BalanceTopCommand(GetAccountsUseCase getAccountsUseCase, MessageService messageService) {
        this.getAccountsUseCase = getAccountsUseCase;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("gemseconomy.command.baltop")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /baltop <currency> [limit]");
            return false;
        }
        String nameCurrency =  args[0];
        int limit = 10;
        if (args.length>1){
            try {
                limit = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("El segundo argumento debe ser un numero");
                return false;
            }

        }


        int finalLimit = limit;
        SchedulerUtils.runAsync(() -> {
            Result<List<Account>> resultAccounts =getAccountsUseCase.getTopAccounts(nameCurrency, finalLimit,0);
            if (resultAccounts.isSuccess()){
                sender.sendMessage("Top "+ finalLimit +" "+ nameCurrency+" : ");
                sender.sendMessage(messageService.getBalanceTopMessage(resultAccounts.getValue(),nameCurrency));
                //for (Account account : resultAccounts.getValue()) {
                //    sender.sendMessage(account.getNickname() + " " + account.getBalance(nameCurrency).getBalance());
                //}

            }else{
                messageService.sendErrorMessage(resultAccounts.getErrorCode(),sender, nameCurrency);
                /*switch (resultAccounts.getErrorCode()){
                    case ACCOUNT_NOT_FOUND ->   sender.sendMessage(F.getBalanceTopEmpty());
                    case INVALID_ARGUMENT ->  sender.sendMessage("invalid argument");
                    case REPOSITORY_NOT_SUPPORT_TOP ->sender.sendMessage("No support top");
                    case DATA_BASE_ERROR ->   sender.sendMessage("Error in query");
                    default -> sender.sendMessage("Unknown error");
                }*/
            }

        });

        return false;
    }
}
