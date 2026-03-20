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

package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.atributes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class SetAtributeModern implements  SetAtributesStrategy {
    @Override
    public void setDisplayName(ItemMeta meta, String displayName) {
        meta.displayName(MiniMessage.miniMessage().deserialize(displayName));
    }

    @Override
    public void setLore(ItemMeta meta, List<String> lore) {
        List<Component> loreComponents = lore.stream()
                .map(m ->  MiniMessage.miniMessage().deserialize(m))
                .collect(Collectors.toList());
        meta.lore(loreComponents);
    }
}
