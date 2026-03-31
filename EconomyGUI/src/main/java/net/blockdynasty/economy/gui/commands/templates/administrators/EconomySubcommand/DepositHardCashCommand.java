package net.blockdynasty.economy.gui.commands.templates.administrators.EconomySubcommand;

import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.deposit.IDepositItemNBTUseCase;
import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;

public class DepositHardCashCommand extends AbstractCommand {
    private final IDepositItemNBTUseCase depositItemUseCase;

    public DepositHardCashCommand() {
        super("depositItem", "");
        this.depositItemUseCase = HardCashUseCaseFactory.getDepositItemNBTUseCase();
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        depositItemUseCase.execute(sender.asEntityGUI().asEntityHardCash());
        return true;
    }
}
