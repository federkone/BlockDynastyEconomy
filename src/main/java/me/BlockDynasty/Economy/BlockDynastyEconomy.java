//TODO: REVISAR FUNCIONALIDAD EXCHANGE PARA EVITAR VALORES NEGATIVOS
//TODO: REVISAR EL ROLLBACK DE HIBERNATE, PUEDE AYUDAR A EVITAR PROBLEMAS EN TRANSACCIONES

package me.BlockDynasty.Economy;

import me.BlockDynasty.Economy.aplication.useCase.currency.*;
import me.BlockDynasty.Economy.aplication.useCase.offer.AcceptOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.CancelOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.offer.CreateOfferUseCase;
import me.BlockDynasty.Economy.aplication.useCase.transaction.*;
import me.BlockDynasty.Economy.config.logging.AbstractLogger;
import me.BlockDynasty.Economy.config.logging.VaultLogger;
import me.BlockDynasty.Economy.domain.Offers.OfferManager;
import me.BlockDynasty.Economy.domain.account.AccountCache;
import me.BlockDynasty.Economy.aplication.api.BlockDynastyEconomyAPI;
import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.cheque.ChequeManager;
import me.BlockDynasty.Economy.aplication.commands.NEW.CommandRegistration;
import me.BlockDynasty.Economy.domain.repository.ConnectionHandler.ConnectionHibernate;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.domain.repository.RepositoryCriteriaApi;
import me.BlockDynasty.Economy.domain.currency.CurrencyCache;
import me.BlockDynasty.Economy.config.file.Configuration;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.aplication.listeners.EconomyListener;
import me.BlockDynasty.Economy.config.logging.EconomyLogger;
import me.BlockDynasty.Economy.utils.UtilServer;
import me.BlockDynasty.Economy.aplication.vault.VaultHandler;
import me.BlockDynasty.Placeholder.BlockdynastyEconomyExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;

    private IRepository repository;
    private AccountCache accountCache;
    private ChequeManager chequeManager;
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

    private WithdrawUseCase withdrawUseCase ;
    private DepositUseCase depositUseCase ;
    private MessageService messageService ;
    private CreateCurrencyUseCase createCurrencyUseCase ;
    private SetBalanceUseCase setBalanceUseCase ;
    private PayUseCase payUseCase ;
    private ExchangeUseCase exchangeUseCase ;
    private GetBalanceUseCase balanceUseCase ;
    private GetAccountsUseCase getAccountsUseCase ;
    private GetCurrencyUseCase getCurrencyUseCase ;
    private CreateAccountUseCase createAccountUseCase ;
    private TransferFundsUseCase transferFundsUseCase ;
    private TradeCurrenciesUseCase tradeCurrenciesUseCase ;
    private DeleteCurrencyUseCase deleteCurrencyUseCase ;
    private EditCurrencyUseCase editCurrencyUseCase ;
    private ToggleFeaturesUseCase toggleFeaturesUseCase ;
    private CreateOfferUseCase createOfferUseCase ;
    private AcceptOfferUseCase acceptOfferUseCase ;
    private CancelOfferUseCase cancelOfferUseCase ;
    private static BlockDynastyEconomyAPI api;


    @Override
    public void onLoad() {
        Configuration configuration = new Configuration(this);
        configuration.loadDefaultConfig();

        setDebug(getConfig().getBoolean("debug",false));
        setVault(getConfig().getBoolean("vault",true));
        setLogging(getConfig().getBoolean("transaction_log",true));
        setCheques(getConfig().getBoolean("cheque.enabled",false));
    }

    @Override
    public void onEnable() {
        instance = this;

        try {
            initRepository(getConfig().getString("storage"));
            initCoreServices();
            registerCommands();
            registerEvents();
            setupIntegrations();
            getLogger().info("Plugin enabled successfully!");
        } catch (Exception e) {
            getLogger().severe("An error occurred during plugin initialization: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }

        //registrar api
        api = new BlockDynastyEconomyAPI(getAccountsUseCase,getCurrencyUseCase,depositUseCase,withdrawUseCase,exchangeUseCase,transferFundsUseCase,tradeCurrenciesUseCase);
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

    public void initRepository(String strategy) {
        switch (strategy.toLowerCase()) {
            case "mysql":
                try {
                    UtilServer.consoleLog("Initializing data store...");
                    //this.repository = new RepositoryMsql(new ConnectionMysql(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password")));
                    repository = new RepositoryCriteriaApi(new ConnectionHibernate(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password")));
                } catch (Exception e) {
                    UtilServer.consoleLog("§cCannot load initial data from DataStore.");
                    UtilServer.consoleLog("§cCheck your files, then try again.");
                    e.printStackTrace();
                    //getServer().getPluginManager().disablePlugin(this);
                    //getServer().shutdown();
                }
                break;
            case "yaml":
                // this.repository = new RepositoryYaml();
                // UtilServer.consoleLog("Loading currencies...");
                // repository.loadCurrencies();
                // UtilServer.consoleLog("Loaded " + getCurrencyManager().getCurrencies().size() + " currencies!");
                break;
            case "mongodb":
                break;
            default:
                UtilServer.consoleLog("§cNo valid storage method provided.");
                UtilServer.consoleLog("§cCheck your files, then try again.");
                getServer().getPluginManager().disablePlugin(this);
                break;
        }
    }
    private void initCoreServices() {
        accountCache = new AccountCache();
        currencyCache = new CurrencyCache(repository);
        economyLogger = new EconomyLogger(this);
        vaultLogger = new VaultLogger(this);
        //metrics = new Metrics(this);

        offerManager = new OfferManager(this);


        getCurrencyUseCase = new GetCurrencyUseCase(currencyCache,getDataStore());
        getAccountsUseCase = new GetAccountsUseCase(accountCache, currencyCache, getDataStore());

        messageService = new MessageService(currencyCache);
        updateForwarder = new UpdateForwarder(this,getAccountsUseCase);
        withdrawUseCase = new WithdrawUseCase(getCurrencyUseCase,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        depositUseCase = new DepositUseCase(getCurrencyUseCase, getAccountsUseCase,getDataStore(), updateForwarder, economyLogger);
        createCurrencyUseCase = new CreateCurrencyUseCase(currencyCache, updateForwarder,getDataStore());
        setBalanceUseCase = new SetBalanceUseCase( getCurrencyUseCase, getAccountsUseCase,getDataStore(), updateForwarder, economyLogger);
        payUseCase = new PayUseCase(getCurrencyUseCase,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        exchangeUseCase = new ExchangeUseCase(getCurrencyUseCase,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        balanceUseCase = new GetBalanceUseCase(getAccountsUseCase);
        createAccountUseCase = new CreateAccountUseCase(accountCache, currencyCache,getAccountsUseCase, getDataStore());
        tradeCurrenciesUseCase = new TradeCurrenciesUseCase(getCurrencyUseCase,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        transferFundsUseCase = new TransferFundsUseCase(getCurrencyUseCase,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        deleteCurrencyUseCase = new DeleteCurrencyUseCase(currencyCache,getDataStore(),updateForwarder);
        editCurrencyUseCase = new EditCurrencyUseCase(currencyCache,updateForwarder,getDataStore());
        toggleFeaturesUseCase = new ToggleFeaturesUseCase(currencyCache,getDataStore(),updateForwarder);
        createOfferUseCase = new CreateOfferUseCase(offerManager,getCurrencyUseCase,getAccountsUseCase);
        acceptOfferUseCase = new AcceptOfferUseCase(offerManager,tradeCurrenciesUseCase);
        cancelOfferUseCase = new CancelOfferUseCase(offerManager);



        if (isLogging()) {
            economyLogger.save();
        }

        if (isChequesEnabled()) {
            chequeManager = new ChequeManager(this);
        }


    }
    private void registerCommands(){
        CommandRegistration.registerCommands(this,payUseCase,exchangeUseCase,balanceUseCase, withdrawUseCase,
                                            setBalanceUseCase, depositUseCase,createCurrencyUseCase, messageService,getCurrencyUseCase,
                                            deleteCurrencyUseCase,editCurrencyUseCase,toggleFeaturesUseCase,createOfferUseCase,acceptOfferUseCase,cancelOfferUseCase);
        //todo: comando trade, el cual eliminaria la necesidad del comando vender de la extension que hice
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new EconomyListener(this,createAccountUseCase,getAccountsUseCase, accountCache), this);
    }
    private void setupIntegrations() {
        // Configuración de Vault
        if (isVault()) {
            //TODO SE PUEDE PREGUNTAR VAULT_LOGGER ESTA HABILITADO PARA INYECTAR EL LOGER VAULT O EL DE ECONOMY
            vaultHandler = new VaultHandler(this,createAccountUseCase,getAccountsUseCase,getCurrencyUseCase, depositUseCase, withdrawUseCase);
            vaultHandler.hook();
        } else {
            getLogger().info("Vault integration is disabled.");
        }

        // PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new BlockdynastyEconomyExpansion(this,getAccountsUseCase,getCurrencyUseCase).register();
            getLogger().info("PlaceholderAPI Expansion registered successfully!");
        } else {
            getLogger().warning("PlaceholderAPI not found. Expansion won't be loaded.");
        }

        // Canal de mensajería
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", updateForwarder);
    }

    public IRepository getDataStore() {  //IRepository
        if (repository == null) {
            throw new IllegalStateException("Repository is not initialized yet.");
        }
        return repository;             //return repository;
    }

    public static BlockDynastyEconomyAPI getAPI() {
        return api;
    }

    public static BlockDynastyEconomy getInstance() {  //en el unico lugar que queda es en la API como @deprecated
        return instance;
    }
    public CurrencyCache getCurrencyManager() {
        return currencyCache;
    }

    public AccountCache getAccountManager() {
        return accountCache;
    }

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }

    public AbstractLogger getEconomyLogger() {
        return economyLogger;
    }


    public ChequeManager getChequeManager() {
        return chequeManager;
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

    public boolean isChequesEnabled() {
        return cheques;
    }

    public void setCheques(boolean cheques) {
        this.cheques = cheques;
    }
}
