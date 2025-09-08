package lib.commands.abstractions;

import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.IPlayer;
import lib.gui.abstractions.Materials;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlatformAdapter {
    Source getPlayer(String name);
    void dispatchCommand(String command);

    IItemStack createItemStack(Materials material);
    IInventory createInventory(String title, int rows);

    Source getPlayerByUUID(UUID uuid);
    Optional<IPlayer> getPlayerOnlineByUUID(UUID uuid);
    List<IPlayer> getOnlinePlayers();
}
