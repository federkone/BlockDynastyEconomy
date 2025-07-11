package me.BlockDynasty.Economy.Infrastructure.BukkitImplementation;

import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.GUIService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commandsGUI.BalanceGUICommand;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.commandsGUI.PayGUICommand;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.GUI.listeners.GUIListener;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.bungee.Courier;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.bungee.CourierImpl;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.log.Log;
import me.BlockDynasty.Economy.aplication.api.Api;
import me.BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.log.VaultLogger;
import me.BlockDynasty.Economy.Infrastructure.services.OfferService;
import me.BlockDynasty.Economy.Infrastructure.services.AccountService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.api.BlockDynastyEconomyApi;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.commands.CommandRegistration;
import me.BlockDynasty.Economy.domain.persistence.entities.IRepository;
import me.BlockDynasty.Economy.Infrastructure.services.CurrencyService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.Configuration;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.file.MessageService;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.listeners.EconomyListener;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.config.log.EconomyLogger;
import me.BlockDynasty.Economy.Infrastructure.repository.InitDatabase;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.utils.UtilServer;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.vault.VaultHandler;
import me.BlockDynasty.Economy.Infrastructure.BukkitImplementation.Integrations.Placeholder.BlockdynastyEconomyExpansion;
import me.BlockDynasty.Economy.domain.services.IAccountService;
import me.BlockDynasty.Economy.domain.services.ICurrencyService;
import me.BlockDynasty.Economy.domain.services.IOfferService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private static Api api;

    private IRepository repository;
    private IAccountService accountService;
   //private ChequeManager chequeManager;
    private ICurrencyService currencyService;
    private IOfferService offerService;
    private VaultHandler vaultHandler;
    private Log economyLogger;
    private Log vaultLogger;
    private UpdateForwarder updateForwarder;
    private Courier courier;

    private boolean debug = false;
    private boolean vault = true;
    private boolean logging = true;
    private boolean cheques = false;

    private boolean disabling = false;
    MessageService messageService;

    private UsesCaseFactory usesCaseFactory;
    private GUIService guiService;

    @Override
    public void onLoad() {
        Configuration configuration = new Configuration(this);
        configuration.loadDefaultConfig();

        setDebug(getConfig().getBoolean("debug",false));
        setVault(getConfig().getBoolean("vault",true));
        setLogging(getConfig().getBoolean("transaction_log",true));
        //setCheques(getConfig().getBoolean("cheque.enabled",false));
    }

    @Override
    public void onEnable() {
        instance = this;
        try {
            initRepository();
            initCoreServices();
            registerCommands();
            registerEvents();
            registerGUI();
            setupIntegrations();
            UtilServer.consoleLog("Plugin enabled successfully!");
        } catch (Exception e) {
            UtilServer.consoleLog("An error occurred during plugin initialization: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }

        //registrar api
        api = new BlockDynastyEconomyApi(usesCaseFactory);
        getServer().getServicesManager().register(BlockDynastyEconomyApi.class, (BlockDynastyEconomyApi)api, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        disabling = true;
        if (isVault()) getVaultHandler().unhook();
        if (getDataStore() != null) {
            getDataStore().close();
        }
    }

    public void initRepository() {
        UtilServer.consoleLog("§a Initializing data store BlockDynastyEconomy...");
        Result<IRepository> result = InitDatabase.init(this);
        if (result.isSuccess()) {
            repository = result.getValue();
            UtilServer.consoleLog("§a Data store BlockDynastyEconomy initialized successfully.");
        } else {
            UtilServer.consoleLog(result.getErrorMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    private void initCoreServices() {
        this.accountService = new AccountService(getConfig().getInt("expireCacheTopMinutes"));
        this.currencyService = new CurrencyService(getDataStore());
        this.messageService = new MessageService(currencyService);

        this.economyLogger = new EconomyLogger(this);
        this.vaultLogger = new VaultLogger(this);
        this.offerService = new OfferService(this);
        this.courier = new CourierImpl(this);

        this.usesCaseFactory = new UsesCaseFactory(accountService, currencyService, economyLogger, offerService, getDataStore(), courier);
        this.updateForwarder = new UpdateForwarder(this);

        this.economyLogger.save();

        //if (isChequesEnabled()) {
        //    chequeManager = new ChequeManager(this);
        //}
    }
    private void registerCommands(){
        CommandRegistration.registerCommands(this);
    }

    private void registerGUI(){
        this.guiService = new GUIService();
        // Register BalanceGUI command
        this.getCommand("balancegui").setExecutor(new BalanceGUICommand(this));
        this.getCommand("paygui").setExecutor(new PayGUICommand(this, usesCaseFactory.getPayUseCase(), currencyService));

        // Register GUI event listener
        getServer().getPluginManager().registerEvents(
                new GUIListener(getGuiManager()), this
        );
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new EconomyListener( usesCaseFactory.getCreateAccountUseCase(), usesCaseFactory.getAccountsUseCase(), accountService,currencyService), this);
    }
    private void setupIntegrations() {
        // Configuración de Vault
        if (isVault()) {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                vaultHandler = new VaultHandler(this,new UsesCaseFactory(accountService, currencyService,vaultLogger, offerService,getDataStore(),courier));
                vaultHandler.hook();
            }else{
                UtilServer.consoleLog("Vault plugin not found. Vault integration will not be enabled.");
                setVault(false);
            }
        } else {
            UtilServer.consoleLog("Vault integration is disabled.");
        }

        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new BlockdynastyEconomyExpansion(usesCaseFactory.getAccountsUseCase(), usesCaseFactory.getCurrencyUseCase()).register();
            UtilServer.consoleLog("PlaceholderAPI Expansion registered successfully!");
        } else {
            UtilServer.consoleLog("PlaceholderAPI not found. Expansion won't be loaded.");
        }

        // Canal de mensajería
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", updateForwarder);
    }

    public UsesCaseFactory getUsesCase() {
        return  this.usesCaseFactory;
    }

    public IRepository getDataStore() {  //IRepository
        if (repository == null) {
            throw new IllegalStateException("Repository is not initialized yet.");
        }
        return repository;             //return repository;
    }

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }

    public static Api getApi() {
        return api;
    }
    public ICurrencyService getCurrencyManager() {
        return currencyService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }

    public UpdateForwarder getUpdateForwarder() {
        return updateForwarder;
    }


    public boolean isDebug() {
        return debug;
    }

    private void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isVault() {
        return vault;
    }

    private void setVault(boolean vault) {
        this.vault = vault;
    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public boolean isDisabling() {
        return disabling;
    }

    public GUIService getGuiManager() {
        return guiService;
    }

    /*public boolean isChequesEnabled() {
        return cheques;
    }

    public void setCheques(boolean cheques) {
        this.cheques = cheques;
    }*/
}
