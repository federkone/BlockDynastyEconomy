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
/*
package com.blockdynasty.economy.repository.ebean.ConnectionHandler;

import io.ebean.datasource.DataSourceConfig;
import org.mariadb.jdbc.Driver;

public class ConnectionEbeanMariaDb extends ConnectionEbean {

    public ConnectionEbeanMariaDb(String host, int port, String database, String username, String password) {
        super();

        DataSourceConfig dsConfig = new DataSourceConfig();

        // 1. Configuración de la conexión
        dsConfig.setDriver(Driver.class.getName()); // org.mariadb.jdbc.Driver
        dsConfig.setUrl("jdbc:mariadb://" + host + ":" + port + "/" + database);
        dsConfig.setUsername(username);
        dsConfig.setPassword(password);

        // 2. Configuración del Pool (Equivalente a tus propiedades de Hikari)
        dsConfig.setMaxConnections(20);
        dsConfig.setMinConnections(5);
        dsConfig.setWaitTimeoutMillis(30000);

        // 3. Optimizaciones específicas de MariaDB/MySQL
        // Ebean permite añadir propiedades directamente al driver JDBC
        dsConfig.addProperty("useServerPrepStmts", "true");
        dsConfig.addProperty("cachePrepStmts", "true");
        dsConfig.addProperty("prepStmtCacheSize", "250");
        dsConfig.addProperty("prepStmtCacheSqlLimit", "2048");

        // 4. Asignar y arrancar
        config.setDataSourceConfig(dsConfig);
        this.init();
    }

    @Override
    protected void stopServer() {
        // MariaDB no suele requerir un stop manual como el WebServer de H2
    }
}*/