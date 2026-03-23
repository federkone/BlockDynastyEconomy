/**
 * Copyright 2026 Federico Barrionuevo "@federkone"
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

package com.blockdynasty.velocity;

import com.blockdynasty.yaml.YamlConfig;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class Config extends YamlConfig {
    private Map<Object, Object> config;
    private final String templatePath = "config.yaml";
    private final File configFile;

    public Config(Path dataDirectory) {
        this.configFile =dataDirectory.resolve("config.yaml").toFile();
        if (!this.configFile.exists()) {
            this.config = createNewFile(configFile, templatePath);
        } else {
            this.config = loadFile(configFile, templatePath);
        }
    }

    public boolean getBoolean(String path) {
        Boolean value = get(path, config, Boolean.class);
        return value != null && value;
    }
    public String getString(String path) {
        String value = get(path,config, String.class);
        return value == null ? "Unknown" : value;
    }
    public Map<Object, Object> getConfig() {
        return config;
    }

}
