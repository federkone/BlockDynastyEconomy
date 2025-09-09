package Main;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.log.Log;
import api.Api;
import api.IApi;
import lib.commands.CommandsFactory;
import lib.commands.abstractions.PlatformAdapter;
import lib.gui.GUIFactory;
import lib.gui.abstractions.ITextInput;
import listeners.*;
import repository.ConnectionHandler.Hibernate.ConnectionHibernateH2;
import repository.ConnectionHandler.Hibernate.ConnectionHibernateMysql;
import repository.Repository;

public class Economy {
    private Core core;
    private IRepository repository;
    private PlayerJoinListener playerJoinListener;
    private IApi api;

    //inyectar consola, log, ITextInput, PlatformAdapter
    //inyectar consola, log, ITextInput, PlatformAdapter,Courier
    //algun archivo/clase de configuracion que traiga informacion util para bildear el plugin como por ejemplo la conexion a la base de datos
    public void init(ITextInput textInput, IConsole console, Log log, PlatformAdapter platformAdapter,
                      IConfiguration configuration, BlockDynasty.Economy.domain.services.courier.Courier courier){

        Console.setConsole(console);

        //IRepository repositoryMysql = new Repository( new ConnectionHibernateMysql( configuration.getHost(), configuration.getPort(), configuration.getDatabase(), configuration.getUsername(), configuration.getPassword() ));
        repository = new Repository(new ConnectionHibernateH2(configuration.getDbFilePath(),configuration.enableServerConsole()));

        core=new Core(repository,60,new OfferListener(platformAdapter),courier,log);
        api = new Api(core);
        playerJoinListener = new PlayerJoinListener(core.getAccountsUseCase().getCreateAccountUseCase(),core.getAccountsUseCase().getGetAccountsUseCase(),core.getServicesManager().getAccountService());
        CommandsFactory.init(core.getTransactionsUseCase(), core.getOfferUseCase(),core.getCurrencyUseCase(), core.getAccountsUseCase(),platformAdapter);
        GUIFactory.init(core.getCurrencyUseCase(), core.getAccountsUseCase(), core.getTransactionsUseCase(),core.getOfferUseCase(),textInput, platformAdapter);
        TransactionsListener.register(core.getServicesManager().getEventManager(),platformAdapter);
    }

    public void initv2(){

    }

    public void shutdown(){
        if (repository == null) {
            throw new IllegalStateException("Repository is not initialized yet.");
        }
        repository.close();
    }
    public Core getCore(){
        return core;
    }

    public IPlayerJoin getPlayerJoinListener(){
        return playerJoinListener;
    }

    public IApi getApi(){
        return api;
    }

    public IApi getApiWithLog(Log log){
        return new Api( core, log);
    }
}
