package me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ViewCommand implements CommandExecutor {
    private final CreateCurrencyUseCase createCurrencyUseCase;

    public ViewCommand(CreateCurrencyUseCase createCurrencyUseCase) {
        this.createCurrencyUseCase = createCurrencyUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(F.getCurrencyUsage_View());
            return true;
        }
        SchedulerUtils.runAsync(() -> {
            Currency currency = createCurrencyUseCase.getCurrency(args[0]);
            if (currency != null) {
                sender.sendMessage(F.getPrefix() + "§7ID: §c" + currency.getUuid().toString());
                sender.sendMessage(F.getPrefix() + "§7Singular: §a" + currency.getSingular() + "§7, Plural: §a" + currency.getPlural());
                sender.sendMessage(F.getPrefix() + "§7Start Balance: " + currency.getColor() + currency.format(currency.getDefaultBalance()) + "§7.");
                sender.sendMessage(F.getPrefix() + "§7Decimals: " + (currency.isDecimalSupported() ? "§aYes" : "§cNo"));
                sender.sendMessage(F.getPrefix() + "§7Default: " + (currency.isDefaultCurrency() ? "§aYes" : "§cNo"));
                sender.sendMessage(F.getPrefix() + "§7Payable: " + (currency.isPayable() ? "§aYes" : "§cNo"));
                sender.sendMessage(F.getPrefix() + "§7Color: " + currency.getColor() + currency.getColor().name());
                sender.sendMessage(F.getPrefix() + "§7Rate: " + currency.getColor() + currency.getExchangeRate());
            } else {
                sender.sendMessage(F.getUnknownCurrency());
            }
        });
        return false;
    }
}
