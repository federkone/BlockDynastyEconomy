package BlockDynasty.BukkitImplementation.commands.SubCommandsTransactions;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.transaction.ExchangeUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class ExchangeCommand implements CommandExecutor {
    private ExchangeUseCase exchange;
    private BlockDynasty.BukkitImplementation.services.MessageService messageService;

    public ExchangeCommand(ExchangeUseCase exchange, BlockDynasty.BukkitImplementation.services.MessageService messageService) {
        this.exchange = exchange;
        this.messageService = messageService;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("BlockDynastyEconomy.command.exchange")) {
            sender.sendMessage(Message.getNoPerms());
            return true;
        }

       /* if (args.length < 4) {
            F.getExchangeHelp(sender);
            return true;
        }

        String player;
        if (args.length > 4) {
            player = args[4];
        }else {
            player =  sender.getName();
        }

        double toExchangeAmount = 0;
        double toReceiveAmount = 0;
        try {
            toExchangeAmount = Double.parseDouble(args[1]);
            toReceiveAmount = Double.parseDouble(args[3]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[2];

        double finalToExchangeAmount = toExchangeAmount;
        double finalToReceiveAmount = toReceiveAmount;
        */
        if (args.length < 3) {
            Message.getExchangeHelp(sender);
            return true;
        }
        String player;
        if (args.length > 3) {
            player = args[3];
        }else {
            player =  sender.getName();
        }

        double toReceiveAmount = 0;

        try {
            toReceiveAmount = Double.parseDouble(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(Message.getUnvalidAmount());
            return true;
        }

        String toExchange = args[0];
        String toReceive = args[1];

        double finalToReceiveAmount = toReceiveAmount;
        Player targetPlayer = Bukkit.getPlayer(player);

        Scheduler.runAsync(ContextualTask.build(() -> {
            Result<BigDecimal> result = exchange.execute(player, toExchange, toReceive, null, BigDecimal.valueOf(finalToReceiveAmount));
                if (!result.isSuccess()) {
                    messageService.sendErrorMessage(result.getErrorCode(),targetPlayer,toReceive);
                }
        }));
        return true;
    }
}
