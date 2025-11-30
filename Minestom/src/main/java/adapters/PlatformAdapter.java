package adapters;

import lib.abstractions.IConsole;
import lib.abstractions.IPlayer;
import lib.gui.components.IInventory;
import lib.gui.components.IItemStack;
import lib.gui.components.ITextInput;
import lib.gui.components.recipes.RecipeInventory;
import lib.scheduler.IScheduler;
import adapters.inventory.MinestomInventory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlatformAdapter implements lib.abstractions.PlatformAdapter {
    public static boolean onlineMode = false;
    public PlatformAdapter(boolean onlineMode) {
        PlatformAdapter.onlineMode=onlineMode;
    }
    @Override
    public IPlayer getPlayer(String name) {
        Player p = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(name);
        if (p == null) {
            return null;
        }
        return new PlayerAdapter(p);
    }

    @Override
    public IPlayer getPlayerByUUID(UUID uuid) {
        Player p = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (p == null) {
            return null;
        }
        return new PlayerAdapter(p);
    }

    @Override
    public List<IPlayer> getOnlinePlayers() {
        Collection<Player> players = MinecraftServer.getConnectionManager().getOnlinePlayers();
        return players.stream().map(PlayerAdapter::new).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public IItemStack createItemStack(lib.gui.components.recipes.RecipeItem recipeItem) {
        ItemStack item= MaterialAdapter.createItem(recipeItem);
        return new ItemStackAdapter(item);
    }

    @Override
    public IInventory createInventory(RecipeInventory recipeInventory) {
        return new InventoryAdapter(new MinestomInventory(recipeInventory));
    }

    //enviar un comando a la consola del servidor
    @Override
    public void dispatchCommand(String command) throws Exception {
//not implemented
    }

    @Override
    public void sendPluginMessage(String channel, byte[] message) {
//not implemented
    }

    @Override
    public IScheduler getScheduler() {
        return new Scheduler();
    }

    @Override
    public IConsole getConsole() {
        return new Console();
    }

    @Override
    public File getDataFolder() {
        return new File("BlockDynastyEconomy"); //verificar ruta utilizada por complementos de Minestom
    }

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public boolean isOnlineMode() {
        return onlineMode;
    }

    @Override
    public boolean hasSupportAdventureText() {
        return false;
    }

    @Override
    public ITextInput getTextInput() {
        return new TextInput();
    }
}
