package BlockDynasty.BukkitImplementation;

import BlockDynasty.BukkitImplementation.GUI.RegisterModule;
import BlockDynasty.BukkitImplementation.Integrations.vault.IVaultHandler;
import BlockDynasty.BukkitImplementation.Integrations.vault.VaultFactory;
import BlockDynasty.BukkitImplementation.Integrations.vaultUnloked.VaultUnlockedHandler;
import BlockDynasty.BukkitImplementation.listeners.EconomyListenerOffline;
import BlockDynasty.BukkitImplementation.utils.JavaUtil;
import BlockDynasty.Economy.aplication.api.Api;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.BukkitImplementation.Integrations.bungee.CourierImpl;
import BlockDynasty.Economy.domain.services.log.Log;
import BlockDynasty.BukkitImplementation.listeners.OfferListenerImpl;
import BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.BukkitImplementation.config.log.VaultLogger;
import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.aplication.api.BlockDynastyEconomyApi;
import BlockDynasty.BukkitImplementation.Integrations.bungee.UpdateForwarder;
import BlockDynasty.BukkitImplementation.commands.CommandRegister;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.BukkitImplementation.config.file.Configuration;
import BlockDynasty.BukkitImplementation.config.file.MessageService;
import BlockDynasty.BukkitImplementation.listeners.EconomyListenerOnline;
import BlockDynasty.BukkitImplementation.config.log.EconomyLogger;
import BlockDynasty.repository.InitDatabase;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.BukkitImplementation.Integrations.vault.VaultHandler;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.BlockdynastyEconomyExpansion;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private static Api api;

    //private ChequeManager chequeManager;
    private IRepository repository;
    private IAccountService accountService;
    private ICurrencyService currencyService;
    private MessageService messageService;
    private IOfferService offerService;

    private Log economyLogger;
    private Log vaultLogger;
    private UpdateForwarder updateForwarder;
    private Courier courier;

    private boolean debug = false;
    private boolean vault = true;
    private boolean logging = true;
    private boolean cheques = false;
    private boolean disabling = false;

    private UsesCaseFactory usesCaseFactory;

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
        getServer().getServicesManager().register(Api.class, api, this, ServicePriority.Low);
    }

    @Override
    public void onDisable() {
        disabling = true;
        if(VaultFactory.getVaultHandler() != null) getVaultHandler().unhook();
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
        this.offerService = new OfferService(new OfferListenerImpl());
        this.courier = new CourierImpl(this);

        this.usesCaseFactory = new UsesCaseFactory(accountService, currencyService, economyLogger, offerService, getDataStore(), courier);
        this.updateForwarder = new UpdateForwarder(this);

        this.economyLogger.save();

        //if (isChequesEnabled()) {
        //    chequeManager = new ChequeManager(this);
        //}
    }
    private void registerCommands(){
        CommandRegister.registerCommands(this);
    }
    private void registerGUI(){
        RegisterModule.register(this,
                usesCaseFactory.getPayUseCase(),
                usesCaseFactory.getCreateCurrencyUseCase(),
                usesCaseFactory.getGetBalanceUseCase(),
                usesCaseFactory.getCurrencyUseCase(),
                usesCaseFactory.getEditCurrencyUseCase(),
                usesCaseFactory.deleteCurrencyUseCase(),
                messageService);
    }
    private void registerEvents() {
        if(getServer().getOnlineMode()){
            getServer().getPluginManager().registerEvents(new EconomyListenerOnline( usesCaseFactory.getCreateAccountUseCase(), usesCaseFactory.getAccountsUseCase(), accountService,currencyService), this);
        }else {
            getServer().getPluginManager().registerEvents(new EconomyListenerOffline( usesCaseFactory.getCreateAccountUseCase(), usesCaseFactory.getAccountsUseCase(), accountService,currencyService), this);
        }
    }
    private void setupIntegrations() {
        // Configuración de Vault
        VaultFactory.init(isVault(),new UsesCaseFactory(accountService, currencyService,vaultLogger, offerService,getDataStore(),courier));

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

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }
    public static Api getApi() {
        return api;
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
    public ICurrencyService getCurrencyManager() {
        return currencyService;
    }
    public MessageService getMessageService() {
        return messageService;
    }
    public IVaultHandler getVaultHandler() {
        return VaultFactory.getVaultHandler();
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

    /*public boolean isChequesEnabled() {
        return cheques;
    }

    public void setCheques(boolean cheques) {
        this.cheques = cheques;
    }*/
}
