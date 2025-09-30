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
import BlockDynasty.Economy.aplication.useCase.account.SearchAccountUseCase;
import BlockDynasty.Economy.aplication.useCase.currency.SearchCurrencyUseCase;
import api.IApi;
import org.bukkit.Bukkit;

public class PlaceHolder {
    private static PlaceHolderExpansion expansion;

    public static void register(lib.placeholder.PlaceHolder placeHolder){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            Console.log("PlaceholderAPI not found. Expansion won't be loaded.");
            return;
        }
        expansion = new PlaceHolderExpansion(placeHolder);
        expansion.register();
        Console.log("PlaceholderAPI Expansion registered successfully!");
    }

    public static void unregister(){
        if(expansion != null){
            expansion.unregister();
        }
    }
}
