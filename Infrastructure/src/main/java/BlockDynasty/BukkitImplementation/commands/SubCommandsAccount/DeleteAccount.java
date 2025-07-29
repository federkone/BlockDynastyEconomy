package BlockDynasty.BukkitImplementation.commands.SubCommandsAccount;

import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.result.Result;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteAccount implements CommandExecutor {
    private DeleteAccountUseCase deleteAccountUseCase;

    public DeleteAccount(DeleteAccountUseCase deleteAccountUseCase) {
        this.deleteAccountUseCase = deleteAccountUseCase;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /deleteaccount <playerName>");
            return false;
        }
        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);
        Result<Void> result =deleteAccountUseCase.execute(playerName);
        if (result.isSuccess()) {
            player.kickPlayer("se ah eliminado tu cuenta de economía");
        }
        //kick player
        return true;
    }
}
