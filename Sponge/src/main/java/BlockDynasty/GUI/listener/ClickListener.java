package BlockDynasty.GUI.listener;

import BlockDynasty.GUI.adapters.SpongePlayer;
import lib.templates.abstractions.AbstractGUI;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.menu.ClickType;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.item.inventory.menu.handler.SlotClickHandler;

public class ClickListener implements SlotClickHandler {
    public AbstractGUI gui;

    public ClickListener(AbstractGUI gui) {
        this.gui = gui;
    }
    @Override
    public boolean handle(Cause cause, Container container, Slot slot, int slotIndex, ClickType<?> clickType) {
        cause.first(ServerPlayer.class).ifPresent(player -> {
            if (ClickTypes.CLICK_LEFT.get().equals(clickType)){
                gui.getGuiService().handleClick(new SpongePlayer(player), lib.components.ClickType.LEFT, slotIndex);
            } else if (ClickTypes.CLICK_RIGHT.get().equals(clickType)) {
                gui.getGuiService().handleClick(new SpongePlayer(player), lib.components.ClickType.RIGHT, slotIndex);
            }
        });
        return true; // true = cancelar acción
    }
}