package me.BlockDynasty.Economy.aplication.commands.SubCommandsCurrency;

import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ViewCommand implements CommandExecutor {
    private final GetCurrencyUseCase getCurrencyUseCase;

    public ViewCommand(GetCurrencyUseCase getCurrencyUseCase) {
        this.getCurrencyUseCase = getCurrencyUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(F.getCurrencyUsage_View());
            return true;
        }

        SchedulerUtils.runAsync(() -> {
            Result<Currency> resultCurrency = getCurrencyUseCase.getCurrency(args[0]);
            if (!resultCurrency.isSuccess()) {
                sender.sendMessage(F.getUnknownCurrency());
                return;
            }

            Currency currency = resultCurrency.getValue();
            sender.sendMessage(F.getPrefix() + "§7ID: §c" + currency.getUuid().toString());
            sender.sendMessage(F.getPrefix() + "§7Singular: §a" + currency.getSingular() + "§7, Plural: §a" + currency.getPlural());
            sender.sendMessage(F.getPrefix() + "§7Start Balance: " + currency.getColor() + currency.format(currency.getDefaultBalance()) + "§7.");
            sender.sendMessage(F.getPrefix() + "§7Decimals: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"));
            sender.sendMessage(F.getPrefix() + "§7Default: " + (currency.isDefaultCurrency() ? "§aYes" : "§cNo"));
            sender.sendMessage(F.getPrefix() + "§7Payable: " + (currency.isPayable() ? "§aYes" : "§cNo"));
            sender.sendMessage(F.getPrefix() + "§7Color: " + currency.getColor() + currency.getColor().name());
            sender.sendMessage(F.getPrefix() + "§7Rate: " + currency.getColor() + currency.getExchangeRate());

        });
        return false;
    }
}
