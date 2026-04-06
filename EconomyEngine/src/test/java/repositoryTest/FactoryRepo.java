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

package repositoryTest;

import net.blockdynasty.economy.core.domain.persistence.entities.IRepository;
import net.blockdynasty.economy.engine.platform.files.IConfigurationEngine;
import net.blockdynasty.economy.engine.repository.ebean.ConnectionFactory;
import net.blockdynasty.economy.engine.repository.ebean.ConnectionHandler.Connection;
import net.blockdynasty.economy.engine.repository.ebean.Repository;
import repositoryTest.ConnectionHandler.MockConnectionHEbeanH2;

import java.util.List;
import java.util.Map;

public class FactoryRepo {
    private static Connection connection = new MockConnectionHEbeanH2();
    private static IRepository repository = new Repository(connection);

    public static IRepository getDb(){
       ConnectionFactory factory = new ConnectionFactory(config());
       Connection connection = factory.get();
       return new Repository(connection);
    }

    public static Connection getConnection(){
        ConnectionFactory factory = new ConnectionFactory(config());
        return factory.get();
    }

    public static IConfigurationEngine config(){
        return new IConfigurationEngine() {
            private Map<String, String> config= Map.of(
                    "sql.type", "h2",
                    "sql.database", "testDb",
                    "sql.host", "localhost",
                    "sql.port", "3306",
                    "sql.username", "root",
                    "sql.password", "",
                    "sql.enableWebEditorSqlServer", "true"
            );


            @Override
            public String getDatabasePath() {
                return "./src/test/java/repositoryTest/data";
            }

            @Override
            public String getLogsPath() {
                return "";
            }

            @Override
            public void updateConfig(Map<Object, Object> newConfig) {

            }

            @Override
            public void saveButtonConfig(int slot, boolean value) {

            }

            @Override
            public Map<Integer, Boolean> getButtonsConfig() {
                return Map.of();
            }

            @Override
            public boolean getBoolean(String path) {
                return false;
            }

            @Override
            public String getString(String path) {
                return config.getOrDefault(path, "");
            }

            @Override
            public List<String> getStringList(String path) {
                return List.of();
            }

            @Override
            public List<Map<String, String>> getStringMapList(String path) {
                return List.of();
            }

            @Override
            public int getInt(String path) {
                return Integer.valueOf(this.config.get(path));
            }

            @Override
            public double getDouble(String path) {
                return Double.valueOf(this.config.get(path));
            }
        };
    }
}
