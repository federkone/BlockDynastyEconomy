package BlockDynasty.BukkitImplementation.adapters.GUI.adapters;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;
import lib.scheduler.ContextualTask;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.function.Function;

public class TextInputChat implements ITextInput {

    @Override
    public void open(IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        sendInitialMessages(owner, title, initialText, false);
        Bukkit.getPluginManager().registerEvents(new ChatInputListener(null, owner, title, function), getPlugin());
    }

    @Override
    public void open(IGUI parent, IEntityGUI owner, String title, String initialText, Function<String, String> function) {
        sendInitialMessages(owner, title, initialText, true);
        Bukkit.getPluginManager().registerEvents(new ChatInputListener(parent, owner, title, function), getPlugin());
    }

    private void sendInitialMessages(IEntityGUI owner, String title, String initialText, boolean hasParent) {
        owner.sendMessage("[" + title + "] " + initialText);
        owner.sendMessage("Type your response in chat.");
        owner.sendMessage( "Type 'cancel' to cancel." + (hasParent ? " Type 'back' to go back." : ""));
        owner.closeInventory();
    }

    private Plugin getPlugin() {
        return BlockDynastyEconomy.getInstance();
    }

    public class ChatInputListener implements Listener {
        private final IGUI parent;
        private final IEntityGUI owner;
        private final String title;
        private final Function<String, String> function;
        private final UUID playerUUID;

        public ChatInputListener(IGUI parent, IEntityGUI owner, String title, Function<String, String> function) {
            this.parent = parent;
            this.owner = owner;
            this.title = title;
            this.function = function;
            this.playerUUID = owner.getUniqueId();
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onChat(AsyncPlayerChatEvent event) {
            if (!event.getPlayer().getUniqueId().equals(playerUUID)) return;

            event.setCancelled(true);
            String message = event.getMessage();

            Scheduler.run(ContextualTask.build( () -> {
                if (message.equalsIgnoreCase("cancel")) {
                    owner.sendMessage( "Operation cancelled.");
                    HandlerList.unregisterAll(this);
                    return;
                }

                if (parent != null && message.equalsIgnoreCase("back")) {
                    parent.open();
                    HandlerList.unregisterAll(this);
                    return;
                }

                String result = function.apply(message);
                if (result != null) {
                    owner.sendMessage( "[" + title + "] "+ result);
                }

                HandlerList.unregisterAll(this);
            }));

        }

        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent event) {
            if (event.getPlayer().getUniqueId().equals(playerUUID)) {
                HandlerList.unregisterAll(this);
            }
        }
    }
}