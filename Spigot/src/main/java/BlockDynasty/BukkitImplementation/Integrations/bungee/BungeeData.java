package BlockDynasty.BukkitImplementation.Integrations.bungee;

import proxy.ProxyData;

import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeData implements ProxyData {
    private final String subChannelName = "BlockDynastyEconomy Data Channel";
    private final String channelName = "BungeeCord";

    public String getChannelName() {
        return channelName;
    }

    public String getSubChannelName() {
        return subChannelName;
    }

    @Override
    public void setAdditionalData(DataOutputStream out) throws IOException {
        out.writeUTF("Forward");
        out.writeUTF("ONLINE");
        out.writeUTF(this.subChannelName);
    }
}
