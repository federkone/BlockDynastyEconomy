package lib;

import lib.components.ClickType;
import lib.components.IGUI;
import lib.components.IGUIService;
import lib.components.IPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIService implements IGUIService {
    private final Map<UUID, IGUI> openGUIs = new HashMap<>();

    public void registerGUI(IPlayer player, IGUI gui) {
        openGUIs.put(player.getUniqueId(), gui);
    }

    public void unregisterGUI(IPlayer player) {
        openGUIs.remove(player.getUniqueId());
    }

    public boolean hasOpenedGUI(IPlayer player) {
        return  openGUIs.containsKey(player.getUniqueId());
    }

    public void handleClick(IPlayer player, ClickType clickType, int indexSlot){
        IGUI gui = openGUIs.get(player.getUniqueId());
        if (gui != null) {
            if (ClickType.LEFT == clickType) {
                gui.handleLeftClick(indexSlot, player);
            } else if (ClickType.RIGHT == clickType) {
                gui.handleRightClick(indexSlot, player);

            }
        }
    }

    public void refresh(UUID playerId) {
        IGUI gui = openGUIs.get(playerId);
        if (gui != null) {
            gui.refresh();
        }
    }
}