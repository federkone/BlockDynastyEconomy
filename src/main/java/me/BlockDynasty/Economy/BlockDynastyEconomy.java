
package me.BlockDynasty.Economy;

import me.BlockDynasty.Economy.GUI.GUIManager;
import me.BlockDynasty.Economy.GUI.commandsGUI.BalanceGUICommand;
import me.BlockDynasty.Economy.GUI.listeners.GUIListener;
import me.BlockDynasty.Economy.domain.result.Result;
import me.BlockDynasty.Economy.aplication.useCase.UsesCase;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.config.logging.VaultLogger;
import me.BlockDynasty.Economy.domain.Offers.OfferManager;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.aplication.api.BlockDynastyEconomyAPI;
import me.BlockDynasty.Integrations.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.aplication.commands.CommandRegistration;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.config.file.Configuration;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.aplication.listeners.EconomyListener;
import me.BlockDynasty.Economy.config.logging.EconomyLogger;
import me.BlockDynasty.Economy.domain.repository.InitDatabase;
import me.BlockDynasty.Economy.utils.UtilServer;
import me.BlockDynasty.Integrations.vault.VaultHandler;
import me.BlockDynasty.Integrations.Placeholder.BlockdynastyEconomyExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private static BlockDynastyEconomyAPI api;

    private IRepository repository;
    private AccountCache accountCache;
   //private ChequeManager chequeManager;
    private CurrencyCache currencyCache;
    private VaultHandler vaultHandler;
    private AbstractLogger economyLogger;
    private AbstractLogger vaultLogger;
    private UpdateForwarder updateForwarder;
    private OfferManager offerManager;

    private boolean debug = false;
    private boolean vault = true;
    private boolean logging = true;
    private boolean cheques = false;

    private boolean disabling = false;
    MessageService messageService;

    private UsesCase usesCase;
    private GUIManager guiManager;

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
            //registerGUI();
            setupIntegrations();
            UtilServer.consoleLog("Plugin enabled successfully!");  //utilServer.consoleLog("Plugin enabled successfully!");
        } catch (Exception e) {
            UtilServer.consoleLog("An error occurred during plugin initialization: " + e.getMessage()); //utilServer.consoleLog("An error occurred during plugin initialization: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        //registrar api
        api = new BlockDynastyEconomyAPI(usesCase);
        getServer().getServicesManager().register(BlockDynastyEconomyAPI.class, api, this, ServicePriority.Normal);
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
        Result<IRepository> result = InitDatabase.init(getConfig());
        if (result.isSuccess()) {
            repository = result.getValue();
            UtilServer.consoleLog("§a Data store BlockDynastyEconomy initialized successfully.");
        } else {
            UtilServer.consoleLog(result.getErrorMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    private void initCoreServices() {
        accountCache = new AccountCache(getConfig().getInt("expireCacheTopMinutes"));
        currencyCache = new CurrencyCache(getDataStore());
        messageService = new MessageService(currencyCache);

        economyLogger = new EconomyLogger(this);
        vaultLogger = new VaultLogger(this);
        offerManager = new OfferManager(this);
        updateForwarder = new UpdateForwarder(this);

        this.usesCase = new UsesCase(accountCache, currencyCache, economyLogger, offerManager, getDataStore(), updateForwarder);
        updateForwarder.setGetAccountUseCase(this.usesCase.getAccountsUseCase());

        economyLogger.save();

        //if (isChequesEnabled()) {
        //    chequeManager = new ChequeManager(this);
        //}
    }
    private void registerCommands(){
        CommandRegistration.registerCommands(this);
    }

    private void registerGUI(){
        this.guiManager = new GUIManager();
        // Register BalanceGUI command
        this.getCommand("balancegui").setExecutor(new BalanceGUICommand(this));

        // Register GUI event listener
        getServer().getPluginManager().registerEvents(
                new GUIListener(getGuiManager()), this
        );
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new EconomyListener(this,usesCase.getCreateAccountUseCase(),usesCase.getAccountsUseCase(), accountCache), this);
    }
    private void setupIntegrations() {
        // Configuración de Vault
        if (isVault()) {
            if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
                vaultHandler = new VaultHandler(this,new UsesCase(accountCache,currencyCache,vaultLogger,offerManager,getDataStore(),updateForwarder));
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
            new BlockdynastyEconomyExpansion(usesCase.getAccountsUseCase(),usesCase.getCurrencyUseCase()).register();
            UtilServer.consoleLog("PlaceholderAPI Expansion registered successfully!");
        } else {
            UtilServer.consoleLog("PlaceholderAPI not found. Expansion won't be loaded.");
        }

        // Canal de mensajería
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", updateForwarder);
    }

    public UsesCase getUsesCase() {
        return  this.usesCase;
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
    public static BlockDynastyEconomyAPI getApi() {
        return api;
    }
    public CurrencyCache getCurrencyManager() {
        return currencyCache;
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

    public GUIManager getGuiManager() {
        return guiManager;
    }

    /*public boolean isChequesEnabled() {
        return cheques;
    }

    public void setCheques(boolean cheques) {
        this.cheques = cheques;
    }*/
}
