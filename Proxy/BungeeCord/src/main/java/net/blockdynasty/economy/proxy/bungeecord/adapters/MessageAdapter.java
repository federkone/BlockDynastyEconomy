/**
 * Copyright 2026 Federico Barrionuevo "@federkone"
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

package net.blockdynasty.economy.proxy.bungeecord.adapters;

import net.blockdynasty.economy.proxy.common.Message;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;

public class MessageAdapter implements Message {
    private PluginMessageEvent event;

    public MessageAdapter(PluginMessageEvent event) {
        this.event = event;
    }

    @Override
    public String getChannelName() {
        return event.getTag();
    }

    @Override
    public boolean isServerMessage() {
        return event.getSender() instanceof Server;
    }

    @Override
    public byte[] getData() {
        return event.getData();
    }

    @Override
    public String getServerSourceName() {
        return ((Server) event.getSender()).getInfo().getName();
    }

    @Override
    public void markAsHandled() {
        event.setCancelled(true);
    }
}
