package BlockDynasty.commands;

import lib.commands.abstractions.PlatformAdapter;
import lib.commands.abstractions.Source;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.exception.CommandException;

public class SpongeAdapter implements PlatformAdapter {


    @Override
    public Source getPlayer(String name) {
        return Sponge.server().player(name).map(SourceAdapter::new).orElse(null);
    }

    @Override
    public void dispatchCommand(String command) {
        try{
            Sponge.server().commandManager().process(command);
        }catch (CommandException e){
            e.printStackTrace();
        }

    }
}
