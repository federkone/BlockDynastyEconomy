//TODO: REVISAR FUNCIONALIDAD EXCHANGE PARA EVITAR VALORES NEGATIVOS
//TODO: REVISAR EL ROLLBACK DE HIBERNATE, PUEDE AYUDAR A EVITAR PROBLEMAS EN TRANSACCIONES

package me.BlockDynasty.Economy;

import me.BlockDynasty.Economy.aplication.useCase.transaction.*;
import me.BlockDynasty.Economy.domain.account.AccountManager;
import me.BlockDynasty.Economy.aplication.api.BlockDynastyEconomyAPI;
import me.BlockDynasty.Economy.aplication.useCase.account.GetBalanceUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.CreateAccountUseCase;
import me.BlockDynasty.Economy.aplication.useCase.account.GetAccountsUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.CreateCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.useCase.currency.GetCurrencyUseCase;
import me.BlockDynasty.Economy.aplication.bungee.UpdateForwarder;
import me.BlockDynasty.Economy.domain.cheque.ChequeManager;
import me.BlockDynasty.Economy.aplication.commands.NEW.CommandRegistration;
import me.BlockDynasty.Economy.domain.repository.ConnectionHandler.ConnectionHibernate;
import me.BlockDynasty.Economy.domain.repository.IRepository;
import me.BlockDynasty.Economy.domain.repository.RepositoryCriteriaApi;
import me.BlockDynasty.Economy.domain.currency.CurrencyManager;
import me.BlockDynasty.Economy.config.file.Configuration;
import me.BlockDynasty.Economy.config.file.MessageService;
import me.BlockDynasty.Economy.aplication.listeners.EconomyListener;
import me.BlockDynasty.Economy.config.logging.EconomyLogger;
import me.BlockDynasty.Economy.utils.Metrics;
import me.BlockDynasty.Economy.utils.UtilServer;
import me.BlockDynasty.Economy.aplication.vault.VaultHandler;
import me.BlockDynasty.Placeholder.BlockdynastyEconomyExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDynastyEconomy extends JavaPlugin {
    private static BlockDynastyEconomy instance;

    private IRepository repository;
    private AccountManager accountManager;
    private ChequeManager chequeManager;
    private CurrencyManager currencyManager;
    private VaultHandler vaultHandler;
    private Metrics metrics;
    private EconomyLogger economyLogger;
    private UpdateForwarder updateForwarder;

    private boolean debug = false;
    private boolean vault = false;
    private boolean logging = false;
    private boolean cheques = true;

    private boolean disabling = false;

    private WithdrawUseCase NewWithdrawUseCase ;
    private DepositUseCase NewDepositUseCase ;
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
    private static BlockDynastyEconomyAPI api;
    //GetAccountsUseCase getAccountsUseCase ;


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
        api = new BlockDynastyEconomyAPI(getAccountsUseCase,getCurrencyUseCase,NewDepositUseCase,NewWithdrawUseCase,exchangeUseCase,transferFundsUseCase,tradeCurrenciesUseCase);
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
        accountManager = new AccountManager();
        currencyManager = new CurrencyManager(repository);
        economyLogger = new EconomyLogger(this);
        //metrics = new Metrics(this);
        updateForwarder = new UpdateForwarder(this);

        messageService = new MessageService(currencyManager);
        getAccountsUseCase = new GetAccountsUseCase(accountManager, currencyManager, getDataStore());
        getCurrencyUseCase = new GetCurrencyUseCase(currencyManager);
        NewWithdrawUseCase = new WithdrawUseCase(accountManager, currencyManager,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        NewDepositUseCase = new DepositUseCase(accountManager, currencyManager, getAccountsUseCase,getDataStore(), updateForwarder, economyLogger);
        createCurrencyUseCase = new CreateCurrencyUseCase(currencyManager, updateForwarder,getDataStore());
        setBalanceUseCase = new SetBalanceUseCase(accountManager, currencyManager, getAccountsUseCase,getDataStore(), updateForwarder, economyLogger);
        payUseCase = new PayUseCase(accountManager, currencyManager,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        exchangeUseCase = new ExchangeUseCase(accountManager, currencyManager,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        balanceUseCase = new GetBalanceUseCase(accountManager, currencyManager,getAccountsUseCase, updateForwarder,getDataStore());
        createAccountUseCase = new CreateAccountUseCase(accountManager,currencyManager,getAccountsUseCase,updateForwarder, getDataStore());
        tradeCurrenciesUseCase = new TradeCurrenciesUseCase(accountManager, currencyManager,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);
        transferFundsUseCase = new TransferFundsUseCase(accountManager, currencyManager,getAccountsUseCase, getDataStore(), updateForwarder, economyLogger);


        if (isLogging()) {
            economyLogger.save();
        }

        if (isChequesEnabled()) {
            chequeManager = new ChequeManager(this);
        }


    }
    private void registerCommands(){
        CommandRegistration.registerCommands(this,payUseCase,exchangeUseCase,balanceUseCase, NewWithdrawUseCase, setBalanceUseCase, NewDepositUseCase,createCurrencyUseCase, messageService);



        //todo: comando trade, el cual eliminaria la necesidad del comando vender de la extension que hice
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new EconomyListener(this,createAccountUseCase,getAccountsUseCase), this);
    }
    private void setupIntegrations() {
        // Configuración de Vault
        if (isVault()) {
            vaultHandler = new VaultHandler(this,createAccountUseCase,getAccountsUseCase,getCurrencyUseCase,NewDepositUseCase,NewWithdrawUseCase);
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
    public CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public VaultHandler getVaultHandler() {
        return vaultHandler;
    }

    public EconomyLogger getEconomyLogger() {
        return economyLogger;
    }

    public Metrics getMetrics() {
        return metrics;
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
