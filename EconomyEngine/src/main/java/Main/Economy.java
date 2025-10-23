/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Main;

import BlockDynasty.Economy.Core;
import BlockDynasty.Economy.domain.persistence.entities.IRepository;
import BlockDynasty.Economy.domain.services.courier.Courier;
import BlockDynasty.Economy.domain.services.log.Log;
import api.Api;
import api.IApi;
import platform.files.Configuration;
import platform.files.Languages;
import platform.files.logs.EconomyLogger;
import platform.files.logs.VaultLogger;
import lib.commands.CommandsFactory;
import lib.abstractions.PlatformAdapter;
import lib.gui.GUIFactory;
import lib.gui.components.ITextInput;
import lib.placeholder.PlaceHolder;
import lib.util.colors.ChatColor;
import platform.listeners.EventListener;
import platform.listeners.IPlayerJoin;
import platform.listeners.PlayerJoinListener;
import platform.proxy.ProxyReceiver;
import platform.proxy.ProxySender;
import redis.Publisher;
import redis.RedisData;
import redis.Subscriber;
import repository.ConnectionHandler.Hibernate.Connection;
import repository.ConnectionHandler.Hibernate.ConnectionHibernateH2;
import repository.ConnectionHandler.Hibernate.ConnectionHibernateMysql;
import repository.ConnectionHandler.Hibernate.ConnectionHibernateSQLite;
import repository.Repository;
import services.Message;
import utils.Console;

public class Economy {
    private Core core;
    private static IRepository repository;
    private PlayerJoinListener playerJoinListener;
    private IApi api;
    private PlaceHolder placeHolder;
    private static Subscriber subscriber;
    private Configuration configuration;
    private Languages languages;
    private PlatformAdapter platformAdapter;

    private Economy(ITextInput textInput, PlatformAdapter platformAdapter){
        this.platformAdapter=platformAdapter;
        configuration= new Configuration(platformAdapter.getDataFolder());
        if(!platformAdapter.hasSupportAdventureText() || configuration.getBoolean("forceVanillaColorsSystem") ){ChatColor.setupVanilla();}
        languages = new Languages(platformAdapter.getDataFolder());
        languages.loadMessages(configuration.getString("lang"));
        Message.addLang(languages);
        Console.setConsole(platformAdapter.getConsole(),configuration);

        try{
            repository = new Repository(getConnection(configuration));
        }catch (Exception e){
            Console.logError("Error connection database, check your credentials.");
            throw new RuntimeException(e.getMessage());
        }

        Console.log("Database connected successfully.");

        core=new Core(repository,60,createCourierImpl(configuration,platformAdapter),new EconomyLogger( configuration,platformAdapter.getScheduler()));
        createListener(configuration,platformAdapter);
        api = new Api(core);
        this.placeHolder = new PlaceHolder(core.getUseCaseFactory());
        playerJoinListener = new PlayerJoinListener(core.getUseCaseFactory(),core.getServicesManager().getAccountService());
        CommandsFactory.init(platformAdapter,core.getUseCaseFactory());
        GUIFactory.init(core.getUseCaseFactory(),textInput, platformAdapter,new Message());
        EventListener.register(core.getServicesManager().getEventManager(),platformAdapter);
    }

    public static Economy init(ITextInput textInput, PlatformAdapter platformAdapter){
        return new Economy(textInput, platformAdapter);
    }

    private Connection getConnection(Configuration configuration){
        switch (configuration.getString("sql.type")){
            case "mysql":
                return new ConnectionHibernateMysql(configuration.getString("sql.host"), configuration.getInt("sql.port"), configuration.getString("sql.database"), configuration.getString("sql.username"), configuration.getString("sql.password"));
            case "h2":
                 return new ConnectionHibernateH2(configuration.getDatabasePath(),configuration.getBoolean("sql.EnableWebEditorSqlServer"));
            case "sqlite":
                 return new ConnectionHibernateSQLite(configuration.getDatabasePath(),configuration.getBoolean("sql.EnableWebEditorSqlServer"));
            default:
                 throw new IllegalArgumentException("Unsupported database type: " + configuration.getString("sql.type"));
            }
    }

    private Courier createCourierImpl(Configuration configuration, PlatformAdapter platformAdapter){
        if(configuration.getBoolean("redis.enabled")){
            Console.log("Redis Enabled");
            return new Publisher( new RedisData(configuration),platformAdapter);
        }else{
            Console.log("Redis Disable - Using Proxy System");
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

    public static void shutdown(){
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
