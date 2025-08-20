package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import BlockDynasty.BukkitImplementation.GUI.RegisterGuiModule;
import BlockDynasty.BukkitImplementation.GUI.services.GUIService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractGUI implements IGUI {
    private final GUIService guiService= RegisterGuiModule.getGuiService();
    private final Inventory inventory;
    private final Player owner;
    private final String title;
    private final Map<Integer, Consumer<Player>> actions = new HashMap<>();
    private IGUI parent; // Optional parent GUI for nested GUIs

    public AbstractGUI(String title, int rows,Player owner) {
        this.title = title;
        this.owner = owner;
        parent= null; // Initialize parent as null
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
        clearGui();
    }

    public AbstractGUI(String title, int rows, Player owner, IGUI parent) {
        this(title, rows, owner);
        this.parent = parent; // Set the parent GUI if provided
    }

    @Override
    public void open() {
        owner.openInventory(inventory);
        guiService.registerGUI(owner, this); //esto reemplaza automaticamente el GUI anterior ya que esta basado en un mapa con key del jugador
    }

    @Override
    public void openParent() {
        if (hasParent()) {
            parent.open();
        }
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public IGUI getParent(){
        return parent;
    }

    @Override
    public void handleClick(int slot, Player player) {
        Consumer<Player> action = actions.get(slot);
        if (action != null) {
            action.accept(player);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getTitle() {
        return title;
    }

    protected void setItem(int slot, ItemStack item, Consumer<Player> action) {
        inventory.setItem(slot, item);
        if (item == null || action == null) {
            actions.remove(slot);
        } else {
            actions.put(slot, action);
        }
    }

    protected ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        return createItem(item, name, lore);
    }

    protected ItemStack createItem(ItemStack base, String name, String... lore) {
        ItemMeta meta = base.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            base.setItemMeta(meta);
        }
        return base;
    }

    protected ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        return createItem(item, name, lore);
    }

    protected ItemStack createItem(ItemStack base, String name, List<String> lore) {
        ItemMeta meta = base.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore);
            }
            base.setItemMeta(meta);
        }
        return base;
    }

    private void fill() {
        ItemStack filler = MaterialAdapter.getPanelGlass();

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            setItem(slot, filler, null);   //filler can be null for empty slots
        }
    }
    protected void clearGui(){
        fill();
    }
}