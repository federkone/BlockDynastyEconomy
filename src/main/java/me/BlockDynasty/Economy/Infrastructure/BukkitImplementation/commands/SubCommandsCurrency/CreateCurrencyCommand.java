package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyAlreadyExist;
import me.BlockDynasty.Economy.Infrastructure.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.F;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class CreateCurrencyCommand implements CommandExecutor {
    private final CreateCurrencyUseCase createCurrencyUseCase;

    public CreateCurrencyCommand(CreateCurrencyUseCase createCurrencyUseCase) {
        this.createCurrencyUseCase = createCurrencyUseCase;

    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(F.getCurrencyUsage_Create());
            return true;
        }

        String single = args[0];
        String plural = args[1];

        SchedulerUtils.runAsync(() -> {
            try {
                createCurrencyUseCase.createCurrency(single, plural);
                sender.sendMessage(F.getPrefix() + "§7Created currency: §a" + single);
            } catch (CurrencyAlreadyExist e) {
                sender.sendMessage(F.getPrefix() + "§cCurrency Already Exist.");
            } catch (TransactionException e) {
                sender.sendMessage(F.getPrefix() + "§cAn error occurred while creating the currency.");
            }


        });

        return false;
    }
}
