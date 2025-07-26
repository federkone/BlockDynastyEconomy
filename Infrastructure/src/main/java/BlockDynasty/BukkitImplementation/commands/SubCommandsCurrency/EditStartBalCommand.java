package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.DecimalNotSupportedException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.BukkitImplementation.config.file.Message;
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
            sender.sendMessage(Message.getCurrencyUsage_Startbal());
            return false;
        }
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(Message.getUnvalidAmount());
            return true;
        }

        String currencyName = args[0];
        double finalAmount = amount;

        Scheduler.runAsync(ContextualTask.build(() -> {
            try {
                editCurrencyUseCase.editStartBal(currencyName, finalAmount);
                sender.sendMessage(Message.getPrefix() + "§7Starting balance for §f" + currencyName + " §7set: §a" + finalAmount);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(Message.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage(Message.getPrefix() + "§cError: §7" + e.getMessage());
            } catch (DecimalNotSupportedException e) {
                sender.sendMessage("no decimal support for " + currencyName);
            }

        }));

        return false;
    }
}
