package BlockDynasty.adapters.GUI.adapters;

import BlockDynasty.SpongePlugin;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.ITextInput;
import net.kyori.adventure.text.TextComponent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.message.PlayerChatEvent;

import java.util.function.Function;

public class TextInput implements ITextInput {
    @Override
    public void open(IPlayer owner, String title, String initialText, Function<String, String> function) {
        owner.sendMessage("§6[" + title + "] §f" + initialText);
        owner.sendMessage("§7Type your response in chat. Type 'cancel' to cancel.");
        owner.closeInventory();

        TextInput.ChatInputListener listener = new TextInput.ChatInputListener(null,owner, title, function);

        Sponge.eventManager().registerListeners(SpongePlugin.getPlugin(), listener);

    }

    @Override
    public void open(IGUI parent, IPlayer owner, String title, String initialText, Function<String, String> function) {

        owner.sendMessage("§6[" + title + "] §f" + initialText);
        owner.sendMessage("§7Type your response in chat. Type 'cancel' to cancel. Type 'back' to go back.");
        owner.closeInventory();

        TextInput.ChatInputListener listener = new TextInput.ChatInputListener(parent,owner, title, function);

        Sponge.eventManager().registerListeners(SpongePlugin.getPlugin(), listener);

    }

    public static class ChatInputListener {
        private final IGUI parent;
        private final IPlayer owner;
        private final String title;
        private final Function<String, String> function;

        public ChatInputListener(IGUI parent, IPlayer owner, String title, Function<String, String> function) {
            this.owner = owner;
            this.title = title;
            this.function = function;
            this.parent = parent;
        }

        @Listener(order = Order.EARLY)
        public void onChat(PlayerChatEvent.Submit event) {
            if (!(event.cause().root() instanceof ServerPlayer)) {
                return;
            }

            ServerPlayer player = (ServerPlayer) event.cause().root();
            if (!player.uniqueId().equals(owner.getUniqueId())) {
                return;
            }
            event.setCancelled(true);
            TextComponent component = (TextComponent) event.message();
            String input = component.content();
            if (input.equalsIgnoreCase("cancel")) {
                owner.sendMessage("§cOperation cancelled.");
                Sponge.eventManager().unregisterListeners(this);
                return;
            }
            if (parent != null) {
                if (input.equalsIgnoreCase("back")) {
                    parent.open();
                    Sponge.eventManager().unregisterListeners(this);
                    return;
                }
            }
            String response = function.apply(input);
            if (response == null) {
                Sponge.eventManager().unregisterListeners(this);
                return;
            }
            owner.sendMessage("§6[" + title + "] §f" + response);
            Sponge.eventManager().unregisterListeners(this);
        }

        @Listener(order = Order.EARLY)
        public void onCommand(ExecuteCommandEvent.Pre event) {
            Sponge.eventManager().unregisterListeners(this);
        }
    }
}
