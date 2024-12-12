package me.BlockDynasty.Economy.aplication.commands.NEW;

import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.domain.account.Exceptions.AccountNotFoundException;
import me.BlockDynasty.Economy.domain.currency.CachedTopListEntry;
import me.BlockDynasty.Economy.domain.repository.Exceptions.RepositoryNotSupportTopException;
import me.BlockDynasty.Economy.domain.repository.Exceptions.TransactionException;
import me.BlockDynasty.Economy.config.file.F;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.utils.SchedulerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;


//TODO PENSAR SI USAR CACHE O NO. QUIZAS NO ES NECESARIO
public class BalanceTopCommand implements CommandExecutor {
    private final GetAccountsUseCase getAccountsUseCase;
    private final MessageService messageService;

    public BalanceTopCommand(GetAccountsUseCase getAccountsUseCase, MessageService messageService) {
        this.getAccountsUseCase = getAccountsUseCase;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("gemseconomy.command.baltop")) {
            sender.sendMessage(F.getNoPerms());
            return true;
        }

        String nameCurrency = args.length > 0 ? args[0] : "default";
        LinkedList<CachedTopListEntry> cache = null;
        SchedulerUtils.runAsync(() -> {
            try {
                //cache = getAccountsUseCase.getTopAccounts(nameCurrency);

                sender.sendMessage(messageService.getBalanceTopMessaje(cache));
            } catch (TransactionException e) {
                sender.sendMessage("Error en la consulta");
            } catch (RepositoryNotSupportTopException e) {
                sender.sendMessage("No support top");
            } catch (AccountNotFoundException e) {
                sender.sendMessage(F.getBalanceTopEmpty()); //cuentas no encotradas para moneda
            }catch (Exception e){
                sender.sendMessage("Error inesperado");
                e.printStackTrace();
            }
        });

        return false;
    }
}
