package BlockDynasty.BukkitImplementation;

import BlockDynasty.BukkitImplementation.GUI.RegisterModule;
import BlockDynasty.BukkitImplementation.Integrations.Placeholder.PlaceHolder;
import BlockDynasty.BukkitImplementation.Integrations.bungee.Bungee;
import BlockDynasty.BukkitImplementation.Integrations.vault.Vault;
import BlockDynasty.BukkitImplementation.Integrations.bungee.CourierImpl;
import BlockDynasty.BukkitImplementation.listeners.EconomyListenerOffline;
import BlockDynasty.BukkitImplementation.listeners.EconomyListenerOnline;
import BlockDynasty.BukkitImplementation.listeners.OfferListenerImpl;
import BlockDynasty.BukkitImplementation.listeners.TransactionsListener;
import BlockDynasty.BukkitImplementation.logs.VaultLogger;
import BlockDynasty.BukkitImplementation.config.file.Configuration;
import BlockDynasty.BukkitImplementation.services.MessageService;
import BlockDynasty.BukkitImplementation.commands.CommandRegister;
import BlockDynasty.BukkitImplementation.logs.EconomyLogger;
import BlockDynasty.BukkitImplementation.utils.UtilServer;

import BlockDynasty.Economy.aplication.api.Api;
import BlockDynasty.Economy.aplication.api.BlockDynastyEconomyApi;
import BlockDynasty.Economy.aplication.events.EventManager;
import BlockDynasty.Economy.aplication.useCase.UsesCaseFactory;
import BlockDynasty.Economy.aplication.services.OfferService;
import BlockDynasty.Economy.aplication.services.AccountService;
import BlockDynasty.Economy.aplication.services.CurrencyService;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.IAccountService;
import BlockDynasty.Economy.domain.services.ICurrencyService;
import BlockDynasty.Economy.domain.services.IOfferService;
import BlockDynasty.Economy.domain.result.Result;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;

import BlockDynasty.repository.InitDatabase;

import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;
    private static Api api;

    private IRepository repository;
    private IAccountService accountService;
    private ICurrencyService currencyService;
    private MessageService messageService;
    private EventManager eventManager;
    private IOfferService offerService;
    private Courier courier;
    private UsesCaseFactory usesCaseFactory;

    private boolean disabling = false;

    @Override
    public void onLoad() {
        Configuration.init(this);
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
            UtilServer.consoleLogError("An error occurred during plugin initialization: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }

        //registrar api
        api = new BlockDynastyEconomyApi(usesCaseFactory);
        getServer().getServicesManager().register(Api.class, api, this, ServicePriority.Low);
    }

    @Override
    public void onDisable() {
        disabling = true;
        if (getDataStore() != null) getDataStore().close();
        Vault.unhook();
        PlaceHolder.unregister();
        Bungee.unhook(this);
    }

    private void initRepository() {
        UtilServer.consoleLog("§a Initializing data store BlockDynastyEconomy...");
        Result<IRepository> result = InitDatabase.init(this);
        if (result.isSuccess()) {
            repository = result.getValue();
            UtilServer.consoleLog("§a Data store BlockDynastyEconomy initialized successfully.");
        } else {
            UtilServer.consoleLogError(result.getErrorMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    private void initCoreServices() {
        this.accountService = new AccountService(getConfig().getInt("expireCacheTopMinutes"));
        this.currencyService = new CurrencyService(getDataStore());
        this.messageService = new MessageService(currencyService);
        this.offerService = new OfferService(new OfferListenerImpl());
        this.eventManager = new EventManager();
        this.courier = new CourierImpl(this);

        this.usesCaseFactory = new UsesCaseFactory(accountService, currencyService, EconomyLogger.build(this), offerService, getDataStore(), courier,eventManager);
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
            UtilServer.consoleLog("Online mode is enabled. The plugin will use UUID to identify players.");
        }else {
            economyListener = new EconomyListenerOffline( usesCaseFactory.getCreateAccountUseCase(), usesCaseFactory.getAccountsUseCase(), accountService,currencyService);
            UtilServer.consoleLog("Online mode is disabled, The plugin will use NICKNAME to identify players.");
        }
        getServer().getPluginManager().registerEvents(economyListener, this);
        TransactionsListener.register(eventManager);
    }
    private void setupIntegrations() {
        Vault.init(new UsesCaseFactory(accountService, currencyService,VaultLogger.build(this), offerService,getDataStore(),courier,eventManager));
        PlaceHolder.register(usesCaseFactory.getAccountsUseCase(), usesCaseFactory.getCurrencyUseCase());
        Bungee.init(this);
    }

    public static BlockDynastyEconomy getInstance() {
        return instance;
    }
    public static Api getApi() {
        return api;
    }
    public UsesCaseFactory getUsesCaseFactory() {
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

    public boolean isDisabling() {
        return disabling;
    }
}
