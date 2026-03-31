package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.platformAdapter.messages;

import net.blockdynasty.economy.libs.services.configuration.IConfiguration;
import net.blockdynasty.economy.spigot.BukkitImplementation.utils.Version;


public class MessageSenderFactory {
    private static IMessageSender messageSender;

    public static void createMessageSender(IConfiguration configuration) {
        if (Version.hasSupportAdventureText() && !configuration.getBoolean("forceVanillaColorsSystem")) {
            messageSender = new MessageSenderModern();
        } else {
            messageSender = new MessageSenderVanilla();
        }
    }

    public static IMessageSender getMessageSender() {
        if (messageSender == null) {
            throw new IllegalStateException("MessageSenderFactory has not been initialized. Call createMessageSender() first.");
        }
        return messageSender;
    }
}
