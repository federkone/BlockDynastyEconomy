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

package net.blockdynasty.economy.engine.repository.ebean.ConnectionHandler;

import io.ebean.datasource.DataSourceConfig;
import com.mysql.cj.jdbc.Driver;
import net.blockdynasty.economy.engine.repository.ebean.DbConfig;

public class ConnectionEbeanMysql extends ConnectionEbean {

    public ConnectionEbeanMysql(DbConfig dBconfig) {
        super();

        DataSourceConfig dsConfig = new DataSourceConfig();

        // 1. Datos de acceso
        dsConfig.setDriver(Driver.class.getName());
        dsConfig.setUrl("jdbc:mysql://" + dBconfig.getHost() + ":" + dBconfig.getPort() + "/" + dBconfig.getDatabase());
        dsConfig.setUsername(dBconfig.getUsername());
        dsConfig.setPassword(dBconfig.getPassword());

        // 2. Tiempos y Pool (Mapeo exacto de tus propiedades de Hikari)
        dsConfig.setMaxConnections(20);
        dsConfig.setMinConnections(5);
        dsConfig.setWaitTimeoutMillis(30000); // connectionTimeout
       // dsConfig.setMaxIdleSecs(600);        // idleTimeout (600000ms -> 600s)
        dsConfig.setMaxAgeMinutes(30);       // maxLifetime (1800000ms -> 30m)

        // 3. Optimizaciones de Rendimiento de MySQL
        dsConfig.addProperty("cachePrepStmts", "true");
        dsConfig.addProperty("prepStmtCacheSize", "250");
        dsConfig.addProperty("prepStmtCacheSqlLimit", "2048");
        dsConfig.addProperty("useServerPrepStmts", "true");

        // 4. Inyectar configuración e inicializar
        config.setDataSourceConfig(dsConfig);
        this.init();
    }

    @Override
    protected void stopServer() {
        // No requiere acción adicional
    }
}