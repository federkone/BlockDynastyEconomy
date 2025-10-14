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

package platform.files;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private Map<String, Object> config;
    private final String databasePath = "/database";
    private final String templatePath = "config-template.yaml";
    private final String configName = "config.yaml";
    private final String logsPath = "/logs";
    private final File configFile;
    private final File rootDirectory;

    public Configuration(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        this.configFile = new File(rootDirectory, configName);
        //preguntar por la existencia de /database directorio y crearlo si no existe
        File databaseDir = new File(rootDirectory, databasePath);
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
        if (!this.configFile.exists()) {
            createNewConfigFile();
        } else {
            loadConfig();
        }
    }

    private void createNewConfigFile() {
        try {
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
                if (inputStream == null) {
                    throw new IOException("Template resource not found: " + templatePath);
                }
                Files.copy(inputStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config file", e);
        }
    }

    private void loadConfig() {
        try {
            Yaml yaml = new Yaml();
            config = yaml.load(Files.newInputStream(configFile.toPath()));
            if (config == null) {
                config = new HashMap<>();
            }

            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
                if (inputStream == null) {
                    throw new IOException("Template resource not found: " + templatePath);
                }
                Map<String, Object> defaultConfig = yaml.load(inputStream);
                if (defaultConfig != null) {

                    if(checkIfNeedUpdate(defaultConfig, config)) {
                        mergeConfigs(defaultConfig, config);

                        //guardar el config actualizado
                        DumperOptions options = new DumperOptions();
                        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                        options.setProcessComments(true);
                        options.setPrettyFlow(true);
                        options.setIndent(2);
                        Yaml yamlWriter = new Yaml(options);
                        String yamlString = yamlWriter.dump(config);
                        Files.write(configFile.toPath(), yamlString.getBytes(StandardCharsets.UTF_8));
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + e.getMessage(), e);
        }
    }

    public boolean checkIfNeedUpdate(Map<String, Object> defaultConfig, Map<String, Object> config) {
        for (Map.Entry<String, Object> entry : defaultConfig.entrySet()) {
            String key = entry.getKey();
            Object defaultValue = entry.getValue();

            if (!config.containsKey(key)) {
                return true;
            }

            if (defaultValue instanceof Map && config.get(key) instanceof Map) {
                return checkIfNeedUpdate((Map<String, Object>) defaultValue, (Map<String, Object>) config.get(key));
            }
        }
        return false;
    }

    public void mergeConfigs(Map<String, Object> defaultConfig, Map<String, Object> config) {
        for (Map.Entry<String, Object> entry : defaultConfig.entrySet()) {
            String key = entry.getKey();
            Object defaultValue = entry.getValue();

            if (!config.containsKey(key)) {
                config.put(key, defaultValue);
            } else {
                Object currentValue = config.get(key);
                if (defaultValue instanceof Map && currentValue instanceof Map) {
                    mergeConfigs((Map<String, Object>) defaultValue, (Map<String, Object>) currentValue);
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T get(String path, Class<T> type) {
        String[] parts = path.split("\\.");
        Map<String, Object> current = config;

        for (int i = 0; i < parts.length - 1; i++) {
            if (!current.containsKey(parts[i])) return null;
            current = (Map<String, Object>) current.get(parts[i]);
        }

        String lastPart = parts[parts.length - 1];
        if (!current.containsKey(lastPart)) return null;

        Object value = current.get(lastPart);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    public boolean getBoolean(String path) {
        Boolean value = get(path, Boolean.class);
        return value != null && value;
    }

    public String getString(String path) {
        return get(path, String.class);
    }

    public int getInt(String path) {
        Integer value = get(path, Integer.class);
        return value != null ? value : 0;
    }

    public double getDouble(String path) {
        Double value = get(path, Double.class);
        return value != null ? value : 0.0;
    }

    public String getDatabasePath() {
        return rootDirectory.getAbsolutePath()+ databasePath;
    }

    public String getLogsPath() {
        return  rootDirectory.getAbsolutePath()+ logsPath;
    }
}