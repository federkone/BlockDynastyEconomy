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

package net.blockdynasty.economy.libs.services.configuration;

import java.util.List;
import java.util.Map;

public interface IConfiguration {

    boolean getBoolean(String path);
    String getString(String path);
    List<String> getStringList(String path);
    List<Map<String, String>> getStringMapList(String path);
    int getInt(String path);
    double getDouble(String path);
}
