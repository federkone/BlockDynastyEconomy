package BlockDynasty;

import BlockDynasty.adapters.abstractions.EntityPlayerAdapter;
import BlockDynasty.adapters.config.ConfigurationAdapter;
import BlockDynasty.adapters.ConsoleAdapter;
import BlockDynasty.adapters.GUI.adapters.TextInput;
import BlockDynasty.adapters.commands.CommandRegister;
import BlockDynasty.adapters.abstractions.SpongeAdapter;
import BlockDynasty.adapters.config.ConfigurationFile;
import BlockDynasty.adapters.proxy.ProxyReceiverImp;
import BlockDynasty.adapters.logs.AbstractLog;
import BlockDynasty.adapters.spongeEconomyApi.EconomyServiceAdapter;
import BlockDynasty.adapters.spongeEconomyApi.MultiCurrencyService;
import BlockDynasty.utils.Console;
import Main.Economy;
import com.google.inject.Inject;
import lib.commands.CommandsFactory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.*;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;
import org.spongepowered.api.network.channel.raw.RawDataChannel;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;
import proxy.ProxyData;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;


@Plugin("blockdynastyeco")

public class SpongePlugin {
    private static PluginContainer container;
    private static Logger logger;
    private static final Economy economy= new Economy();
    public static String databasePath;
    public static Path configPath;
    private static RawDataChannel channel;

    @Inject
    SpongePlugin(final PluginContainer container, final Logger logger,@ConfigDir(sharedRoot = false) final Path configDir) {
        SpongePlugin.container = container;
        SpongePlugin.logger = logger;
        SpongePlugin.configPath = configDir;
        SpongePlugin.databasePath = setupDatabaseDirectory(configDir);
    }

    @Listener
    public void onRegisterChannel(final RegisterChannelEvent event) {
        channel = event.register(ResourceKey.resolve(ProxyData.getChannelName()),RawDataChannel.class);
        ProxyReceiverImp.register().addHandler(channel);
    }

    private String setupDatabaseDirectory(final Path configDir) {
        try {
            Path databasePath = configDir.resolve("database");
            Files.createDirectories(databasePath);
            return databasePath.toAbsolutePath().toString();
        } catch (Exception e) {
            logger.error("Error creating database directory", e);
            return null;
        }
    }

    @Listener
    public void onConstructPlugin(final ConstructPluginEvent event) {
        // Perform any one-time setup
        ConfigurationFile.init( this);
        Console.setConsole(new ConsoleAdapter());
        economy.init(new TextInput(),new ConsoleAdapter(),new AbstractLog(), new SpongeAdapter(),new ConfigurationAdapter());
        Console.log("Plugin constructed...");
    }

    @Listener
    public void provideEconomy(final ProvideServiceEvent.EngineScoped<MultiCurrencyService> event) {
        //info: El servicio de Economia de Sponge solo soporta 1 default Currency, no tiene interface para multiples monedas....
        event.suggest(() -> new EconomyServiceAdapter( economy.getApi() ));
    }

    @Listener
    public void onServerStarting(final StartingEngineEvent<Server> event) {

        Console.log("Server is starting...");
    }

    @Listener
    public void onServerStopping(final StoppingEngineEvent<Server> event) {
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

        if(Sponge.server().isOnlineModeEnabled()){
            economy.getPlayerJoinListener().loadOnlinePlayerAccount(EntityPlayerAdapter.of(player));
        }else{
            economy.getPlayerJoinListener().loadOfflinePlayerAccount(EntityPlayerAdapter.of(player));
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static PluginContainer getPlugin() {
        return container;
    }
    public static RawDataChannel getChannel() {
        return channel;
    }
}

