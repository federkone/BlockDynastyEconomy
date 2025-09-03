package lib.commands.commands.administrators.EconomySubcommand;

import lib.commands.abstractions.Command;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

public class CurrencyCommand extends AbstractCommand {
    public CurrencyCommand() {
        super("currency","BlockDynastyEconomy.command.currency");
        //, List.of("create","delete","color","decimal","payable","plural","rate","singular","startBal","symbol","list","default","view")
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length == 0) {
            //Message.sendCurrencyUsage(sender);
            return true;
        }

        Command subCommand = getSubCommands().stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);
        if (subCommand == null) {
            sender.sendMessage("Unknown subcommand!");
            return false;
        }

        // Extrae los argumentos restantes para pasarlos al subcomando
        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, args.length - 1);

        return subCommand.execute(sender, subCommandArgs);
    }



}
