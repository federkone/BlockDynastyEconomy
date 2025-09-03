package lib.commands.commands.administrators;

import lib.commands.abstractions.Command;
import lib.commands.abstractions.Source;
import lib.commands.abstractions.AbstractCommand;

import java.util.List;

public class EconomyCommand extends AbstractCommand {

    public EconomyCommand() {
        super("eco","BlockDynastyEconomy.command.economy",List.of("set","take","give"));
    }

    @Override
    public boolean execute(Source sender, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage("no permission");
            return false;
        }

        if (args.length == 0) {
            //Message.getManageHelp(sender); // Mensaje de ayuda
            sender.sendMessage(" Usa /eco [set,take,give]");
            return false;
        }

        Command subCommand = getSubCommands().stream()
                .filter(cmd -> cmd.getName().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);
        if (subCommand == null) {
            //sender.sendMessage(Message.getUnknownSubCommand());
            sender.sendMessage(" Subcomando desconocido. Usa /eco [set,take,give]");
            return false;
        }

        // Extrae los argumentos restantes para pasarlos al subcomando
        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, args.length - 1);

        return subCommand.execute(sender, subCommandArgs);
    }
}
