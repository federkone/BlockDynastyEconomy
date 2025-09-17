package BlockDynasty.BukkitImplementation;

import BlockDynasty.BukkitImplementation.Integrations.bungee.BungeeData;
import BlockDynasty.BukkitImplementation.Integrations.velocity.Velocity;
import BlockDynasty.BukkitImplementation.Integrations.velocity.VelocityData;
import BlockDynasty.BukkitImplementation.adapters.ConfigurationAdapter;
import BlockDynasty.BukkitImplementation.adapters.ConsoleAdapter;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.TextInput;
import BlockDynasty.BukkitImplementation.adapters.GUI.listener.ClickListener;
import BlockDynasty.BukkitImplementation.adapters.GUI.listener.CloseListener;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.PlaceHolder;
import BlockDynasty.BukkitImplementation.Integrations.bungee.Bungee;
import BlockDynasty.BukkitImplementation.Integrations.vault.Vault;

import BlockDynasty.BukkitImplementation.adapters.abstractions.BukkitAdapter;
import BlockDynasty.BukkitImplementation.adapters.commands.CommandRegister;
import BlockDynasty.BukkitImplementation.adapters.listeners.PlayerJoinListenerOffline;
import BlockDynasty.BukkitImplementation.adapters.listeners.PlayerJoinListenerOnline;

import BlockDynasty.BukkitImplementation.logs.VaultLogger;
import BlockDynasty.BukkitImplementation.config.file.ConfigurationFile;
import BlockDynasty.BukkitImplementation.logs.EconomyLogger;
import BlockDynasty.BukkitImplementation.utils.Console;


import Main.Economy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import proxy.ProxyData;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private final Economy economy = new Economy();

    @Override
    public void onLoad() {
        ConfigurationFile.init(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            initCoreServices();
            registerCommands();
            registerEvents();
            setupIntegrations();
            Console.log("§aPlugin enabled successfully!");
        } catch (Exception e) {
            Console.logError("An error occurred during plugin initialization: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        economy.shutdown();
        Vault.unhook();
        PlaceHolder.unregister();
        Bungee.unhook(this);
    }

    private void initCoreServices() {
        int expireCacheTopMinutes = getConfig().getInt("expireCacheTopMinutes", 60);
        Console.setConsole(new ConsoleAdapter());

        ProxyData proxyData=null;
        String proxyMode =BlockDynastyEconomy.getInstance().getConfig().getString("proxy (Velocity/BungeeCord)");
        if(proxyMode != null && proxyMode.equalsIgnoreCase("BungeeCord")) {
            proxyData = new BungeeData();
            Console.log("BungeeCord proxy mode enabled.");
        } else if (proxyMode.equalsIgnoreCase("Velocity")) {
            proxyData = new VelocityData();
            Console.log("Velocity proxy mode enabled.");
        } else {
            Console.log("No proxy mode enabled.");
            throw new RuntimeException("Error initializing proxy mode. Please check the 'proxy' configuration.");
        }
        economy.init(new TextInput(),new ConsoleAdapter(),EconomyLogger.build(this),new BukkitAdapter(),new ConfigurationAdapter(),proxyData);

    }
    private void registerCommands(){
        CommandRegister.registerAll();
    }

    private void registerEvents() {
        Listener economyListener;
        if(getServer().getOnlineMode()){ //get Config().getBoolean("online-mode",true)
            economyListener = new PlayerJoinListenerOnline(economy.getPlayerJoinListener());
            Console.log("Online mode is enabled. The plugin will use UUID to identify players.");
        }else {
            economyListener = new PlayerJoinListenerOffline(economy.getPlayerJoinListener());
            Console.log("Online mode is disabled, The plugin will use NICKNAME to identify players.");
        }

        getServer().getPluginManager().registerEvents(economyListener, this);
        getServer().getPluginManager().registerEvents(new ClickListener(),this);
        getServer().getPluginManager().registerEvents(new CloseListener(),this);

    }
    private void setupIntegrations() {
        Vault.init(economy.getApiWithLog(VaultLogger.build(this)));
        PlaceHolder.register(economy.getPlaceHolder());
        Bungee.init(this,economy.getApi());
        Velocity.init(this,economy.getApi());
    }

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }
}