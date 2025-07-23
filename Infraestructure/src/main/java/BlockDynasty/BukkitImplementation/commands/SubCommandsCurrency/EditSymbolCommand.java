package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.BukkitImplementation.config.file.F;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EditSymbolCommand implements CommandExecutor {
    private EditCurrencyUseCase editCurrencyUseCase;

    public EditSymbolCommand(EditCurrencyUseCase editCurrencyUseCase) {
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length<2){
            sender.sendMessage(F.getCurrencyUsage_Symbol());
            return true;
        }

        String currencyName = args[0];
        String currencySymbol = args[1];

        SchedulerFactory.runAsync(() -> {
            try {
                editCurrencyUseCase.editSymbol(currencyName, currencySymbol);
                sender.sendMessage(F.getPrefix() + "§7Currency symbol for §f" + currencyName + " §7updated: §a" + currencySymbol);
            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§7error in transaction.");
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            }
        });

        return false;
    }
}
