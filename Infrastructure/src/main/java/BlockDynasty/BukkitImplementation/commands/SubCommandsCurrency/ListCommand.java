package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListCommand implements CommandExecutor {
    private final SearchCurrencyUseCase searchCurrencyUseCase;

    public ListCommand(SearchCurrencyUseCase searchCurrencyUseCase){
        this.searchCurrencyUseCase = searchCurrencyUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Scheduler.runAsync(ContextualTask.build(() -> {
            List<Currency> currencies = searchCurrencyUseCase.getCurrencies();
            sender.sendMessage(Message.getPrefix() + "§7There are §f" + currencies.size() + "§7 currencies.");
            for (Currency currency : currencies) {
                sender.sendMessage("§a§l>> §e" + currency.getSingular());
            }
        }));
        return false;
    }
}
