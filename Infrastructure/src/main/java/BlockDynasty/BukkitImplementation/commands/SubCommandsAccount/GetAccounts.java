package BlockDynasty.BukkitImplementation.commands.SubCommandsAccount;

import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetAccounts implements CommandExecutor {
    private final SearchAccountUseCase searchAccountUseCase;
    public GetAccounts(SearchAccountUseCase searchAccountUseCase) {
        this.searchAccountUseCase = searchAccountUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Result<List<Account>> result = searchAccountUseCase.getOfflineAccounts();
        result.getValue().forEach(System.out::println);
        return true;
    }
}
