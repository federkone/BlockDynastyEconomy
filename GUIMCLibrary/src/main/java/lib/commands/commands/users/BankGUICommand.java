package lib.commands.commands.users;

import lib.commands.abstractions.AbstractCommand;
import lib.commands.abstractions.Source;

public class BankGUICommand extends AbstractCommand {

    public BankGUICommand() {
        super("Bank", "BlockDynastyEconomy.command.pay");
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage( "no tienes permisos"); //no tiene permisos para ejecutar comando pagar
            return true;
        }

        //GUIFactory.bankPanel(new PlayerAdapter(player)).open();
        return false;
    }
}
