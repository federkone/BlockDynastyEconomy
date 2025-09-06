package BlockDynasty;

import BlockDynasty.Economy.domain.entities.account.Account;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.GUI.adapters.PlatformAdapter;
import BlockDynasty.GUI.adapters.TextInput;
import BlockDynasty.commands.CommandRegister;
import BlockDynasty.commands.SpongeAdapter;
import BlockDynasty.listeners.Courier;
import BlockDynasty.listeners.OfferListener;
import BlockDynasty.logs.AbstractLog;
import BlockDynasty.utils.Console;
import com.google.inject.Inject;
import lib.commands.CommandsFactory;

import lib.gui.GUIFactory;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import BlockDynasty.Economy.Core;
import repository.RepositorySql;


@Plugin("blockdynastyeco")

public class SpongePlugin {
    private static PluginContainer container;
    private static Logger logger;
    private static Core core;

    @Inject
    SpongePlugin(final PluginContainer container, final Logger logger) {
        SpongePlugin.container = container;
        SpongePlugin.logger = logger;
    }

    @Listener
    public void onConstructPlugin(final ConstructPluginEvent event) {
        // Perform any one-time setup
        core=new Core(new RepositorySql(),5,new OfferListener(),new Courier(),new AbstractLog());
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

        CommandRegister.registerCommands(event, container, CommandsFactory.Commands.getMainCommands());

        Console.log("Registered commands...");
    }

    @Listener
    public void onPlayerJoin(final ServerSideConnectionEvent.Join event) {
        ServerPlayer player = event.player();

        Result<Account> result = core.getAccountsUseCase().getGetAccountsUseCase().getAccount(player.uniqueId());
        if (result.isSuccess()) {
            Result<Void> resultChangeName = core.getServicesManager().getAccountService().checkNameChange(result.getValue(), player.name());
            if(!resultChangeName.isSuccess()){
                player.kick(Component.text("Error al cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador."));
                return;
            }
            core.getServicesManager().getAccountService().addAccountToOnline(result.getValue());
            return;
        }

        Result<Account> creationResult = core.getAccountsUseCase().getCreateAccountUseCase().execute(player.uniqueId(), player.name());
        if (!creationResult.isSuccess()) {
            player.kick(Component.text("Error al crear o cargar tu cuenta de economía. Por favor, vuelve a ingresar o contacta a un administrador."));
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static PluginContainer getPlugin() {
        return container;
    }
}

