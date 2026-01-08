package lib.commands.templates.administrators.EconomySubcommand;

import aplication.useCase.DepositItemUseCase;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class DepositHardCashCommand extends AbstractCommand {
    private final DepositItemUseCase depositItemUseCase;

    public DepositHardCashCommand(DepositItemUseCase depositItemUseCase) {
        super("depositItem", "");
        this.depositItemUseCase = depositItemUseCase;
    }

    @Override
    public boolean execute(lib.commands.abstractions.IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        depositItemUseCase.execute(sender.asEntityGUI().asEntityHardCash());
        return true;
    }
}
