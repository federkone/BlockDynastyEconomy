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

package BlockDynasty.BukkitImplementation.adapters.abstractions;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.utils.Version;
import lib.commands.abstractions.IEntityCommands;
import lib.gui.components.IEntityGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.ConsoleCommandSender;

import java.util.UUID;

//Adapter para CommandSender que no son jugadores (Consola, Bloque de comandos, etc)
public class EntityConsoleAdapter implements IEntityCommands {
    private final ConsoleCommandSender commandSender;
    private EntityConsoleAdapter(ConsoleCommandSender commandSender) {
        this.commandSender = commandSender;
    }

    public static EntityConsoleAdapter of(ConsoleCommandSender commandSender) {
        return new EntityConsoleAdapter(commandSender);
    }
    @Override
    public String getName() {
        return "BlockDynasty";
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public void sendMessage(String message) {
        if (!Version.hasSupportAdventureText() || BlockDynastyEconomy.getConfiguration().getBoolean("forceVanillaColorsSystem")){
            message = translateColorCodes(message);
            commandSender.sendMessage(message);
        }else {
            Component textonuevo = MiniMessage.miniMessage().deserialize(message);
            commandSender.sendMessage(textonuevo);
        }
    }

    private String translateColorCodes(String message) {
        return message.replaceAll("&([0-9a-fk-or])", "");
    }

    @Override
    public void playNotificationSound() {

    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void kickPlayer(String message) {

    }

    @Override
    public IEntityGUI asEntityGUI() {
        return null;
    }

    @Override
    public Object getRoot() {
        return commandSender;
    }

}
