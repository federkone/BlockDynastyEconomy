package BlockDynasty.commands;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class HelloWorldCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandContext context) throws CommandException{
        //ServerPlayer player = (ServerPlayer) context.cause().root(); //this is correct??
        //context.sendMessage(player.identity(),Component.text("Hello World!"));
        context.sendMessage(Component.text("Hello World!"));
        return CommandResult.success();
    }

}
