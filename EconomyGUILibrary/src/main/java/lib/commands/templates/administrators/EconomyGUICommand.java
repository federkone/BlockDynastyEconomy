package lib.commands.templates.administrators;

import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IEntityGUI;

public class EconomyGUICommand extends AbstractCommand {

    public EconomyGUICommand() {
        super("menu", "BlockDynastyEconomy.command.menu");
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        IEntityGUI entityGUI = sender.asEntityGUI();
        if (entityGUI != null) {
            GUIFactory.adminPanel(entityGUI).open();
        }else {
            sender.sendMessage("Economy Admin GUI is only available for players.");
        }


        return true;
    }
}
