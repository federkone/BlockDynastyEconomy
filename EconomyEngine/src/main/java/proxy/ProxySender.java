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
    private final ProxyData proxyData;
    private final Gson gson = new Gson();

    public ProxySender(ProxyData proxyData, PlatformAdapter platformAdapter) {
        this.platformAdapter = platformAdapter;
        this.proxyData = proxyData;
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
            platformAdapter.sendPluginMessage(proxyData.getChannelName(), createMessage(type, data, target));
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
            proxyData.setAdditionalData(out);
            out.writeUTF(jsonMessage);
        }

        return outBytes.toByteArray();
    }
}
/*public abstract class ProxySender implements Courier {
    private PlatformAdapter platformAdapter;
    private ProxyData proxyData;


    public ProxySender(ProxyData proxyData, PlatformAdapter platformAdapter) {
        this.platformAdapter = platformAdapter;
        this.proxyData = proxyData;
    }

    @Override
    public void sendUpdateMessage(String type, String target){
        if(type.equals("account")) {
            IPlayer player = platformAdapter.getPlayerByUUID(UUID.fromString(target));
            if(player != null && player.isOnline()) {
                return;
            }
        }

        try {
            Map<String, String> messageData = new HashMap<>();
            messageData.put("type", type);
            messageData.put("target", target);
            String jsonMessage = new Gson().toJson(messageData);

            // Crear mensaje para BungeeCord
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            try (DataOutputStream out = new DataOutputStream(outBytes)) {
                setAdditionalData(out);
                out.writeUTF(jsonMessage);
            }

            platformAdapter.sendPluginMessage(proxyData.getChannelName(), outBytes.toByteArray());

        } catch (IOException e) {
            Console.logError(e.getMessage());
        }

    }
    @Override
    public void sendUpdateMessage(String type, String data,String target){
        if(type.equals("event")) {
            IPlayer player = platformAdapter.getPlayerByUUID(UUID.fromString(target));
            if(player != null && player.isOnline()) {
                return;
            }
        }
        try {
            Map<String, String> messageData = new HashMap<>();
            messageData.put("type", type);
            messageData.put("target", target);
            messageData.put("data", data);

            String jsonMessage = new Gson().toJson(messageData);

            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            try (DataOutputStream out = new DataOutputStream(outBytes)) {
                setAdditionalData(out);
                out.writeUTF(jsonMessage);
            }

            platformAdapter.sendPluginMessage(proxyData.getChannelName(), outBytes.toByteArray());

        } catch (IOException e) {
            Console.logError(e.getMessage());
        }
    }

    private void setAdditionalData(DataOutputStream out) throws IOException {
        proxyData.setAdditionalData(out);
    }

}*/
