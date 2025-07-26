package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.ChatColor;

public class ViewCommand implements CommandExecutor {
    private final GetCurrencyUseCase getCurrencyUseCase;

    public ViewCommand(GetCurrencyUseCase getCurrencyUseCase) {
        this.getCurrencyUseCase = getCurrencyUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Message.getCurrencyUsage_View());
            return true;
        }

        Scheduler.runAsync(ContextualTask.build(() -> {
            Result<Currency> resultCurrency = getCurrencyUseCase.getCurrency(args[0]);
            if (!resultCurrency.isSuccess()) {
                sender.sendMessage(Message.getUnknownCurrency());
                return;
            }

            Currency currency = resultCurrency.getValue();
            sender.sendMessage(Message.getPrefix() + "§7ID: §c" + currency.getUuid().toString());
            sender.sendMessage(Message.getPrefix() + "§7Singular: "+ChatColor.valueOf(currency.getColor()) + currency.getSingular() + "§7, Plural: " +ChatColor.valueOf(currency.getColor())+ currency.getPlural());
            sender.sendMessage(Message.getPrefix() + "§7Color: " +  ChatColor.valueOf(currency.getColor())+ currency.getColor());
            sender.sendMessage(Message.getPrefix() + "§7Symbol: " +  ChatColor.valueOf(currency.getColor())+ currency.getSymbol());
            sender.sendMessage(Message.getPrefix() + "§7Start Balance: " + ChatColor.valueOf(currency.getColor()) + currency.format(currency.getDefaultBalance()) + "§7.");
            sender.sendMessage(Message.getPrefix() + "§7Decimals: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"));
            sender.sendMessage(Message.getPrefix() + "§7Default: " + (currency.isDefaultCurrency() ? "§aYes" : "§cNo"));
            sender.sendMessage(Message.getPrefix() + "§7Payable: " + (currency.isPayable() ? "§aYes" : "§cNo"));
            sender.sendMessage(Message.getPrefix() + "§7Rate: " + ChatColor.valueOf(currency.getColor()) + currency.getExchangeRate());

        }));
        return false;
    }
}
