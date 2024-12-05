package me.BlockDynasty.Economy.aplication.commands.NEW.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.ToggleFeaturesUseCase;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EditDecimalsCommand implements CommandExecutor {
    public final ToggleFeaturesUseCase toggleFeaturesUseCase;

    public EditDecimalsCommand(ToggleFeaturesUseCase toggleFeaturesUseCase) {
        this.toggleFeaturesUseCase = toggleFeaturesUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length <1) {
            sender.sendMessage(F.getCurrencyUsage_Decimals());
        }

        String currencyName = args[0];

        SchedulerUtils.runAsync(() -> {
            try {
                toggleFeaturesUseCase.toggleDecimals(currencyName);
                sender.sendMessage(F.getPrefix() + "§7Toggled Decimal Support for §f" + currencyName);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§cError toggling decimals for currency §f" + currencyName);
            }
        });
        return false;
    }
}
