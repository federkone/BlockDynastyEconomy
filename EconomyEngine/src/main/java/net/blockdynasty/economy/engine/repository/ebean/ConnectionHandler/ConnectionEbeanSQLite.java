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
/*
import net.blockdynasty.economy.engine.repository.ebean.DbConfig;
import net.blockdynasty.economy.libs.services.Console;
import org.h2.tools.Server;
import org.sqlite.JDBC;


import java.nio.charset.StandardCharsets;

import io.ebean.datasource.DataSourceConfig;

public class ConnectionEbeanSQLite extends ConnectionEbean {
    private Server webServer;
    public ConnectionEbeanSQLite(DbConfig dbconfig) {
        super();

        // 1. URL de conexión estándar para SQLite
        String url = "jdbc:sqlite:" + dbconfig.getDatabasePath() + "/database.db";

        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setDriver(JDBC.class.getName());
        dsConfig.setUrl(url);

        // En SQLite, el pool debe ser de 1 conexión para evitar bloqueos de escritura
        dsConfig.setMaxConnections(1);
        dsConfig.setWaitTimeoutMillis(30000);

        // 2. Configuración de PRAGMAs (WAL y Synchronous)
        // Ebean permite pasar estas propiedades que el driver de SQLite aplica al conectar
        dsConfig.addProperty("journal_mode", "WAL");
        dsConfig.addProperty("synchronous", "NORMAL");

        // 3. Asignar configuración
        config.setDataSourceConfig(dsConfig);

        // 4. Inicializar Ebean
        this.init();

        if (dbconfig.isEnableWebEditorSqlServer()) {
            // Nota: SQLite no tiene una consola web nativa como H2.
            // Si usabas una herramienta externa, asegúrate de que sea compatible.
            startServerConsole(dbconfig.getDatabasePath());
        }
    }

    // This method starts the H2 web console server.
    private void startServerConsole(String dbFilePath){
        try {
            // Skip password requirement
            System.setProperty("h2.consoleForcePassword", "false");

            // Set default settings for the console
            System.setProperty("h2.bindAddress", "localhost");
            System.setProperty("h2.webAllowOthers", "true");
            System.setProperty("h2.webAdminPassword", "");

            // Create web server with customized settings
            webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082",
                    "-baseDir", dbFilePath);
            webServer.start();

            // Generate direct access URL with pre-filled connection details
            String jdbcUrl = "jdbc:h2:file:" + dbFilePath + "/h2Database";
            String consoleUrl = "http://localhost:8082/";
            String directAccessUrl = consoleUrl + "login.jsp?jsessionid=autocomplete&url=" +
                    java.net.URLEncoder.encode(jdbcUrl, StandardCharsets.UTF_8) +
                    "&user=sa&password=";

            Console.log("  -> SQL WEB console started at: " + consoleUrl);
            Console.log("  -> Direct access URL: " + directAccessUrl);
            Console.log("  -> JDBC URL: " + jdbcUrl);
        } catch (Exception e) {
            Console.logError("Failed to start H2 console: " + e.getMessage());
        }
    }

    @Override
    protected void stopServer() {
        if (webServer != null) {
            webServer.stop();
        }

    }
}*/