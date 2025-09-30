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

package files;

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
    private final String templatePath = "config-template.yaml";
    private final String databasePath = "/database";
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

            // Directly copy the template file to preserve comments
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
                if (inputStream == null) {
                    throw new IOException("Template resource not found: " + templatePath);
                }
                Files.copy(inputStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // Load the config after creating it
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
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config "+ e.getMessage());
        }
    }

    public void saveConfig() {
        try {
            // Create a temporary copy of the current file to preserve comments
            File tempFile = new File(configFile.getParentFile(), "temp-config.yaml");
            if (configFile.exists()) {
                Files.copy(configFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // Update values
            DumperOptions options = new DumperOptions();
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setIndent(2);

            Yaml yaml = new Yaml(options);
            String content = yaml.dump(config);

            Files.write(configFile.toPath(), content.getBytes(StandardCharsets.UTF_8));

            // Cleanup
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config", e);
        }
    }

    public Map<String, Object> getConfig() {
        return config;
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