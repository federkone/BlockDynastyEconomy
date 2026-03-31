package net.blockdynasty.economy.engine.configFromChannel;

import net.blockdynasty.economy.core.domain.services.courier.Message;
import net.blockdynasty.economy.libs.abstractions.platform.IProxySubscriber;
import net.blockdynasty.economy.engine.Economy;
import net.blockdynasty.economy.engine.MessageChannel.proxy.ProxyData;
import net.blockdynasty.economy.engine.platform.files.IConfigurationEngine;
import net.blockdynasty.utils.cryptography.CryptoUtils;
import org.yaml.snakeyaml.Yaml;
import net.blockdynasty.economy.libs.services.Console;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProxyConfigSubscriber implements IProxySubscriber {
    private Economy economy;
    private IConfigurationEngine configuration;

    public ProxyConfigSubscriber(Economy economy,IConfigurationEngine configuration) {
        this.economy = economy;
        this.configuration = configuration;
    }

    @Override
    @SuppressWarnings("Unchecked")
    public void onPluginMessageReceived(String channel, byte[] bytecode) {
        if (!channel.equals(ProxyData.getChannelName())) {
            return;
        }
        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytecode))) {
            String messageString = in.readUTF();

            Message message = Message.builder()
                    .fromJson(messageString)
                    .build();

            if (message.getType() == Message.Type.SYNC_DATA) {
                Console.log("Sync data received from proxy");

                String data= message.getData();
                Yaml yaml = new Yaml();
                Map<Object, Object> newConfig = new HashMap<>();

                if(configuration.getBoolean("hashCredentials.enable")){
                    String salt = configuration.getString("hashCredentials.salt");
                    try {
                        String decryptedYaml = CryptoUtils.decrypt(data, salt);
                        newConfig = yaml.load(decryptedYaml);
                    }catch (Exception e){
                        Console.logError("Failed to decrypt data: " + e.getMessage());
                    }
                }else{
                    newConfig = yaml.load(data);
                }

                configuration.updateConfig(newConfig);
                economy.startServer(configuration);
            }
        }catch (IOException exception){
            Console.logError(exception.getMessage());
        }
    }
}
