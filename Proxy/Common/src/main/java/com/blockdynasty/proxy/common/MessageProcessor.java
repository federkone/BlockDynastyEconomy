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

package com.blockdynasty.proxy.common;

import com.blockdynasty.utils.cryptography.CryptoUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.file.Path;
import java.util.*;

public abstract class MessageProcessor {
    public static final String CHANNEL_NAME = "proxy:blockdynasty";
    private final Logger logger;
    private ConfigFile config;
    private final Path dataDirectory;

    public MessageProcessor(Logger logger, Path dataDirectory) {
        this.logger = logger;
        this.config = new ConfigFile(dataDirectory);
        this.dataDirectory = dataDirectory;
    }

    public void processMessage(Message message){
        if (!isValidChannel(message.getChannelName())) {
            return;
        }
        if(!message.isServerMessage()){
            return;
        }
        message.markAsHandled();

        try {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(message.getData()));
            String jsonMessage = dataInputStream.readUTF();
            Gson gson = new Gson();
            Map<String, String> messageData = gson.fromJson(jsonMessage, new TypeToken<Map<String, String>>(){}.getType());

            String type = messageData.get("type");
            switch (type) {
                case "event":
                case "account":
                    String target = messageData.get("target");
                    UUID uuid = UUID.fromString(target);
                    forwardMessageEvent(uuid,message);
                    break;
                case "currency":
                    forwardMessageCurrency(message);
                    break;
                case "syncData":
                    forwardMessageSyncData(messageData, message);
                    break;
                default:
                    logger.logWarning("Unknown message type received: " + type);
            }
        }catch (Exception e){
            logger.logError("Error reading message channel: "+e.getMessage());
        }
    }
    private boolean isValidChannel(String channel){
        return channel.equalsIgnoreCase(CHANNEL_NAME);
    }
    private void forwardMessageSyncData(Map<String, String> messageData, Message message){
        try {
            Gson gson = new Gson();
            Map<Object, Object> configOriginal = config.getConfig();

            Map<Object,Object> configData = new HashMap<>(configOriginal);
            configData.remove("hashCredentials");
            configData.remove("whitelist");

            Yaml yaml = new Yaml();
            String configAsYaml = yaml.dump(configData);

            String salt = config.getString("hashCredentials.salt");
            boolean hashEnabled = config.getBoolean("hashCredentials.enable");
            List<String> allowedServers = config.getStringList("whitelist.allowedServers");
            boolean needCheckWhitelist = !allowedServers.isEmpty();

            String dataSend = "Undefined";
            if (hashEnabled){
                dataSend = CryptoUtils.encrypt(configAsYaml, salt);
            }else{
                dataSend = configAsYaml;
            }
            messageData.put("data", dataSend);

            String modifiedJson = gson.toJson(messageData);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(modifiedJson);
            byte[] modifiedData = byteArrayOutputStream.toByteArray();

            String target = messageData.get("target");
            UUID uuid = UUID.fromString(target);

            this.forwardMessageSyncData(uuid, needCheckWhitelist, allowedServers,new ModifiedMessage(message, modifiedData));
        }catch (Exception e){
            logger.logError("->> Error processing syncData message: " + e.getMessage());
        }
    }
    public void updateConfig(){
        this.config = new ConfigFile(dataDirectory);
    }

    /**
     * Forwards a message to a player target is not same original sender server.
     * @param playerId
     * @param message
     */
    public abstract void forwardMessageEvent(UUID playerId,Message message);

    /**
     * Forwards a message to all servers except original sender server.
     * @param message
     */
    public abstract void forwardMessageCurrency(Message message);

    /**
     * Forwards a message to a player target inclusive original sender server, but only if the server is in the whitelist (if enabled).
     * @param playerId
     * @param needCheckWhitelist
     * @param allowedServers
     * @param message
     */
    public abstract void forwardMessageSyncData(UUID playerId, boolean needCheckWhitelist, List<String> allowedServers, Message message);
}
