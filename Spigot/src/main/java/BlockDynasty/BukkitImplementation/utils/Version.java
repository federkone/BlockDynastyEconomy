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

package BlockDynasty.BukkitImplementation.utils;

import org.bukkit.Bukkit;

import java.util.Arrays;

public class Version {
    private static final String currentVersion = org.bukkit.Bukkit.getBukkitVersion();
    private static final boolean hasAdventure = JavaUtil.classExists("net.kyori.adventure.text.Component")&&JavaUtil.classExists("net.kyori.adventure.text.minimessage.MiniMessage");

    public static boolean match(String version) {
        return currentVersion.startsWith(version);
    }

    public static boolean match(String... versions) {
        return Arrays.stream(versions).anyMatch(currentVersion::startsWith);
    }

    public static boolean isMohist() {
        return JavaUtil.classExists("com.mohistmc.MohistMC");
    }
    /**
     *  this is useful for item Creations
     **/
    public static boolean isLegacy() {
        return Version.match("1.8","1.9","1.10","1.11","1.12");
    }

    /**
     * Check if the server supports Adventure Text API
     */
    public static boolean hasSupportAdventureText() {
        return hasAdventure;
    }
}