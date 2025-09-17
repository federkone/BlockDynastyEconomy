package BlockDynasty.adapters.proxy.velocity;

import BlockDynasty.SpongePlugin;
import proxy.ProxyData;
import proxy.ProxyReceiver;

public class VelocityReceiver  extends ProxyReceiver {

    public void register(ProxyData data) {
        //WARNING THIS CODE ONLY WORKS ON SPONGE SERVER 1.23, IN VERSION 1.20.1 NOT WORKS
        SpongePlugin.getChannel().play().addHandler((buf, connection) -> {
            super.onPluginMessageReceived(data.getChannelName(), buf.readBytes(buf.available()));
        });
    }
}