package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.DeleteCurrencyUseCase;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
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

        SchedulerUtils.runAsync(() -> {
            try {
                deleteCurrencyUseCase.deleteCurrency(currencyName);
                sender.sendMessage(F.getPrefix() + "§7Deleted currency: §a" + currencyName);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency() + e.getMessage()+" asegurate de tener otra moneda por defecto antes de eliminarla");
            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
            }
        });

        return false;
    }
}
