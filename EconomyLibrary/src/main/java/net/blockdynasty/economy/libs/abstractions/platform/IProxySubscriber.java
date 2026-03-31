package net.blockdynasty.economy.libs.abstractions.platform;

public interface IProxySubscriber {
    void onPluginMessageReceived(String channel, byte[] bytecode);
}
