package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.BukkitImplementation.config.file.F;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DeleteCurrencyCommand implements CommandExecutor {
    private final DeleteCurrencyUseCase deleteCurrencyUseCase;

    public DeleteCurrencyCommand(DeleteCurrencyUseCase deleteCurrencyUseCase) {
        this.deleteCurrencyUseCase = deleteCurrencyUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            sender.sendMessage(F.getCurrencyUsage_Delete());
            return true;
        }

        String currencyName = args[0];

        Scheduler.runAsync(ContextualTask.build(() -> {
            try {
                deleteCurrencyUseCase.deleteCurrency(currencyName);
                sender.sendMessage(F.getPrefix() + "§7Deleted currency: §a" + currencyName);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getPrefix()+"§7"+ e.getMessage()+" asegurate de tener otra moneda por defecto antes de eliminarla");
            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
            }
        }));

        return false;
    }
}
