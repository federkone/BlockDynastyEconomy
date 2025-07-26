package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyAlreadyExist;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import BlockDynasty.BukkitImplementation.config.file.Message;
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
            sender.sendMessage(Message.getCurrencyUsage_Create());
            return true;
        }

        String single = args[0];
        String plural = args[1];

        Scheduler.runAsync(ContextualTask.build(() -> {
            try {
                createCurrencyUseCase.createCurrency(single, plural);
                sender.sendMessage(Message.getPrefix() + "§7Created currency: §a" + single);
            } catch (CurrencyAlreadyExist e) {
                sender.sendMessage(Message.getPrefix() + "§cCurrency Already Exist.");
            } catch (TransactionException e) {
                sender.sendMessage(Message.getPrefix() + "§cAn error occurred while creating the currency.");
            }
        }));

        return false;
    }
}
