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

package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.Materials;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData.NBTService;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.NBTData.NBTServiceFactory;
import BlockDynasty.BukkitImplementation.adapters.GUI.adapters.customTexture.*;
import BlockDynasty.BukkitImplementation.utils.ItemSerialization;
import BlockDynasty.BukkitImplementation.utils.Version;

import abstractions.platform.materials.Materials;
import abstractions.platform.recipes.RecipeItem;
import domain.entity.currency.NbtData;
import domain.entity.currency.RecipeItemCurrency;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings( "deprecation")
public class ItemStackProvider {
    private static ItemTextureService itemTextureService = new ItemTextureServiceNull();
    private static NBTService nbtService = NBTServiceFactory.get();
    private static MaterialService materialService;


    static {
        if(Version.hasSupportCustomProfile()){
            itemTextureService = new ItemTextureServiceModern();
        }else{
            if(Version.hasMojangAuthLib()) {
                itemTextureService = new ItemTextureServiceVanilla();
            }
        }
        if(Version.isLegacy()){
            materialService= new MaterialLegacy();
        }else{
            materialService= new MaterialModern();
        }
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

        if (!Version.hasSupportAdventureText() || BlockDynastyEconomy.getConfiguration().getBoolean("forceVanillaColorsSystem")){
            meta.setDisplayName(displayName);
        }else {
            meta.displayName(MiniMessage.miniMessage().deserialize(displayName));
        }

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

    public static void applyItemLore(ItemStack item, String[] loreArray){
        if (loreArray == null || loreArray.length == 0) return;

        List<String> lore = List.of(loreArray);
        ItemMeta meta= item.getItemMeta();

        if (!Version.hasSupportAdventureText() || BlockDynastyEconomy.getConfiguration().getBoolean("forceVanillaColorsSystem")){
                meta.setLore(lore);
        }else {
            List<Component> loreComponents = lore.stream()
                    .map(m ->  MiniMessage.miniMessage().deserialize(m))
                    .collect(Collectors.toList());
            meta.lore(loreComponents);
        }

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

    public static ItemStack createItemStackCurrency(RecipeItemCurrency recipeItem) {
        ItemStack itemStack = createItem(recipeItem);
        applyNBTData(itemStack, recipeItem.getNbtData());
        applyItemName(itemStack, recipeItem.getName());
        applyItemLore(itemStack ,recipeItem.getLore());
        applyTexture(itemStack, recipeItem.getTexture());
        return itemStack;
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