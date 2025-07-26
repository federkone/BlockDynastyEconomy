package BlockDynasty.BukkitImplementation.commands.SubCommandsTransactions;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.balance.GetBalanceUseCase;
import BlockDynasty.Economy.domain.entities.balance.Balance;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.BukkitImplementation.config.file.Message;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import BlockDynasty.BukkitImplementation.services.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class BalanceCommand implements CommandExecutor {
    private final GetBalanceUseCase balance;
    private final MessageService messageService;

    public BalanceCommand(GetBalanceUseCase balance, MessageService messageService) {
        this.balance = balance;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("BlockDynastyEconomy.command.economy")) {
            sender.sendMessage(Message.getNoPerms());
            return true;
        }
        if (!sender.hasPermission("BlockDynastyEconomy.command.balance")) {
            sender.sendMessage(Message.getNoPerms());
            return true;
        }

        //if (args.length == 0) {
        //    F.getManageHelp(sender); //todo el mensaje de eco help
        //    return true;
        //}

        final String target;
        if (args.length > 0) {
            target = args[0];
        }else {
            target = sender.getName();
        }

        Scheduler.runAsync(ContextualTask.build(() -> {
            Result<List<Balance>> resultBalances = balance.getBalances(target);
            if (resultBalances.isSuccess()) {
                sender.sendMessage(Message.getBalanceMultiple().replace("{player}", target));
                for (Balance entry : resultBalances.getValue()) {
                    Currency currency = entry.getCurrency();
                    BigDecimal balance = entry.getAmount();
                    sender.sendMessage(Message.getBalanceList().replace("{currencycolor}", ChatColor.valueOf(currency.getColor()) + "").replace("{format}", currency.format(balance)));
                }
            }else{
                switch (resultBalances.getErrorCode()){
                    case ACCOUNT_NOT_FOUND:
                        sender.sendMessage(messageService.getAccountNotFoundMessage());
                    case CURRENCY_NOT_FOUND:
                        sender.sendMessage(messageService.getNoCurrencyFund(target));
                }
            }
        }));
        return true;
    }
}
