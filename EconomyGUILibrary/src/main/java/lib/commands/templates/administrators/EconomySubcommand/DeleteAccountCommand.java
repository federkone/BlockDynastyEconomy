package lib.commands.templates.administrators.EconomySubcommand;

import BlockDynasty.Economy.aplication.useCase.account.DeleteAccountUseCase;
import BlockDynasty.Economy.domain.result.Result;
import lib.commands.abstractions.IEntityCommands;
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
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String playerName = args[0];
        IEntityCommands player = CommandsFactory.getPlatformAdapter().getPlayer(playerName);
        Result<Void> result =deleteAccountUseCase.execute(playerName);
        if (result.isSuccess()) {
            sender.kickPlayer("Your account has been deleted by an administrator.");
        }
        return true;
    }
}
