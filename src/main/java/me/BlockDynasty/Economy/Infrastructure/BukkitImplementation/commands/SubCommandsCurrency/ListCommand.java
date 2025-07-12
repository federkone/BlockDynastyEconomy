package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Currency;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListCommand implements CommandExecutor {
    private final GetCurrencyUseCase getCurrencyUseCase;

    public ListCommand(GetCurrencyUseCase getCurrencyUseCase){
        this.getCurrencyUseCase = getCurrencyUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        SchedulerUtils.runAsync(() -> {
            List<Currency> currencies = getCurrencyUseCase.getCurrencies();
            sender.sendMessage(F.getPrefix() + "§7There are §f" + currencies.size() + "§7 currencies.");
            for (Currency currency : currencies) {
                sender.sendMessage("§a§l>> §e" + currency.getSingular());
            }
        });
        return false;
    }
}
