package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
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

public class EditRateCommand implements CommandExecutor {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditRateCommand(EditCurrencyUseCase editCurrencyUseCase) {
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length < 2){
            sender.sendMessage(F.getCurrencyUsage_Rate());
            return false;
        }

        String currencyName = args[0];
        String rate = args[1];

        double amount;
        try {
            amount = Double.parseDouble(rate);
            if (amount <= 0.0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            sender.sendMessage(F.getUnvalidAmount());
            return true;
        }

        double finalAmount = amount;
        SchedulerFactory.runAsync(new ContextualTask(() -> {
            try {
                editCurrencyUseCase.setCurrencyRate(currencyName, finalAmount);
                sender.sendMessage("Rate currency updated for " + currencyName + " to " + rate);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage("error while updating the rate");
            }
        }));

        return false;
    }
}
