package lib.components;

import java.util.UUID;

public interface IGUIService {
    void registerGUI(IPlayer player, IGUI gui);
    void unregisterGUI(IPlayer player);
    boolean hasOpenedGUI(IPlayer player);
    void handleClick(IPlayer player, ClickType clickType, int indexSlot);
    void refresh(UUID playerId);
}
