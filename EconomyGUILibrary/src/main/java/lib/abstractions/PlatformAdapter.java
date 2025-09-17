package lib.abstractions;

import lib.gui.abstractions.IInventory;
import lib.gui.abstractions.IItemStack;
import lib.gui.abstractions.Materials;

import java.util.List;
import java.util.UUID;

public interface PlatformAdapter {
    IPlayer getPlayer(String name);
    IPlayer getPlayerByUUID(UUID uuid);
    List<IPlayer> getOnlinePlayers();

    IItemStack createItemStack(Materials material);
    IInventory createInventory(String title, int rows);
    void dispatchCommand(String command) throws Exception;

    void sendPluginMessage(String channel, byte[] message);
    void executeAsync(Runnable task);

}
