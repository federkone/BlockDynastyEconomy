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

import BlockDynasty.BukkitImplementation.adapters.abstractions.EntityPlayerAdapter;
import lib.placeholder.PlaceHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;



public class PlaceHolderExpansion extends PlaceholderExpansion {
    private final PlaceHolder placeHolder;

    public PlaceHolderExpansion(PlaceHolder placeHolder) {
        this.placeHolder = placeHolder;
    }

    @Override
    public boolean register() {
        if(!canRegister()){
            return false;
        }
        return super.register();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "BlockDynastyEconomy";
    }

    @Override
    public String getAuthor() {
        return "Nullplague";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getRequiredPlugin(){
        return "BlockDynastyEconomy";
    }

    @Override
    public String onRequest(OfflinePlayer player, String s) {
        return placeHolder.onRequest(EntityPlayerAdapter.of(player.getPlayer()), s);
    }

}