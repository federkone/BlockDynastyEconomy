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

package net.blockdynasty.economy.gui.commands.templates.users;

import net.blockdynasty.economy.gui.commands.abstractions.AbstractCommand;
import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;

public class BankGUICommand extends AbstractCommand {

    public BankGUICommand() {
        super("bank", "BlockDynastyEconomy.players.bank");
    }

    @Override
    public boolean execute(IEntityCommands sender, String[] args) {
        if(!super.execute( sender, args)){
            return false;
        }

        IEntityGUI entityGUI = sender.asEntityGUI();
        if (entityGUI != null) {
            GUIFactory.bankPanel(entityGUI).open();
        }else {
            sender.sendMessage("Bank GUI is only available for players.");
        }

        return true;
    }
}
