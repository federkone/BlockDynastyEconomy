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

package BlockDynasty.BukkitImplementation.adapters.listeners;

import BlockDynasty.BukkitImplementation.adapters.abstractions.EntityPlayerAdapter;
import listeners.IPlayerJoin;
import org.bukkit.entity.Player;

public class PlayerJoinListenerOffline extends PlayerJoinListenerOnline {

    private IPlayerJoin playerJoin;
    public PlayerJoinListenerOffline(IPlayerJoin playerJoin) {
        super(playerJoin);
        this.playerJoin = playerJoin;
    }

    //si se comienza a trabajar en offline se van a buscar las cuentas por nombre y se va a preguntar si cambio el uuid para actualizar en sistema.
    @Override
    protected void loadPlayerAccount(Player player){
       this.playerJoin.loadOfflinePlayerAccount(EntityPlayerAdapter.of(player));
    }
}
