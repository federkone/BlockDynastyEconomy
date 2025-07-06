package me.BlockDynasty.Economy.aplication.commands.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.currency.Exceptions.DecimalNotSupportedException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
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

        SchedulerUtils.runAsync(() -> {
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

        });

        return false;
    }
}
