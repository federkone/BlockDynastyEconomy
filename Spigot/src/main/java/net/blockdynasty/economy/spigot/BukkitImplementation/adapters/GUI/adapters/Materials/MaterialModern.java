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

package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.Materials;

import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MaterialModern implements MaterialService {
    private static final Material FALLBACK = Material.STONE;
    private final Map<Materials,Material> MATERIAL_MAP;

    public MaterialModern() {
        this.MATERIAL_MAP = getMaterialMap();
    }

    @Override
    public Map<Materials, Material> getMaterialMap() {
        Map<Materials, Material> MATERIAL_MAP = new HashMap<>();
        for (Materials material : Materials.values()) {
            try {
                Material bukkitMaterial = Material.valueOf(material.name());
                MATERIAL_MAP.put(material, bukkitMaterial);
            } catch (IllegalArgumentException e) {
                MATERIAL_MAP.put(material, FALLBACK);
            }
        }
        return MATERIAL_MAP;
    }

    @Override
    public Material toBukkitMaterial(Materials material) {
        return MATERIAL_MAP.getOrDefault(material, FALLBACK);
    }

    @Override
    public boolean isPlayerHead(Material material) {
        return material == Material.PLAYER_HEAD;
    }

    @Override
    public ItemStack createItemStack(Materials materials) {
        return new ItemStack(toBukkitMaterial(materials));
    }
}
