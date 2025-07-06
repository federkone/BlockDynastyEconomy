package me.BlockDynasty.Economy.aplication.commands.SubCommandsCurrency;

import me.BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.domain.currency.Exceptions.CurrencyNotFoundException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
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
        SchedulerUtils.runAsync(() -> {
            try {
                editCurrencyUseCase.setSingularName(plural, newPlural);
                sender.sendMessage("Plural name updated for " + plural + " to " + newPlural);
            }catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (TransactionException e) {
                sender.sendMessage("error while updating the plural name");
            }
        });
        return false;
    }
}
