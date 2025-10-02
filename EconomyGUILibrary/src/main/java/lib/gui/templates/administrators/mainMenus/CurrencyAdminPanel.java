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

package lib.gui.templates.administrators.mainMenus;

import lib.gui.GUIFactory;
import lib.gui.components.IGUI;
import lib.gui.components.IEntityGUI;
import lib.gui.components.Materials;
import lib.gui.components.abstractions.AbstractPanel;

public class CurrencyAdminPanel extends AbstractPanel {
    private final IEntityGUI player;

    public CurrencyAdminPanel(IEntityGUI player, IGUI parent) {
        super("Currency Manager", 3,player,parent);
        this.player = player;
        setupGUI();
    }

    private void setupGUI() {
        // Create Currency button
        setItem(10, createItem(Materials.EMERALD, "Create Currency",
                "Click to create new currency"), unused -> {
            GUIFactory.createCurrencyPanel(player,this);

        });

        // Delete Currency button
        setItem(12, createItem(Materials.REDSTONE, "Delete Currency",
                "Click to delete currency"), unused -> {
            GUIFactory.currencyListToDeletePanel(player, this).open();
        });

        // Edit Currency button
        setItem(14, createItem(Materials.BOOK, "Edit Currency",
                "Click to edit Currency"), unused -> {
            GUIFactory.currencyListToEditPanel(player, this).open();
        });

        // Toggle Features button
        setItem(16, createItem(Materials.PAPER, "Config Features",
                "Click"), unused -> {
            player.sendMessage("[Bank] This feature is not implemented yet.");
        });

        // Exit button
        setItem(22, createItem(Materials.BARRIER, "Back",
                "Click to go back"), unused -> {
            this.openParent();
        });
    }

}