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

package net.blockdynasty.economy.gui.gui.components;

import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeInventory;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IScheduler;

import java.util.List;

public interface PlatformGUI  {
    IItemStack createItemStack(RecipeItem recipeItem);
    IInventory createInventory(RecipeInventory recipeInventory);
    ITextInput getTextInput();
    IPlayer getPlayer( String name);
    void dispatchCommand( String command) throws Exception;
    IScheduler getScheduler();
    List<IPlayer> getOnlinePlayers();
    boolean hasSupportGui();
}
