package BlockDynasty.GUI.listener;

import BlockDynasty.GUI.adapters.PlayerAdapter;
import lib.GUIFactory;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.menu.handler.CloseHandler;

public class CloseListener implements CloseHandler {

    @Override
    public void handle(Cause cause, Container container) {
        cause.first(ServerPlayer.class).ifPresent(player -> {
            GUIFactory.getGuiService().unregisterGUI(new PlayerAdapter((ServerPlayer) player));
        });
    }
}
