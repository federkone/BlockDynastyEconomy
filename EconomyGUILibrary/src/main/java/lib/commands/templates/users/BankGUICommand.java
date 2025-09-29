package lib.commands.templates.users;

import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IEntityGUI;

public class BankGUICommand extends AbstractCommand {

    public BankGUICommand() {
        super("bank", "BlockDynastyEconomy.command.bank");
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        IEntityGUI entityGUI = sender.asEntityGUI();
        if (entityGUI != null) {
            GUIFactory.bankPanel(entityGUI).open();
        }else {
            sender.sendMessage("Bank GUI is only available for players.");
        }

        return true;
    }
}
