package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.BukkitImplementation.config.file.Message;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.Economy.domain.persistence.Exceptions.TransactionException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EditSingularNameCommand  implements CommandExecutor {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditSingularNameCommand(EditCurrencyUseCase editCurrencyUseCase) {
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < 2){
            sender.sendMessage("§cUsage: /currency singular <singular> <newSingular>");
            return false;
        }

        String plural = args[0];
        String newPlural = args[1];
        Scheduler.runAsync(ContextualTask.build(() -> {
            try {
                editCurrencyUseCase.setSingularName(plural, newPlural);
                sender.sendMessage("Plural name updated for " + plural + " to " + newPlural);
            }catch (CurrencyNotFoundException e) {
                sender.sendMessage(Message.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage("error while updating the plural name");
            }
        }));
        return false;
    }
}
