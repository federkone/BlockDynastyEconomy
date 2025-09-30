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

package BlockDynasty.BukkitImplementation.adapters.proxy;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.utils.Console;
import proxy.ProxyData;

public class ChannelRegister {

    public static void init(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, ProxyData.getChannelName());  //outgoing channel
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin,ProxyData.getChannelName(), new ProxyReceiverImp());  //incoming channel
        Console.log("Proxy Message channel has been initialized.");
    }

    public static void unhook(BlockDynastyEconomy plugin) {
        plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin, ProxyData.getChannelName());
        plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin, ProxyData.getChannelName());
    }
}
