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

package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter;

import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter.messages.IMessageSender;
import net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter.messages.MessageSenderFactory;
import org.bukkit.command.ConsoleCommandSender;

import java.util.UUID;

//Adapter para CommandSender que no son jugadores (Consola, Bloque de comandos, etc)
public class EntityConsoleAdapter implements IEntityCommands {
    private final ConsoleCommandSender commandSender;
    private final IMessageSender messageSender;

    private EntityConsoleAdapter(ConsoleCommandSender commandSender) {
        this.commandSender = commandSender;
        this.messageSender=MessageSenderFactory.getMessageSender();
    }

    public static EntityConsoleAdapter of(ConsoleCommandSender commandSender) {
        return new EntityConsoleAdapter(commandSender);
    }
    @Override
    public String getName() {
        return "net/blockdynasty/economy/spigot";
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
        messageSender.sendMessage(this.commandSender, message);
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
