package net.blockdynasty.economy.gui.commands.templates.administrators.EconomySubcommand;

import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.withdraw.IExtractItemNBTUseCase;
import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;

import java.math.BigDecimal;
import java.util.List;

public class ExtractHardCashCommand extends AbstractCommand {
    private final IExtractItemNBTUseCase extractItemUseCase;

    public ExtractHardCashCommand() {
        super("extract", "", List.of("currency","amount"));
        this.extractItemUseCase = HardCashUseCaseFactory.getExtractItemNBTUseCase();
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        //extract currency and amount from args
        String currency = args[0];
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount");
            return false;
        }
        extractItemUseCase.execute(sender.asEntityGUI().asEntityHardCash(), BigDecimal.valueOf(amount),currency);
        return true;
    }
}
