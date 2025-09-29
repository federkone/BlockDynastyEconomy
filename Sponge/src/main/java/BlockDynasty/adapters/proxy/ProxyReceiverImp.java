package BlockDynasty.adapters.proxy;

import org.spongepowered.api.network.channel.raw.RawDataChannel;
import proxy.ProxyData;
import proxy.ProxyReceiver;


//WARNING THIS CODE ONLY WORKS ON SPONGE SERVER 1.23, IN VERSION 1.20.1 NOT WORKS
public class ProxyReceiverImp extends ProxyReceiver {

    public static ProxyReceiverImp register() {
        return new ProxyReceiverImp();
    }

    public void addHandler(RawDataChannel channel){
        channel.play().addHandler((buf, connection) -> {
            super.onPluginMessageReceived(ProxyData.getChannelName(), buf.readBytes(buf.available()));
        });
    }
}