package BlockDynasty.BukkitImplementation.commands.SubCommandsAccount;

import BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetAccounts implements CommandExecutor {
    private final GetAccountsUseCase getAccountsUseCase;
    public GetAccounts(GetAccountsUseCase getAccountsUseCase) {
        this.getAccountsUseCase = getAccountsUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Result<List<Account>> result = getAccountsUseCase.getOfflineAccounts();
        result.getValue().forEach(System.out::println);
        return true;
    }
}
