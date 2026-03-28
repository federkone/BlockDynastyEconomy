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

/*package com.blockdynasty.economy.repository.ebean.ConnectionHandler;

import com.blockdynasty.economy.repository.ebean.Mappers.CurrencyInterchangeable;
import com.blockdynasty.economy.repository.ebean.Models.AccountDb;
import com.blockdynasty.economy.repository.ebean.Models.BalanceDb;
import com.blockdynasty.economy.repository.ebean.Models.CurrencyDb;
import com.blockdynasty.economy.repository.ebean.Models.WalletDb;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;

public abstract class ConnectionEbean implements Connection {
    private Database database;
    protected DatabaseConfig config = new DatabaseConfig();
    protected DataSourceConfig dsConfig = new DataSourceConfig();

    public ConnectionEbean() {
        config.setDdlGenerate(true);
        config.setDdlRun(true);
        //config.setDdlCreateOnly(false);

        config.addClass(CurrencyDb.class);
        config.addClass(AccountDb.class);
        config.addClass(BalanceDb.class);
        config.addClass(WalletDb.class);
        config.addClass(CurrencyInterchangeable.class);
    }

    protected void init() {
        try {
            this.config.setDataSourceConfig(dsConfig);
            this.database = DatabaseFactory.create(config);
        } catch (Exception ex) {
            throw new RuntimeException("Error al iniciar el motor de Ebean: " + ex);
        }
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    @Override
    public void close() {
        if (database != null) {
            database.shutdown(); // Cierra el pool y libera recursos
        }
        stopServer();
    }

    protected abstract void stopServer();
}*/