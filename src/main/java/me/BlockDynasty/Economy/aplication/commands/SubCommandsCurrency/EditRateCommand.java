package me.BlockDynasty.Economy.aplication.commands.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
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
        SchedulerUtils.runAsync(() -> {
            try {
                editCurrencyUseCase.setCurrencyRate(currencyName, finalAmount);
                sender.sendMessage("Rate currency updated for " + currencyName + " to " + rate);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage("error while updating the rate");
            }
        });

        return false;
    }
}
