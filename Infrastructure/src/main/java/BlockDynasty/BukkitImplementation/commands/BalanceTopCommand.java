package BlockDynasty.BukkitImplementation.commands;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BalanceTopCommand implements CommandExecutor {
    private final SearchAccountUseCase searchAccountUseCase;
    private final BlockDynasty.BukkitImplementation.services.MessageService messageService;

    public BalanceTopCommand(SearchAccountUseCase searchAccountUseCase, BlockDynasty.BukkitImplementation.services.MessageService messageService) {
        this.searchAccountUseCase = searchAccountUseCase;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("BlockDynastyEconomy.command.baltop")) {
            sender.sendMessage(Message.getNoPerms());
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
        Scheduler.runAsync(ContextualTask.build(() -> {
            Result<List<Account>> resultAccounts = searchAccountUseCase.getTopAccounts(nameCurrency, finalLimit,0);
            if (!resultAccounts.isSuccess()){
                messageService.sendErrorMessage(resultAccounts.getErrorCode(),sender, nameCurrency);

            }else{
                sender.sendMessage(messageService.getBalanceTopMessage(resultAccounts.getValue(),nameCurrency,finalLimit));
                //for (Account account : resultAccounts.getValue()) {
                //    sender.sendMessage(account.getNickname() + " " + account.getBalance(nameCurrency).getBalance());
                //}
            }

        }));

        return false;
    }
}
