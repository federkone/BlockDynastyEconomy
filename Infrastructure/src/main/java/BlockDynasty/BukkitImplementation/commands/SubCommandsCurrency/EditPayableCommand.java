package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.BukkitImplementation.config.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EditPayableCommand implements CommandExecutor {
    private final EditCurrencyUseCase toggleFeaturesUseCase;

    public EditPayableCommand(EditCurrencyUseCase toggleFeaturesUseCase) {
        this.toggleFeaturesUseCase = toggleFeaturesUseCase;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Message.getCurrencyUsage_Payable());
            return true;
        }

        String currencyName = args[0];
        Scheduler.runAsync(ContextualTask.build(() -> {
            try {
                toggleFeaturesUseCase.togglePayable(currencyName);
                sender.sendMessage(Message.getPrefix() + "§7Toggled payability for §f" + currencyName);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(Message.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage(Message.getPrefix() + "§cAn error occurred while toggling payability for §f" + currencyName);
            }
        }));
        return false;
    }
}
