package Main;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import api.Api;
import api.IApi;
import files.Configuration;
import files.logs.EconomyLogger;
import files.logs.VaultLogger;
import lib.commands.CommandsFactory;
import lib.abstractions.PlatformAdapter;
import lib.gui.GUIFactory;
import lib.gui.abstractions.ITextInput;
import lib.placeholder.PlaceHolder;
import listeners.*;
import proxy.ProxyReceiver;
import proxy.ProxySender;
import redis.Publisher;
import redis.RedisData;
import redis.Subscriber;
import repository.ConnectionHandler.Hibernate.Connection;
import repository.ConnectionHandler.Hibernate.ConnectionHibernateH2;
import repository.ConnectionHandler.Hibernate.ConnectionHibernateMysql;
import repository.ConnectionHandler.Hibernate.ConnectionHibernateSQLite;
import repository.Repository;

public class Economy {
    private Core core;
    private IRepository repository;
    private PlayerJoinListener playerJoinListener;
    private IApi api;
    private PlaceHolder placeHolder;
    private Subscriber subscriber;
    private Configuration configuration;
    private PlatformAdapter platformAdapter;

    public void init(ITextInput textInput, IConsole console, PlatformAdapter platformAdapter){
        Console.setConsole(console);
        this.platformAdapter=platformAdapter;
        configuration= new Configuration(platformAdapter.getDataFolder());

        repository = new Repository(getConnection(configuration));

        core=new Core(repository,60,createCourierImpl(configuration,platformAdapter),new EconomyLogger( configuration,platformAdapter.getScheduler()));
        createListener(configuration,platformAdapter);
        api = new Api(core);
        this.placeHolder = new PlaceHolder(core.getAccountsUseCase().getGetAccountsUseCase(), core.getCurrencyUseCase().getGetCurrencyUseCase());
        playerJoinListener = new PlayerJoinListener(core.getAccountsUseCase().getCreateAccountUseCase(),core.getAccountsUseCase().getGetAccountsUseCase(),core.getServicesManager().getAccountService());
        CommandsFactory.init(core.getTransactionsUseCase(), core.getOfferUseCase(),core.getCurrencyUseCase(), core.getAccountsUseCase(),platformAdapter);
        GUIFactory.init(core.getCurrencyUseCase(), core.getAccountsUseCase(), core.getTransactionsUseCase(),core.getOfferUseCase(),textInput, platformAdapter);
        EventListener.register(core.getServicesManager().getEventManager(),platformAdapter);
    }

    private Connection getConnection(Configuration configuration){
        switch (configuration.getString("sql.type")){
            case "mysql":
                return new ConnectionHibernateMysql(configuration.getString("sql.host"), configuration.getInt("sql.port"), configuration.getString("sql.database"), configuration.getString("sql.username"), configuration.getString("sql.password"));
            case "h2":
                return new ConnectionHibernateH2(configuration.getDatabasePath(),configuration.getBoolean("EnableWebEditorSqlServer"));
            case "sqlite":
                return new ConnectionHibernateSQLite(configuration.getDatabasePath(),configuration.getBoolean("EnableWebEditorSqlServer"));
            default:
                throw new IllegalArgumentException("Unsupported database type: " + configuration.getString("sql.type"));
        }
    }

    private Courier createCourierImpl(Configuration configuration, PlatformAdapter platformAdapter){
        if(configuration.getBoolean("redis.enabled")){
            return new Publisher( new RedisData(configuration),platformAdapter);
        }else{
            return new ProxySender(platformAdapter);
        }
    }
    private void createListener(Configuration configuration, PlatformAdapter platformAdapter){
        if(configuration.getBoolean("redis.enabled")){
            subscriber = new Subscriber(new RedisData(configuration),platformAdapter,core.getServicesManager().getOfferService(),core.getServicesManager().getCurrencyService(),core.getServicesManager().getAccountService(),core.getServicesManager().getEventManager());
            subscriber.startListening();
        }else{
            ProxyReceiver.init(core.getServicesManager().getAccountService(),core.getServicesManager().getCurrencyService(),core.getServicesManager().getEventManager(), core.getServicesManager().getOfferService(),platformAdapter);
        }
    }

    public void shutdown(){
        if (repository != null) {
            repository.close();
        }
        if (subscriber != null) {
            subscriber.stopListening();
        }
    }

    public IPlayerJoin getPlayerJoinListener(){
        return playerJoinListener;
    }

    public IApi getApi(){
        return api;
    }
    public PlaceHolder getPlaceHolder(){
        return placeHolder;
    }

    public IApi getApiWithLog(Log log){
        return new Api( core, log);
    }

    public Configuration getConfiguration(){
        return configuration;
    }

    public Log getVaultLogger(){
        return VaultLogger.build(configuration,platformAdapter.getScheduler());
    }
}
