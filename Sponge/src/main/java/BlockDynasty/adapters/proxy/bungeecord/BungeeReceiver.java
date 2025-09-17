package BlockDynasty.adapters.proxy.bungeecord;

import BlockDynasty.SpongePlugin;
import proxy.ProxyData;
import proxy.ProxyReceiver;

import java.io.DataInputStream;
import java.io.IOException;

public class BungeeReceiver  extends ProxyReceiver {
    private final BungeeData data = new BungeeData();

    @Override
    public boolean isValidChannel(DataInputStream in) throws IOException {
        String subchannel = in.readUTF();
        return subchannel.equals(data.getSubChannelName());
    }

    public void register() {
        //WARNING THIS CODE ONLY WORKS ON SPONGE SERVER 1.23, IN VERSION 1.20.1 NOT WORKS
        SpongePlugin.getChannel().play().addHandler((buf, connection) -> {
            super.onPluginMessageReceived(data.getChannelName(), buf.readBytes(buf.available()));
        });
    }
}