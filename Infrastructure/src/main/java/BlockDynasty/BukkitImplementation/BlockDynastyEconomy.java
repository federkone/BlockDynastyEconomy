package BlockDynasty.BukkitImplementation;

import BlockDynasty.BukkitImplementation.GUI.RegisterModule;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.PlaceHolder;
import BlockDynasty.BukkitImplementation.Integrations.bungee.Bungee;
import BlockDynasty.BukkitImplementation.Integrations.vault.Vault;
import BlockDynasty.BukkitImplementation.Integrations.bungee.CourierImpl;

import BlockDynasty.BukkitImplementation.commands.BukkitAdapter;
import BlockDynasty.BukkitImplementation.commands.CommandRegister;
import BlockDynasty.BukkitImplementation.listeners.EconomyListenerOffline;
import BlockDynasty.BukkitImplementation.listeners.EconomyListenerOnline;
import BlockDynasty.BukkitImplementation.listeners.OfferListenerImpl;
import BlockDynasty.BukkitImplementation.listeners.TransactionsListener;

import BlockDynasty.BukkitImplementation.logs.VaultLogger;
import BlockDynasty.BukkitImplementation.config.file.ConfigurationFile;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.BukkitImplementation.logs.EconomyLogger;
import BlockDynasty.BukkitImplementation.utils.Console;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.aplication.api.Api;
import BlockDynasty.Economy.aplication.api.BlockDynastyEconomyApi;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import BlockDynasty.repository.InitDatabase;

import lib.commands.commands.CommandsFactory;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private Api api;
    private Core core;
    private MessageService messageService;
    private IRepository repository;

    @Override
    public void onLoad() {
        ConfigurationFile.init(this);
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            initRepository();
            initCoreServices();
            registerCommands();
            registerGUI();
            registerEvents();
            setupIntegrations();

            api = new BlockDynastyEconomyApi(core.getAccountsUseCase(),core.getCurrencyUseCase(),core.getTransactionsUseCase());
            getServer().getServicesManager().register(Api.class, api, this, ServicePriority.Low);

            Console.log("§aPlugin enabled successfully!");
        } catch (Exception e) {
            Console.logError("An error occurred during plugin initialization: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (getDataStore() != null) getDataStore().close();
        Vault.unhook();
        PlaceHolder.unregister();
        Bungee.unhook(this);
    }

    private void initRepository() {
        Console.log("Initializing data store BlockDynastyEconomy...");
        Result<IRepository> result = InitDatabase.init(this);
        if (result.isSuccess()) {
            repository = result.getValue();
            Console.log("Data store BlockDynastyEconomy initialized successfully.");
        } else {
            Console.logError(result.getErrorMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    private void initCoreServices() {
        int expireCacheTopMinutes = getConfig().getInt("expireCacheTopMinutes", 60);
        this.core = new Core( getDataStore(),expireCacheTopMinutes,new OfferListenerImpl(),new CourierImpl(this),EconomyLogger.build(this)); //init core application
        this.messageService = new MessageService(core.getServicesManager().getCurrencyService());
    }
    private void registerCommands(){
        //CommandRegister.registerCommands(this, core.getAccountsUseCase(),core.getCurrencyUseCase(),core.getTransactionsUseCase(),core.getOfferUseCase());
        CommandsFactory.init(core.getTransactionsUseCase(),core.getOfferUseCase(),core.getCurrencyUseCase(),core.getAccountsUseCase(),new BukkitAdapter());
        CommandRegister.registerAll();
    }
    private void registerGUI(){
        RegisterModule.register(
                core.getTransactionsUseCase(),
                core.getAccountsUseCase(),
                core.getCurrencyUseCase(),
                core.getOfferUseCase(),
                messageService);
    }
    private void registerEvents() {
        Listener economyListener;
        if(getServer().getOnlineMode()){ //get Config().getBoolean("online-mode",true)
            economyListener = new EconomyListenerOnline( core.getAccountsUseCase().getCreateAccountUseCase(), core.getAccountsUseCase().getGetAccountsUseCase(), core.getServicesManager().getAccountService(), core.getServicesManager().getCurrencyService());
            Console.log("Online mode is enabled. The plugin will use UUID to identify players.");
        }else {
            economyListener = new EconomyListenerOffline( core.getAccountsUseCase().getCreateAccountUseCase(), core.getAccountsUseCase().getGetAccountsUseCase(), core.getServicesManager().getAccountService(), core.getServicesManager().getCurrencyService());
            Console.log("Online mode is disabled, The plugin will use NICKNAME to identify players.");
        }

        getServer().getPluginManager().registerEvents(economyListener, this);
        getServer().getPluginManager().registerEvents(RegisterModule.guiListener(),this);
        TransactionsListener.register(core.getServicesManager().getEventManager(), messageService);
    }
    private void setupIntegrations() {
        Vault.init( core.getAccountsUseCase(),core.getCurrencyUseCase(),core.getTransactionsUseCase(new VaultLogger( this)));
        PlaceHolder.register(core.getAccountsUseCase().getGetAccountsUseCase(), core.getCurrencyUseCase().getGetCurrencyUseCase());
        Bungee.init(this,core.getServicesManager().getAccountService());
    }

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }
    public Api getApi() {
        return this.api;
    }
    private IRepository getDataStore() {
        if (repository == null) {
            throw new IllegalStateException("Repository is not initialized yet.");
        }
        return repository;
    }
    public MessageService getMessageService() {
        return messageService;
    }
}