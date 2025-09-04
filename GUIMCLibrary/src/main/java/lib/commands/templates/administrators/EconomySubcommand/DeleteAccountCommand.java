package lib.commands.templates.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.CommandsFactory;

import java.util.List;

public class DeleteAccountCommand extends AbstractCommand {
    private DeleteAccountUseCase deleteAccountUseCase;

    public DeleteAccountCommand(DeleteAccountUseCase deleteAccountUseCase) {
        super("deleteAccount","BlockDynastyEconomy.command.account", List.of("player"));
        this.deleteAccountUseCase = deleteAccountUseCase;
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage("Usage: /deleteaccount <playerName>");
            return false;
        }
        String playerName = args[0];
        Source player = CommandsFactory.getPlatformAdapter().getPlayer(playerName);
        Result<Void> result =deleteAccountUseCase.execute(playerName);
        if (result.isSuccess()) {
            sender.kickPlayer("se ah eliminado tu cuenta de economía");
        }
        //kick player
        return true;
    }
}
