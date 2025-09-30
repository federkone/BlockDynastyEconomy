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

package proxy;

import BlockDynasty.Economy.domain.services.courier.Courier;
import Main.Console;
import com.google.gson.Gson;
import lib.abstractions.IPlayer;
import lib.abstractions.PlatformAdapter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProxySender implements Courier {
    private final PlatformAdapter platformAdapter;

    private final Gson gson = new Gson();

    public ProxySender( PlatformAdapter platformAdapter) {
        this.platformAdapter = platformAdapter;
    }

    @Override
    public void sendUpdateMessage(String type, String target) {
        sendUpdateMessage(type, null, target);
    }

    @Override
    public void sendUpdateMessage(String type, String data, String target) {
        if (shouldSkipProcessing(type, target)) {
            return;
        }
        try {
            platformAdapter.sendPluginMessage(ProxyData.getChannelName(), createMessage(type, data, target));
        } catch (IOException e) {
            Console.logError(e.getMessage());
        }
    }

    private boolean shouldSkipProcessing(String type, String target) {
        switch (type) {
            case "account":
            case "event":
                IPlayer player = platformAdapter.getPlayerByUUID(UUID.fromString(target));
                return player != null && player.isOnline();
            default:
                return false;
        }
    }

    private byte[] createMessage(String type, String data, String target) throws IOException {
        // Create the message data map
        Map<String, String> messageData = new HashMap<>();
        messageData.put("type", type);
        messageData.put("target", target);
        if (data != null) {
            messageData.put("data", data);
        }

        String jsonMessage = gson.toJson(messageData);

        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        try (DataOutputStream out = new DataOutputStream(outBytes)) {
            out.writeUTF(jsonMessage);
        }

        return outBytes.toByteArray();
    }
}
