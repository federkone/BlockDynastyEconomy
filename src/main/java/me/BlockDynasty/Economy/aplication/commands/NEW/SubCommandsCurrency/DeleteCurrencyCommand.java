package me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DeleteCurrencyCommand implements CommandExecutor {
    private final CreateCurrencyUseCase createCurrencyUseCase;

    public DeleteCurrencyCommand(CreateCurrencyUseCase createCurrencyUseCase) {
        this.createCurrencyUseCase = createCurrencyUseCase;
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
                createCurrencyUseCase.deleteCurrency(currencyName);
                sender.sendMessage(F.getPrefix() + "§7Deleted currency: §a" + currencyName);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency() + e.getMessage());
            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§cError while deleting currency: §4" + e.getMessage());
            }
        });

        return false;
    }
}
