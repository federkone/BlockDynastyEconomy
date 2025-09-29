package lib.gui.templates.abstractions;

import lib.gui.abstractions.IEntityGUI;
import lib.abstractions.PlatformAdapter;
import lib.gui.abstractions.*;

import java.util.*;
import java.util.function.Consumer;

public class AbstractGUI implements IGUI {
    protected static PlatformAdapter platformAdapter; //todas las guis van a compartir el mismo adapter
    protected static IGUIService guiService;
    protected IInventory inventory;
    protected final IEntityGUI owner;
    protected IGUI parent;
    protected final Map<Integer, IItemStack> items = new HashMap<>();
    protected final Map<Integer, Consumer<IEntityGUI>> leftClickActions = new HashMap<>();
    protected final Map<Integer, Consumer<IEntityGUI>> rightClickActions = new HashMap<>();

    // Set the platform adapter at startup
    public static void setPlatformAdapter(PlatformAdapter adapter,IGUIService guiServices) {
        platformAdapter = adapter;
        guiService = guiServices;
    }

    public AbstractGUI(String title, int rows, IEntityGUI owner) {
        createInventory(title, rows);
        fill();
        this.owner = owner;
        this.parent = null;
    }
    public AbstractGUI(String title, int rows, IEntityGUI owner, IGUI parent) {
        this(title, rows, owner);
        this.parent = parent;
    }
    private void createInventory(String title, int rows) {
        this.inventory =platformAdapter.createInventory(title, rows);
        this.inventory.setRows(rows);
        this.inventory.setTitle(title);
        items.forEach((key, value) -> inventory.set(key, value));
    }
    protected IItemStack createItem(Materials material, String name, String... lore) {
        IItemStack item =platformAdapter.createItemStack(material);
        item.setDisplayName(name);
        item.setLore(Arrays.asList(lore));
        return item;
    }
    protected void setItem(int slot, IItemStack item, Consumer<IEntityGUI> leftClickAction) {
        items.put(slot, item);
        inventory.set(slot, item);
        if (item == null || leftClickAction == null) {
            leftClickActions.remove(slot);
        } else {
            leftClickActions.put(slot, leftClickAction);
        }
    }
    protected void setItem(int slot, IItemStack item, Consumer<IEntityGUI> leftClickAction, Consumer<IEntityGUI> rightClickAction) {
        setItem(slot, item, leftClickAction);

        if (item == null || rightClickAction == null) {
            rightClickActions.remove(slot);
        } else {
            rightClickActions.put(slot, rightClickAction);
        }
    }


    @Override
    public void close() {
        owner.closeInventory();
        guiService.unregisterGUI(owner);
    }

    @Override
    public void open() {
        createInventory(inventory.getTitle(), inventory.getRows());
        owner.openInventory(this.inventory);
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
    public void handleRightClick(int slot, IEntityGUI player) {
        Consumer<IEntityGUI> action = rightClickActions.get(slot);
        if (action != null) {
            action.accept(player);
            player.playSuccessSound();
        }else {player.playFailureSound();}
    }

    @Override
    public void handleLeftClick(int slot, IEntityGUI player) {
        Consumer<IEntityGUI> action = leftClickActions.get(slot);
        if (action != null) {
            action.accept(player);
            player.playSuccessSound();
        }else{player.playFailureSound();}
    }

    @Override
    public String getTitle(){
        return this.inventory.getTitle();
    }

    @Override
    public int getRows(){
        return this.inventory.getRows();
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

    public IInventory getInventory() {
        return this.inventory;
    }

    @Override
    public void refresh() {
    }
}
