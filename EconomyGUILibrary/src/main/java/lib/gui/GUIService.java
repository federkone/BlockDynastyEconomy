package lib.gui;

import lib.gui.abstractions.ClickType;
import lib.gui.abstractions.IGUI;
import lib.gui.abstractions.IGUIService;
import lib.gui.abstractions.IEntityGUI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIService implements IGUIService {
    private final Map<UUID, IGUI> openGUIs = new HashMap<>();

    public void registerGUI(IEntityGUI player, IGUI gui) {
        openGUIs.put(player.getUniqueId(), gui);
    }

    public void unregisterGUI(IEntityGUI player) {
        openGUIs.remove(player.getUniqueId());
    }

    public boolean hasOpenedGUI(IEntityGUI player) {
        return  openGUIs.containsKey(player.getUniqueId());
    }

    public void handleClick(IEntityGUI player, ClickType clickType, int indexSlot){
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