package me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.domain.currency.Currency;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListCommand implements CommandExecutor {
    private final CreateCurrencyUseCase creteCurrencyUseCase;

    public ListCommand(CreateCurrencyUseCase createCurrencyUseCase){
        this.creteCurrencyUseCase = createCurrencyUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        SchedulerUtils.runAsync(() -> {
            List<Currency> currencies = creteCurrencyUseCase.getCurrencies();
            sender.sendMessage(F.getPrefix() + "§7There are §f" + currencies.size() + "§7 currencies.");
            for (Currency currency : currencies) {
                sender.sendMessage("§a§l>> §e" + currency.getSingular());
            }
        });
        return false;
    }
}
