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

package BlockDynasty.BukkitImplementation.Integrations.vault;

import BlockDynasty.BukkitImplementation.Integrations.vaultUnloked.VaultUnlockedHandler;
import BlockDynasty.BukkitImplementation.Integrations.vault2.Vault2Handler;
import BlockDynasty.BukkitImplementation.utils.JavaUtil;
import BlockDynasty.BukkitImplementation.utils.Console;
import com.BlockDynasty.api.DynastyEconomy;
import com.blockdynasty.economy.Economy;
import net.blockdynasty.providers.services.ServiceProvider;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Vault {
    private static final BlockDynasty.BukkitImplementation.BlockDynastyEconomy plugin = BlockDynasty.BukkitImplementation.BlockDynastyEconomy.getInstance();
    private static List<IVaultHandler> vaultsInjected = new ArrayList<>();
    private static Optional<DynastyEconomy> api;
    
    public static void init() {
        Vault.vaultsInjected = new ArrayList<>();
        api = ServiceProvider.get(DynastyEconomy.class, service -> service.getId().equals(Economy.getApiWithVaultLoggerId()));
        if (api.isEmpty()) {
            Console.log("No economy API found. Vault integration will not be enabled.");
            return;
        }
        if (!BlockDynasty.BukkitImplementation.BlockDynastyEconomy.getConfiguration().getBoolean("vault")) {
            Console.log("Vault integration is disabled.");
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Console.log("Vault plugin not found. Vault integration will not be enabled.");
            return;
        }

        //VAULT 2.0 CHECK
        if(JavaUtil.classExists("net.milkbowl.vault.economy.EconomyMultiCurrency")) {
            vaultsInjected.add(new Vault2Handler(plugin,api.get()));
        }else{
            //DEFAULT VAULT V1.7
            vaultsInjected.add(new VaultHandler(plugin,api.get()));
        }

        //VAULT UNLOCKED CHECK
        if(JavaUtil.classExists("net.milkbowl.vault2.economy.Economy")){
            vaultsInjected.add(new VaultUnlockedHandler(plugin,api.get()));
        }

        vaultsInjected.forEach(IVaultHandler::hook);
    }

    public static void unhook(){
        vaultsInjected.forEach(IVaultHandler::unhook);
    }
}
