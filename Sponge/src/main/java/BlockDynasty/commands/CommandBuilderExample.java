package BlockDynasty.commands;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;


public class CommandBuilderExample {

    public Command.Parameterized buildCommand(){
        //simple command
        return Command
                .builder()
                .executor(new HelloWorldCommand())
                .permission("myplugin.command.helloWorld")
                .shortDescription(Component.text("Hello World Command"))
                .build();

        //instance to restrict command to players only
        /*return Command
                .builder()
                .executor(new HelloWorldCommand())
                .permission("myplugin.command.helloWorld")
                .shortDescription(Component.text("Hello World Command"))
                .executionRequirements(context -> context.cause().root() instanceof ServerPlayer)
                .build();*/
    }


    @Inject
    PluginContainer container;

    @Listener
    public void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event){
        event.register(this.container, buildCommand(), "helloworld", "hello", "test");
    }
}
