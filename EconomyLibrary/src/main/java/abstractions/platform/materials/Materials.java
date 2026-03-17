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

package abstractions.platform.materials;

/**
 * Enum representing common materials used across GUIs
 */
public enum Materials {
    // Basic materials
    GLASS_PANE,
    BLUE_STAINED_GLASS_PANE,
    RED_CONCRETE,
    LIME_CONCRETE,
    EMERALD_BLOCK,
    GOLD_NUGGET,
    LIME_DYE,
    NETHER_STAR,
    STONE,
    //AIR,

    // Item indicators
    PAPER,
    BOOK,
    WRITABLE_BOOK,
    CHEST,
    ENDER_CHEST,
    BARRIER,
    NAME_TAG,

    // Navigation
    ARROW,

    // Action items
    EMERALD,
    DIAMOND,
    GOLD_INGOT,
    REDSTONE,

    // Head items
    PLAYER_HEAD,

    //WOOLS
    WHITE_WOOL,
    RED_WOOL,
    PURPLE_WOOL,
    ORANGE_WOOL,
    LIME_WOOL,
    GRAY_WOOL,
    LIGHT_GRAY_WOOL,
    CYAN_WOOL,
    LIGHT_BLUE_WOOL,
    BLUE_WOOL,
    BROWN_WOOL,
    GREEN_WOOL,
    PINK_WOOL,
    YELLOW_WOOL,


    // --- NUEVOS ELEMENTOS (Compatibles con 1.8) ---

// Minerals & Ores (Minerales)
    COAL_ORE,
    IRON_ORE,
    GOLD_ORE,
    DIAMOND_ORE,
    EMERALD_ORE,
    REDSTONE_ORE,
    LAPIS_ORE,
    NETHER_QUARTZ_ORE,
    COAL,
    IRON_INGOT,
    QUARTZ,
    LAPIS_LAZULI,

// Building Blocks (Bloques de construcción)
    COBBLESTONE,
    OAK_PLANKS,
    SPRUCE_PLANKS,
    BIRCH_PLANKS,
    JUNGLE_PLANKS,
    ACACIA_PLANKS,
    DARK_OAK_PLANKS,
    BRICKS,
    STONE_BRICKS,
    SANDSTONE,
    RED_SANDSTONE,
    OBSIDIAN,
    GLASS,
    BOOKSHELF,
    NETHER_BRICKS,
    QUARTZ_BLOCK,

// Natural Blocks (Bloques naturales)
    DIRT,
    COARSE_DIRT,
    //GRASS_BLOCK, // En la API 1.8 pura esto era simplemente "GRASS"
    SAND,
    RED_SAND,
    GRAVEL,
    OAK_LOG,
    SPRUCE_LOG,
    BIRCH_LOG,
    JUNGLE_LOG,
    ACACIA_LOG,
    DARK_OAK_LOG,
    //OAK_LEAVES, // En la API 1.8 pura era "LEAVES" y "LEAVES_2"
    SPONGE,
    CACTUS,
    PUMPKIN,
    MELON,
    SUGAR_CANE,
    CHARCOAL,
    COAL_BLOCK,
    IRON_BLOCK,
    GOLD_BLOCK,
    LAPIS_BLOCK,
    REDSTONE_BLOCK,
    DIAMOND_BLOCK,

    GRANITE,
    POLISHED_GRANITE,
    DIORITE,
    POLISHED_DIORITE,
    ANDESITE,
    POLISHED_ANDESITE,
    BEDROCK,
    PODZOL,
    GRASS_BLOCK,
    MYCELIUM,
    //DIRT_PATH, // Conocido como Grass Path (1.9), pero útil incluirlo si usas API moderna. Si es 1.8 estricto, quítalo.
    CLAY,
    WET_SPONGE,

    WATER_LILY_PAD,
    VINE,
    CARVED_PUMPKIN,
    JACK_O_LANTERN,
    BROWN_MUSHROOM,
    RED_MUSHROOM,
    MUSHROOM_STEM,
    BROWN_MUSHROOM_BLOCK,
    RED_MUSHROOM_BLOCK,
    COBWEB,
    TALL_GRASS,
    FERN,
    DEAD_BUSH,

    SNOW,
    SNOW_BLOCK,
    ICE,
    PACKED_ICE,

    NETHERRACK,
    SOUL_SAND,
    GLOWSTONE,
    END_STONE,
    END_PORTAL_FRAME,
    DRAGON_EGG;

    public static Materials match(String value) {
       if (value==null) return GOLD_INGOT;
       for (Materials m : Materials.values()){
              if(m.name().equalsIgnoreCase(value)){
                return m;
              }
       }
       return GOLD_INGOT;
    }
}