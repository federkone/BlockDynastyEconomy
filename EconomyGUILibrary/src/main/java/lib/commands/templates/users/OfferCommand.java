package lib.commands.templates.users;

import lib.commands.abstractions.Command;
import lib.commands.abstractions.IEntityCommands;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class OfferCommand extends AbstractCommand {

    public OfferCommand() {
        super("offer","BlockDynastyEconomy.command.offer");
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        if (args.length == 0) {
            List<Command> subCommands = getSubCommands();
            StringBuilder helpMessage = new StringBuilder("&2&l>>"+this.getName()+" Subcommands:\n");
            for (Command cmd : subCommands) {
                helpMessage.append("- ").append(cmd.getName()).append("\n");
            }
            sender.sendMessage(helpMessage.toString());
            return false;
        }

        Command subCommand = getSubCommands().stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);

        if (subCommand == null) {
            List<Command> subCommands = getSubCommands();
            StringBuilder helpMessage = new StringBuilder("&2&l>>"+this.getName()+" Subcommands:\n");
            for (Command cmd : subCommands) {
                helpMessage.append("- ").append(cmd.getName()).append("\n");
            }
            sender.sendMessage(helpMessage.toString());
            return false;
        }


        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, args.length - 1);

        return subCommand.execute(sender, subCommandArgs);
    }

}
