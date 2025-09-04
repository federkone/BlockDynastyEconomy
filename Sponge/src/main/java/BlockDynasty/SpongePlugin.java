package BlockDynasty;

import BlockDynasty.GUI.adapters.PlatformAdapter;
import BlockDynasty.GUI.adapters.TextInput;
import BlockDynasty.commands.CommandRegistry;
import BlockDynasty.commands.SpongeAdapter;
import BlockDynasty.listeners.Courier;
import BlockDynasty.listeners.OfferListener;
import BlockDynasty.logs.AbstractLog;
import BlockDynasty.utils.Console;
import com.google.inject.Inject;
import lib.commands.CommandsFactory;

import lib.gui.GUIFactory;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
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
        SpongePlugin.container = container;
        SpongePlugin.logger = logger;
    }

    @Listener
    public void onConstructPlugin(final ConstructPluginEvent event) {
        // Perform any one-time setup
        Core core=new Core(new RepositorySql(),5,new OfferListener(),new Courier(),new AbstractLog());
        CommandsFactory.init(core.getTransactionsUseCase(), core.getOfferUseCase(),core.getCurrencyUseCase(), core.getAccountsUseCase(),new SpongeAdapter());
        GUIFactory.init(core.getCurrencyUseCase(), core.getAccountsUseCase(), core.getTransactionsUseCase(),core.getOfferUseCase(),new TextInput(), new PlatformAdapter());
        Console.log("Plugin constructed...");
    }

    @Listener
    public void onServerStarting(final StartingEngineEvent<Server> event) {
        // Any setup per-game instance. This can run multiple times when
        // using the integrated (singleplayer) server.

        Console.log("Server is starting...");
    }

    @Listener
    public void onServerStopping(final StoppingEngineEvent<Server> event) {
        // Any tear down per-game instance. This can run multiple times when
        // using the integrated (singleplayer) server.

        Console.log("Server is stopping...");
    }

    @Listener
    public void onRegisterCommands(final RegisterCommandEvent<Command.Parameterized> event) {

        CommandRegistry.registerCommands(event, container, CommandsFactory.Commands.getMainCommands());

        Console.log("Registered commands...");
    }

    public static Logger getLogger() {
        return logger;
    }

    public static PluginContainer getPlugin() {
        return container;
    }
}

