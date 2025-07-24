package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.DecimalNotSupportedException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.BukkitImplementation.config.file.F;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EditStartBalCommand implements CommandExecutor {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditStartBalCommand(EditCurrencyUseCase editCurrencyUseCase){
        this.editCurrencyUseCase = editCurrencyUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 2) {
            sender.sendMessage(F.getCurrencyUsage_Startbal());
            return false;
        }
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        String currencyName = args[0];
        double finalAmount = amount;

        Scheduler.runAsync(ContextualTask.build(() -> {
            try {
                editCurrencyUseCase.editStartBal(currencyName, finalAmount);
                sender.sendMessage(F.getPrefix() + "§7Starting balance for §f" + currencyName + " §7set: §a" + finalAmount);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§cError: §7" + e.getMessage());
            } catch (DecimalNotSupportedException e) {
                sender.sendMessage("no decimal support for " + currencyName);
            }

        }));

        return false;
    }
}
