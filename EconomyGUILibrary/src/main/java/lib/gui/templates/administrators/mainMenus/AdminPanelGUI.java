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
import lib.gui.abstractions.IEntityGUI;
import lib.gui.abstractions.Materials;
import lib.gui.templates.abstractions.AbstractGUI;

public class AdminPanelGUI extends AbstractGUI {
    private final IEntityGUI sender;

    public AdminPanelGUI(IEntityGUI sender)
    {
        super("Economy Admin Panel", 5,sender);
        this.sender = sender;

        initializeButtons();
    }

    private void initializeButtons() {
        setItem(20, createItem(Materials.EMERALD, "Edit Currencies", "Click to edit currencies"), event -> {
            GUIFactory.currencyPanel( sender, this).open();
        });


        setItem(24, createItem(Materials.PLAYER_HEAD, "Manage Accounts", "Click to manage accounts"), event -> {
            GUIFactory.accountPanel( sender, this).open();
        });

        setItem(40, createItem(Materials.BARRIER, "Exit", "Click to exit"), event -> {
            this.close();
        });


    }
}