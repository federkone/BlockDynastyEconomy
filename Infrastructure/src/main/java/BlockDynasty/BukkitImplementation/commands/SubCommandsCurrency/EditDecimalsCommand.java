package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.BukkitImplementation.config.file.F;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EditDecimalsCommand implements CommandExecutor {
    public final EditCurrencyUseCase toggleFeaturesUseCase;

    public EditDecimalsCommand(EditCurrencyUseCase toggleFeaturesUseCase) {
        this.toggleFeaturesUseCase = toggleFeaturesUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length <1) {
            sender.sendMessage(F.getCurrencyUsage_Decimals());
        }

        String currencyName = args[0];

        Scheduler.runAsync(ContextualTask.build(() -> {
            try {
                toggleFeaturesUseCase.toggleDecimals(currencyName);
                sender.sendMessage(F.getPrefix() + "§7Toggled Decimal Support for §f" + currencyName);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§cError toggling decimals for currency §f" + currencyName);
            }
        }));
        return false;
    }
}
