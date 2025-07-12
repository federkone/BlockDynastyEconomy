package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
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

        SchedulerUtils.runAsync(() -> {
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
