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

package BlockDynasty.BukkitImplementation.adapters.GUI.adapters;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials.MaterialService;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials.MaterialServiceFactory;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData.NBTService;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData.NBTServiceFactory;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.atributes.SetAtributesFactory;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.atributes.SetAtributesStrategy;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.customTexture.*;

import abstractions.platform.materials.Materials;
import abstractions.platform.recipes.RecipeItem;
import domain.entity.currency.NbtData;
import domain.entity.currency.RecipeItemCurrency;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import services.configuration.IConfiguration;

import java.util.*;

@SuppressWarnings( "deprecation")
public class ItemStackProvider {
    private static ItemTextureService itemTextureService;
    private static NBTService nbtService;
    private static SetAtributesStrategy setAtributesStrategy;
    private static MaterialService materialService;

    public static void init(IConfiguration configuration, JavaPlugin plugin){
        itemTextureService= ItemTextureServiceFactory.getItemTextureService();
        materialService = MaterialServiceFactory.getMaterialService();
        nbtService = NBTServiceFactory.get(plugin);
        setAtributesStrategy = SetAtributesFactory.getStrategy(configuration.getBoolean("forceVanillaColorsSystem"));
    }

    public static Material toBukkitMaterial(Materials material) {
        return materialService.toBukkitMaterial(material);
    }

    public static void applyItemName(ItemStack item, String displayName){
        if (displayName == null) return;
        ItemMeta meta;

        if (isPlayerHead(item.getType())) {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            if (isValidPlayerName(displayName)){
                skullMeta.setOwner(displayName);
            }
            meta = skullMeta;
        } else {
            meta = item.getItemMeta();
        }
        setAtributesStrategy.setDisplayName(meta, displayName);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

    public static void applyItemLore(ItemStack item, String[] loreArray){
        if (loreArray == null || loreArray.length == 0) return;

        List<String> lore = List.of(loreArray);
        ItemMeta meta= item.getItemMeta();

        setAtributesStrategy.setLore(meta, lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

    public static void applyTexture(ItemStack item, String textureUrl) {
        itemTextureService.applyTexture(item, textureUrl);
    }

    public static ItemStack createItemStack(RecipeItem recipeItem) {
        ItemStack itemStack = createItem(recipeItem);
        applyItemName(itemStack, recipeItem.getName());
        applyItemLore(itemStack ,recipeItem.getLore());
        applyTexture(itemStack, recipeItem.getTexture());
        return itemStack;
    }

    public static ItemStack createItemStackNBT(RecipeItemCurrency recipeItem) {
        ItemStack itemStack = createItem(recipeItem);
        applyNBTData(itemStack, recipeItem.getNbtData());
        applyItemName(itemStack, recipeItem.getName());
        applyItemLore(itemStack ,recipeItem.getLore());
        applyTexture(itemStack, recipeItem.getTexture());
        return itemStack;
    }

    public static ItemStack creteItemStackBase64(RecipeItemCurrency recipeItem) {
        return ItemSerialization.fromBase64(recipeItem.getBase64ITEM());
    }

    private static ItemStack createItem(RecipeItem recipeItem) {
        if (recipeItem.getBase64ITEM() != null && !recipeItem.getBase64ITEM().isEmpty()) {
            ItemStack itemStack = ItemSerialization.fromBase64(recipeItem.getBase64ITEM());
            if (itemStack.getType() == Material.AIR) {
                return materialService.createItemStack(recipeItem.getMaterial());
            }
            return itemStack;
        }else{
            return materialService.createItemStack(recipeItem.getMaterial());
        }
    }

    public static void applyNBTData(ItemStack itemStack,NbtData nbtData){
        if (nbtData == null) return;
        nbtService.applyNBTData(itemStack, nbtData);
    }

    public static NbtData getNBTData(ItemStack itemStack){
        return nbtService.getNBTData(itemStack);
    }

    private static boolean isValidPlayerName(String name) {
        if (name == null) return false;
        return name.matches("^[a-zA-Z0-9_]{3,16}$");
    }
    public static boolean isPlayerHead(Material material) {
       return materialService.isPlayerHead(material);
    }
}