package lib.commands.templates.administrators.EconomySubcommand;

import aplication.useCase.ExtractItemUseCase;
import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.IEntityCommands;

import java.math.BigDecimal;
import java.util.List;

public class ExtractHardCashCommand extends AbstractCommand {
    private final ExtractItemUseCase extractItemUseCase;

    public ExtractHardCashCommand(ExtractItemUseCase extractItemUseCase) {
        super("extract", "", List.of("currency","amount"));
        this.extractItemUseCase = extractItemUseCase;
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
