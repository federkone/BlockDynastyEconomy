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

public class SetDefaultCommand implements CommandExecutor {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public SetDefaultCommand(EditCurrencyUseCase editCurrencyUseCase) {
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Message.getCurrencyUsage_Default());
            return false;
        }

        String currencyName =args[0];
        Scheduler.runAsync(ContextualTask.build(() -> {
            try {
                editCurrencyUseCase.setDefaultCurrency(currencyName);
                sender.sendMessage(Message.getPrefix() + "§7Set default currency to §f" + currencyName);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(Message.getUnknownCurrency());

            } catch (TransactionException e) {
                sender.sendMessage(Message.getPrefix() + "§cError while setting default currency");
            }
        }));
        return false;
    }
}
