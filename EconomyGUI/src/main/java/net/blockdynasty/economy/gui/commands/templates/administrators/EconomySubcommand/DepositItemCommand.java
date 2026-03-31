package net.blockdynasty.economy.gui.commands.templates.administrators.EconomySubcommand;

import net.blockdynasty.economy.hardcash.aplication.useCase.HardCashUseCaseFactory;
import net.blockdynasty.economy.hardcash.aplication.useCase.notes.give.IGiveItemNBTUseCase;
import net.blockdynasty.economy.gui.commands.PlatformCommand;
import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.libs.services.messages.MessageService;

import java.math.BigDecimal;
import java.util.List;

public class DepositItemCommand extends AbstractCommand {
    private IGiveItemNBTUseCase giveItemUseCase;
    private PlatformCommand platformCommand;

    public DepositItemCommand() {
        super("giveitem", "", List.of("player", "amount", "currency"));
        this.giveItemUseCase = HardCashUseCaseFactory.getGiveItemNBTUseCase();
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        String target = args[0];
        String montoString= args[1];
        String currencyName = args[2];

        double amount=0;
        try {
            amount = Double.parseDouble(montoString);
        } catch (NumberFormatException e) {
            sender.sendMessage(MessageService.getMessage("invalidamount"));
            return false;
        }
        double finalMount = amount;

        boolean result = giveItemUseCase.execute(target, BigDecimal.valueOf(finalMount), currencyName);
        if (!result) {
            sender.sendMessage("Give item failed. Check if the player exists and has enough inventory space.");
            return false;
        }
        sender.sendMessage("Successfully gave " + finalMount + " " + currencyName + " to " + target + " in items.");
        return true;
    }
}
