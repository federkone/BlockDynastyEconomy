package BlockDynasty.GUI.services;

import BlockDynasty.GUI.components.IGUI;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIService {
    private final Map<UUID, IGUI> openGUIs = new HashMap<>();

    public void registerGUI(ServerPlayer player, IGUI gui) {
        openGUIs.put(player.uniqueId(), gui);
    }

    public void unregisterGUI(ServerPlayer player) {
        openGUIs.remove(player.uniqueId());
    }

    public void refresh(UUID playerId) {
        IGUI gui = openGUIs.get(playerId);
        if (gui != null) {
            gui.refresh();
        }
    }
}