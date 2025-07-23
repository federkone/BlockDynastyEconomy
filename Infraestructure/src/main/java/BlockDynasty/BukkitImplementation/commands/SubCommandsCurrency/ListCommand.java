package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.BukkitImplementation.config.file.F;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
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

        SchedulerFactory.runAsync(() -> {
            List<Currency> currencies = getCurrencyUseCase.getCurrencies();
            sender.sendMessage(F.getPrefix() + "§7There are §f" + currencies.size() + "§7 currencies.");
            for (Currency currency : currencies) {
                sender.sendMessage("§a§l>> §e" + currency.getSingular());
            }
        });
        return false;
    }
}
