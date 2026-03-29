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

package BlockDynasty.BukkitImplementation.Integrations.Placeholder;

import BlockDynasty.BukkitImplementation.utils.Console;
import com.blockdynasty.economy.Economy;
import lib.placeholder.IPlaceHolderDynastyEconomy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class PlaceHolder {
    private static PlaceHolderExpansion expansion;
    private static PlaceHolderListener listener;

    public static void register(JavaPlugin plugin) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Console.log("PlaceholderAPI not found. Expansion won't be loaded.");
            return;
        }
        Optional<IPlaceHolderDynastyEconomy> placeHolder= Economy.getPlaceholder();
        if (placeHolder.isEmpty()) {
            Console.log("IPlaceHolderDynastyEconomy service not found. Expansion won't be loaded.");
            return;
        }

        expansion = new PlaceHolderExpansion(placeHolder.get());
        expansion.register();
        listener = new PlaceHolderListener(expansion);
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        Console.log("PlaceholderAPI Expansion registered successfully!");
    }

    public static void unregister(){
        if(expansion != null){
            expansion.unregister();
            HandlerList.unregisterAll(listener);
        }
    }
}
