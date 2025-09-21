package Main;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import api.Api;
import api.IApi;
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
import redis.clients.jedis.Jedis;
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
    Subscriber subscriber;

    public void init(ITextInput textInput, IConsole console, Log log, PlatformAdapter platformAdapter,
                      IConfiguration configuration){

        Console.setConsole(console);

        repository = new Repository(getConnection(configuration));

        core=new Core(repository,60,createCourierImpl(configuration,platformAdapter),log);
        createListener(configuration,platformAdapter);
        api = new Api(core);
        this.placeHolder = new PlaceHolder(core.getAccountsUseCase().getGetAccountsUseCase(), core.getCurrencyUseCase().getGetCurrencyUseCase());
        playerJoinListener = new PlayerJoinListener(core.getAccountsUseCase().getCreateAccountUseCase(),core.getAccountsUseCase().getGetAccountsUseCase(),core.getServicesManager().getAccountService());
        CommandsFactory.init(core.getTransactionsUseCase(), core.getOfferUseCase(),core.getCurrencyUseCase(), core.getAccountsUseCase(),platformAdapter);
        GUIFactory.init(core.getCurrencyUseCase(), core.getAccountsUseCase(), core.getTransactionsUseCase(),core.getOfferUseCase(),textInput, platformAdapter);
        EventListener.register(core.getServicesManager().getEventManager(),platformAdapter);
    }

    private Connection getConnection(IConfiguration configuration){
        switch (configuration.getConfig().getString("sql.type")){
            case "mysql":
                return new ConnectionHibernateMysql(configuration.getConfig().getString("sql.host"), configuration.getConfig().getInt("sql.port"), configuration.getConfig().getString("sql.database"), configuration.getConfig().getString("sql.username"), configuration.getConfig().getString("sql.password"));
            case "h2":
                return new ConnectionHibernateH2(configuration.getDbFilePath(),configuration.getConfig().getBoolean("EnableWebEditorSqlServer"));
            case "sqlite":
                return new ConnectionHibernateSQLite(configuration.getDbFilePath(),configuration.getConfig().getBoolean("EnableWebEditorSqlServer"));
            default:
                throw new IllegalArgumentException("Unsupported database type: " + configuration.getConfig().getString("sql.type"));
        }
    }

    private Courier createCourierImpl(IConfiguration configuration, PlatformAdapter platformAdapter){
        if(configuration.getConfig().getBoolean("redis.enabled")){
            return new Publisher( new RedisData(configuration),platformAdapter);
        }else{
            return new ProxySender(platformAdapter);
        }
    }
    private void createListener(IConfiguration configuration, PlatformAdapter platformAdapter){
        if(configuration.getConfig().getBoolean("redis.enabled")){
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
}
