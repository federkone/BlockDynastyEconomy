package BlockDynasty.BukkitImplementation;

import BlockDynasty.BukkitImplementation.GUI.RegisterModule;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.PlaceHolder;
import BlockDynasty.BukkitImplementation.Integrations.bungee.Bungee;
import BlockDynasty.BukkitImplementation.Integrations.vault.IVaultHandler;
import BlockDynasty.BukkitImplementation.Integrations.vault.VaultFactory;
import BlockDynasty.BukkitImplementation.listeners.EconomyListenerOffline;
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
import BlockDynasty.BukkitImplementation.commands.CommandRegister;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.BukkitImplementation.config.file.Configuration;
import BlockDynasty.BukkitImplementation.config.file.MessageService;
import BlockDynasty.BukkitImplementation.listeners.EconomyListenerOnline;
import BlockDynasty.BukkitImplementation.config.log.EconomyLogger;
import BlockDynasty.repository.InitDatabase;
import BlockDynasty.BukkitImplementation.utils.UtilServer;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.PlaceHolderExpansion;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import org.bukkit.event.Listener;
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

    private Courier courier;

    private boolean debug = false;
    private boolean disabling = false;

    private UsesCaseFactory usesCaseFactory;

    @Override
    public void onLoad() {
        Configuration configuration = new Configuration(this);
        configuration.loadDefaultConfig();

        setDebug(getConfig().getBoolean("debug",false));

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
        if (getDataStore() != null) getDataStore().close();
    }

    private void initRepository() {
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
        this.offerService = new OfferService(new OfferListenerImpl());
        this.courier = new CourierImpl(this);

        this.usesCaseFactory = new UsesCaseFactory(accountService, currencyService, EconomyLogger.build(this), offerService, getDataStore(), courier);

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
        Listener economyListener;
        if(getServer().getOnlineMode()){ //get Config().getBoolean("online-mode",true)
            economyListener = new EconomyListenerOnline( usesCaseFactory.getCreateAccountUseCase(), usesCaseFactory.getAccountsUseCase(), accountService,currencyService);
        }else {
            economyListener = new EconomyListenerOffline( usesCaseFactory.getCreateAccountUseCase(), usesCaseFactory.getAccountsUseCase(), accountService,currencyService);
        }
        getServer().getPluginManager().registerEvents(economyListener, this);
    }
    private void setupIntegrations() {
        VaultFactory.init(new UsesCaseFactory(accountService, currencyService,VaultLogger.build(this), offerService,getDataStore(),courier));

        PlaceHolder.register(usesCaseFactory.getAccountsUseCase(), usesCaseFactory.getCurrencyUseCase());

        Bungee.init();
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
    public IRepository getDataStore() {
        if (repository == null) {
            throw new IllegalStateException("Repository is not initialized yet.");
        }
        return repository;
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

    public boolean isDebug() {
        return debug;
    }
    private void setDebug(boolean debug) {
        this.debug = debug;
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
