package BlockDynasty.BukkitImplementation.adapters.platformAdapter.messages;

import BlockDynasty.BukkitImplementation.utils.Version;
import services.configuration.IConfiguration;

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
