package BlockDynasty.GUI.commands;

import BlockDynasty.GUI.adapters.SpongePlayer;
import lib.GUIFactory;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class BankGUICommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandContext context) throws CommandException {
        ServerPlayer player = context.cause().first(ServerPlayer.class)
                .orElseThrow(() -> new CommandException(Component.text("Este comando solo puede ser usado por un jugador")));

        GUIFactory.bankPanel(new SpongePlayer(player)).open();
        //player.playSound(player.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);

        return CommandResult.success();
    }


}
