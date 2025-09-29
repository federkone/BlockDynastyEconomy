package lib.gui.abstractions;

import java.util.UUID;

public interface IGUIService {
    void registerGUI(IEntityGUI player, IGUI gui);
    void unregisterGUI(IEntityGUI player);
    boolean hasOpenedGUI(IEntityGUI player);
    void handleClick(IEntityGUI player, ClickType clickType, int indexSlot);
    void refresh(UUID playerId);
}
