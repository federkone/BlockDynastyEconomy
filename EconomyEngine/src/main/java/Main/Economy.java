package Main;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.log.Log;
import api.Api;
import api.IApi;
import lib.commands.CommandsFactory;
import lib.abstractions.PlatformAdapter;
import lib.gui.GUIFactory;
import lib.gui.abstractions.ITextInput;
import lib.placeholder.PlaceHolder;
import listeners.*;
import proxy.ProxyData;
import proxy.ProxyReceiver;
import proxy.ProxySender;
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


    public void init(ITextInput textInput, IConsole console, Log log, PlatformAdapter platformAdapter,
                      IConfiguration configuration, ProxyData proxyData){

        Console.setConsole(console);

        repository = new Repository(getConnection(configuration));

        core=new Core(repository,60,new OfferListener(platformAdapter),new ProxySender(proxyData,platformAdapter ),log);
        api = new Api(core);
        this.placeHolder = new PlaceHolder(core.getAccountsUseCase().getGetAccountsUseCase(), core.getCurrencyUseCase().getGetCurrencyUseCase());
        playerJoinListener = new PlayerJoinListener(core.getAccountsUseCase().getCreateAccountUseCase(),core.getAccountsUseCase().getGetAccountsUseCase(),core.getServicesManager().getAccountService());
        CommandsFactory.init(core.getTransactionsUseCase(), core.getOfferUseCase(),core.getCurrencyUseCase(), core.getAccountsUseCase(),platformAdapter);
        GUIFactory.init(core.getCurrencyUseCase(), core.getAccountsUseCase(), core.getTransactionsUseCase(),core.getOfferUseCase(),textInput, platformAdapter);
        TransactionsListener.register(core.getServicesManager().getEventManager(),platformAdapter);
        ProxyReceiver.init(proxyData, core.getServicesManager().getAccountService(),core.getServicesManager().getEventManager(), platformAdapter);
    }

    private Connection getConnection(IConfiguration configuration){
        switch (configuration.getType()){
            case "mysql":
                return new ConnectionHibernateMysql(configuration.getHost(), configuration.getPort(), configuration.getDatabase(), configuration.getUsername(), configuration.getPassword());
            case "h2":
                return new ConnectionHibernateH2(configuration.getDbFilePath(),configuration.enableServerConsole());
            case "sqlite":
                return new ConnectionHibernateSQLite(configuration.getDbFilePath(),configuration.enableServerConsole());
            default:
                throw new IllegalArgumentException("Unsupported database type: " + configuration.getType());
        }
    }
    public void shutdown(){
        if (repository != null) {
            repository.close();
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
