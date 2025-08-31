package lib.templates.abstractions;

import lib.components.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AbstractGUI implements IGUI {
    protected static PlatformAdapter platformAdapter; //todas las guis van a compartir el mismo adapter
    protected static IGUIService guiService;
    protected IInventory inventory;
    protected final IPlayer owner;
    protected IGUI parent;
    protected final int rows;
    protected final String title;
    protected final Map<Integer, IItemStack> items = new HashMap<>();
    protected final Map<Integer, Consumer<IPlayer>> leftClickActions = new HashMap<>();
    protected final Map<Integer, Consumer<IPlayer>> rightClickActions = new HashMap<>();

    public interface PlatformAdapter {
        IItemStack createItemStack(Materials material, String name, String... lore);
        IInventory createInventory(AbstractGUI gui);

        java.util.Optional<IPlayer> getPlayerOnlineByUUID(java.util.UUID uuid);
        List<IPlayer> getOnlinePlayers();
    }
    // Set the platform adapter at startup
    public static void setPlatformAdapter(PlatformAdapter adapter,IGUIService guiServices) {
        platformAdapter = adapter;
        guiService = guiServices;
    }

    public AbstractGUI(String title, int rows, IPlayer owner) {
        this.title = title;
        this.owner = owner;
        this.rows = rows;
        this.parent = null;
        fill();
        recreateInventory();
    }
    public AbstractGUI(String title, int rows, IPlayer owner, IGUI parent) {
        this(title, rows, owner);
        this.parent = parent;
    }

    private void recreateInventory() { //create inventory and usage in open and in constructor
        this.inventory =platformAdapter.createInventory(this);
        items.forEach((key, value) -> inventory.set(key, value));
    }

    @Override
    public void close() {
        owner.closeInventory();
        guiService.unregisterGUI(owner);
    }

    @Override
    public void open() {
        recreateInventory();
        owner.openInventory(this);
        guiService.registerGUI(owner, this);
    }

    @Override
    public void openParent() {
        if (hasParent()) {
            parent.open();
        } else {
            owner.closeInventory();
        }
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public IGUI getParent() {
        return this.parent;
    }

    @Override
    public IPlayer getOwner() {
        return this.owner;
    }

    @Override
    public void handleRightClick(int slot, IPlayer player) {
        Consumer<IPlayer> action = rightClickActions.get(slot);
        if (action != null) {
            action.accept(player);
        }
    }

    @Override
    public void handleLeftClick(int slot, IPlayer player) {
        Consumer<IPlayer> action = leftClickActions.get(slot);
        if (action != null) {
            action.accept(player);
        }
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public void fill() {
        IItemStack blueGlass = createItem(Materials.BLUE_STAINED_GLASS_PANE, " " ," ");
        IItemStack filler = createItem(Materials.GLASS_PANE, " "," ");

        int rows = (getRows()*9) / 9;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {
                int slot = row * 9 + col;
                if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                    items.put(slot, blueGlass);
                } else {
                    items.put(slot, filler);
                }
            }
        }

    }

    public void setInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return this.inventory;
    }

    @Override
    public void refresh() {
    }

    public IGUIService getGuiService() {
        return guiService;
    }

    protected void setItem(int slot, IItemStack item, Consumer<IPlayer> leftClickAction) {
        items.put(slot, item);
        inventory.set(slot, item);
        if (item == null || leftClickAction == null) {
            leftClickActions.remove(slot);
        } else {
            leftClickActions.put(slot, leftClickAction);
        }
    }
    protected void setItem(int slot, IItemStack item, Consumer<IPlayer> leftClickAction, Consumer<IPlayer> rightClickAction) {
        setItem(slot, item, leftClickAction);

        if (item == null || rightClickAction == null) {
            rightClickActions.remove(slot);
        } else {
            rightClickActions.put(slot, rightClickAction);
        }
    }
    protected IItemStack createItem(Materials material, String name, String... lore) {
        return platformAdapter.createItemStack(material, name, lore);
    }


}
