package lib.commands.templates.administrators;

import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.Source;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IPlayer;

public class EconomyGUICommand extends AbstractCommand {

    public EconomyGUICommand() {
        super("menu", "BlockDynastyEconomy.command.menu");
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        IPlayer player = sender.asIPlayer();
        if (player != null) {
            GUIFactory.adminPanel(player).open();
        }

        return true;
    }
}
