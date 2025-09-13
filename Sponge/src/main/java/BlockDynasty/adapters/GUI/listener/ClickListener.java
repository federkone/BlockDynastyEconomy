package BlockDynasty.adapters.GUI.listener;

import BlockDynasty.adapters.abstractions.EntityPlayerAdapter;
import lib.gui.GUIFactory;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.menu.ClickType;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.item.inventory.menu.handler.SlotClickHandler;

public class ClickListener implements SlotClickHandler {

    @Override
    public boolean handle(Cause cause, Container container, Slot slot, int slotIndex, ClickType<?> clickType) {
        cause.first(ServerPlayer.class).ifPresent(player -> {
            if (ClickTypes.CLICK_LEFT.get().equals(clickType)){
                GUIFactory.getGuiService().handleClick(EntityPlayerAdapter.of(player), lib.gui.abstractions.ClickType.LEFT, slotIndex);
            } else if (ClickTypes.CLICK_RIGHT.get().equals(clickType)) {
                GUIFactory.getGuiService().handleClick(EntityPlayerAdapter.of(player), lib.gui.abstractions.ClickType.RIGHT, slotIndex);
            }
        });
        return true; // true = cancelar acción
    }
}