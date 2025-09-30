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

package BlockDynasty.BukkitImplementation.adapters.GUI.listener;

import BlockDynasty.BukkitImplementation.adapters.abstractions.EntityPlayerAdapter;
import lib.gui.GUIFactory;
import lib.gui.abstractions.ClickType;
import lib.gui.abstractions.IEntityGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        IEntityGUI sender = EntityPlayerAdapter.of((Player) event.getWhoClicked());
        int slot = event.getRawSlot();
        if (!GUIFactory.getGuiService().hasOpenedGUI(sender)) return;
        event.setCancelled(true);
        if (event.isLeftClick()) {
            GUIFactory.getGuiService().handleClick(sender, ClickType.LEFT, slot);
        } else if (event.isRightClick()) {
            GUIFactory.getGuiService().handleClick(sender, ClickType.RIGHT, slot);
        }
    }
}