package lib.commands.templates.users;

import lib.commands.abstractions.Command;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class OfferCommand extends AbstractCommand {

    public OfferCommand() {
        super("offer","BlockDynastyEconomy.command.offer", List.of(""));
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length == 0) {
            //F.sendCurrencyUsage(sender); //offer accetpt <player> //offer deny <player> //offer cancel <player>
            sender.sendMessage("Usage: /offer <create,accept,deny,cancel>");
            return false;
        }

        Command subCommand = getSubCommands().stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);
        if (subCommand == null) {
            sender.sendMessage("Unknown subcommand.");
            return false;
        }

        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, args.length - 1);

        return subCommand.execute(sender, subCommandArgs);
    }

}
