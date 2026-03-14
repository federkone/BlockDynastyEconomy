package lib.commands.templates.administrators.EconomySubcommand;

import aplication.useCase.HardCashUseCaseFactory;
import aplication.useCase.nbtItems.IDepositItemNBTUseCase;
import lib.commands.abstractions.AbstractCommand;

public class DepositHardCashCommand extends AbstractCommand {
    private final IDepositItemNBTUseCase depositItemUseCase;

    public DepositHardCashCommand() {
        super("depositItem", "");
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemNBTUseCase();
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
