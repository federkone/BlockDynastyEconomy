package lib.commands.templates.users;

import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.Source;
import lib.gui.GUIFactory;
import lib.gui.abstractions.IPlayer;

public class BankGUICommand extends AbstractCommand {

    public BankGUICommand() {
        super("bank", "BlockDynastyEconomy.command.bank");
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage( "no tienes permisos"); //no tiene permisos para ejecutar comando pagar
            return true;
        }

        IPlayer player = sender.asIPlayer();
        if (player != null) {
            GUIFactory.bankPanel(player).open();
        }

        return true;
    }
}
