package BlockDynasty;

import BlockDynasty.GUI.commands.AdminGUICommand;
import BlockDynasty.GUI.RegisterGuiModule;
import BlockDynasty.GUI.commands.BankGUICommand;
import BlockDynasty.listeners.Courier;
import BlockDynasty.listeners.OfferListener;
import BlockDynasty.logs.AbstractLog;
import BlockDynasty.utils.Console;
import com.google.inject.Inject;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.LinearComponents;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import BlockDynasty.Economy.Core;
import repository.RepositorySql;


//todo need init core
@Plugin("blockdynastyeco")

public class SpongePlugin {
    private static PluginContainer container; // The plugin container
    private static Logger logger; // The logger instance

    @Inject
    SpongePlugin(final PluginContainer container, final Logger logger) {
        this.container = container;
        SpongePlugin.logger = logger;
    }

    @Listener
    public void onConstructPlugin(final ConstructPluginEvent event) {
        // Perform any one-time setup

        Console.log("Constructing test");
    }

    @Listener
    public void onServerStarting(final StartingEngineEvent<Server> event) {
        // Any setup per-game instance. This can run multiple times when
        // using the integrated (singleplayer) server.
        Core core=new Core(new RepositorySql(),5,new OfferListener(),new Courier(),new AbstractLog());
        RegisterGuiModule.register( core.getTransactionsUseCase(), core.getAccountsUseCase(), core.getCurrencyUseCase(), core.getOfferUseCase());
        Console.log(" Server is starting");
    }

    @Listener
    public void onServerStopping(final StoppingEngineEvent<Server> event) {
        // Any tear down per-game instance. This can run multiple times when
        // using the integrated (singleplayer) server.
        Console.log("Server is stopping");
    }

    @Listener
    public void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event) {
        // Register a simple command
        // When possible, all commands should be registered within a command register event
        final Parameter.Value<String> nameParam = Parameter.string().key("name").build();
        event.register(container,
                Command.builder()
                        .addParameter(nameParam)
                        .permission("test.command.greet")
                        .executor(ctx -> {
                            final String name = ctx.requireOne(nameParam);
                            ctx.sendMessage(Identity.nil(),
                                    LinearComponents.linear(NamedTextColor.AQUA, Component.text("Hello "), Component.text(name, Style.style(TextDecoration.BOLD)), Component.text("!")));
                            return CommandResult.success();
                        })
                        .build(), "greet", "wave");

        event.register(container, Command.builder().executor(new BankGUICommand()).build(), "bank");
        event.register(container, Command.builder().executor(new AdminGUICommand()).build(), "admin");
    }


    public static Logger getLogger() {
        return logger;
    }

    public static PluginContainer getPlugin() {
        return container;
    }

}

