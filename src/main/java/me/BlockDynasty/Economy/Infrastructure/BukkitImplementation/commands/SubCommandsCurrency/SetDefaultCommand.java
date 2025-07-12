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

public class SetDefaultCommand implements CommandExecutor {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public SetDefaultCommand(EditCurrencyUseCase editCurrencyUseCase) {
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            sender.sendMessage(F.getCurrencyUsage_Default());
            return false;
        }

        String currencyName =args[0];
        SchedulerUtils.runAsync(() -> {
            try {
                editCurrencyUseCase.setDefaultCurrency(currencyName);
                sender.sendMessage(F.getPrefix() + "§7Set default currency to §f" + currencyName);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());

            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§cError while setting default currency");
            }
        });
        return false;
    }
}
