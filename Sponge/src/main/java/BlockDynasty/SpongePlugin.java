package BlockDynasty;

import BlockDynasty.adapters.ConfigurationAdapter;
import BlockDynasty.adapters.ConsoleAdapter;
import BlockDynasty.adapters.GUI.adapters.TextInput;
import BlockDynasty.adapters.commands.CommandRegister;
import BlockDynasty.adapters.SpongeAdapter;
import BlockDynasty.adapters.commands.SourceAdapter;
import BlockDynasty.adapters.config.ConfigurationFile;
import BlockDynasty.adapters.listeners.Courier;
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
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

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

    @Inject
    SpongePlugin(final PluginContainer container, final Logger logger,@ConfigDir(sharedRoot = false) final Path configDir) {
        SpongePlugin.container = container;
        SpongePlugin.logger = logger;
        SpongePlugin.configPath = configDir;
        SpongePlugin.databasePath = setupDatabaseDirectory(configDir);
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
        economy.init(new TextInput(),new ConsoleAdapter(),new AbstractLog(), new SpongeAdapter(),new ConfigurationAdapter(),new Courier() );
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

        // Register a test command for economy
        Command.Parameterized ecoTestCommand = Command.builder()
                .permission("blockdynasty.ecotester")
                .executor(context -> {
                    if (context.cause().root() instanceof ServerPlayer) {
                        ServerPlayer player = (ServerPlayer) context.cause().root();
                        // Get the economy service
                        Sponge.server().serviceProvider().economyService().ifPresent(economyService -> {
                            // Test account existence
                            var account = economyService.findOrCreateAccount(player.uniqueId());
                            if (account.isPresent()) {
                                player.sendMessage(Component.text("---------------------------------").color(NamedTextColor.GOLD));
                                player.sendMessage(Component.text("Account found: " + account.get().balance(economyService.defaultCurrency())));

                                // Test deposit
                                var result = account.get().deposit(
                                        economyService.defaultCurrency(),
                                        BigDecimal.valueOf(100),
                                        Cause.of(EventContext.empty(), container)
                                );
                                player.sendMessage(Component.text("Deposit result: " + result.result().name()));

                                // Show new balance
                                player.sendMessage(Component.text("New balance: " + account.get().balance(economyService.defaultCurrency())));

                                var resultWithdraw = account.get().withdraw(
                                        economyService.defaultCurrency(),
                                        BigDecimal.valueOf(50),
                                        Cause.of(EventContext.empty(), container)
                                );
                                player.sendMessage(Component.text("Withdraw result: " + resultWithdraw.result().name()));
                                player.sendMessage(Component.text("New balance: " + account.get().balance(economyService.defaultCurrency())));

                                var accountCris = economyService.findOrCreateAccount("Cris");

                                var resultTransfer = account.get().transfer(
                                        accountCris.get(),
                                        economyService.defaultCurrency(),
                                        BigDecimal.valueOf(1),
                                        Cause.of(EventContext.empty(), container)
                                );

                                player.sendMessage(Component.text("Transfer result: " + resultTransfer.result().name()));
                                player.sendMessage(Component.text("Your new balance: " + account.get().balance(economyService.defaultCurrency())));
                                player.sendMessage(Component.text("Cris's new balance: " + accountCris.get().balance(economyService.defaultCurrency())));
                                player.sendMessage(Component.text("---------------------------------").color(NamedTextColor.GOLD));

                            } else {
                                player.sendMessage(Component.text("Failed to find or create account!").color(NamedTextColor.RED));
                            }
                        });
                        return CommandResult.success();
                    }
                    return CommandResult.error(Component.text("Only players can use this command"));
                })
                .build();

        event.register(container, ecoTestCommand, "ecotest");

        Console.log("Registered commands...");
    }

    @Listener
    public void onPlayerJoin(final ServerSideConnectionEvent.Join event) {
        ServerPlayer player = event.player();

        if(Sponge.server().isOnlineModeEnabled()){
            economy.getPlayerJoinListener().loadOnlinePlayerAccount(new SourceAdapter(player));
        }else{
            economy.getPlayerJoinListener().loadOfflinePlayerAccount(new SourceAdapter(player));
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static PluginContainer getPlugin() {
        return container;
    }
}

