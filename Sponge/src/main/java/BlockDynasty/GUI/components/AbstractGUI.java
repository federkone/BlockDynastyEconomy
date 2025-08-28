package BlockDynasty.GUI.components;

import BlockDynasty.GUI.RegisterGuiModule;
import BlockDynasty.GUI.services.GUIService;
import BlockDynasty.SpongePlugin;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.item.inventory.menu.ClickType;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.menu.handler.SlotClickHandler;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.plugin.PluginContainer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AbstractGUI implements IGUI{
    private final PluginContainer pluginContainer = SpongePlugin.getPlugin();
    private final GUIService guiService= RegisterGuiModule.getGuiService();

    private InventoryMenu inventoryMenu;
    private ViewableInventory viewableInventory;
    private Inventory inventory;

    private final ServerPlayer owner;
    private final String title;
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Map<Integer, Consumer<ServerPlayer>> leftClickActions = new HashMap<>();
    private final Map<Integer, Consumer<ServerPlayer>> rightClickActions = new HashMap<>();
    private IGUI parent; // Optional parent GUI for nested GUIs

    public AbstractGUI(String title, int rows,ServerPlayer owner) {
        System.out.println(" CREANDO ABSTRACCION DE GUI");
        this.title = title;
        this.owner = owner;
        this.parent= null;
        viewableInventory= ViewableInventory.builder()
                .type(getTypeFromRows(rows))
                .completeStructure()
                .plugin(this.pluginContainer)
                .build();
        this.inventory = viewableInventory;

        this.inventoryMenu =InventoryMenu.of(viewableInventory).setReadOnly(true);
        this.inventoryMenu.setTitle(Component.text(title));
        this.inventoryMenu.registerSlotClick(new MyClickHandler());

        clearGui();
    }

    public AbstractGUI(String title, int rows,ServerPlayer owner, IGUI parent) {
        this( title, rows, owner);
        this.parent= parent; // Set the provided parent GUI
    }

    @Override
    public void close() {
        owner.closeInventory();
        guiService.unregisterGUI(owner);
    }

    private void reCreate(){
        this.items.forEach((key, value) -> viewableInventory.set(key, value));
    }
    @Override
    public void open() {
        inventoryMenu.open(owner);
        reCreate();
        guiService.registerGUI(owner, this);
    }

    @Override
    public void openParent() {
        if (hasParent()) {
            parent.open();
        }else {owner.closeInventory();} //method close??
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
    public void handleRightClick(int slot, ServerPlayer player) {
        Consumer<ServerPlayer> action = rightClickActions.get(slot);
        if (action != null) {
            action.accept(player);
        }
    }

    @Override
    public void handleLeftClick(int slot, ServerPlayer player) {
        Consumer<ServerPlayer> action = leftClickActions.get(slot);
        if (action != null) {
            action.accept(player);
        }
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void refresh() {

    }

    protected void setItem(int slot, ItemStack item, Consumer<ServerPlayer> leftClickAction) {
        inventory.set(slot, item);
        items.put(slot, item);

        if (item == null || leftClickAction == null) {
            leftClickActions.remove(slot);
        } else {
            leftClickActions.put(slot, leftClickAction);
        }
    }

    protected void setItem(int slot, ItemStack item, Consumer<ServerPlayer>leftClickAction ,Consumer<ServerPlayer> rightClickAction) {
        inventory.set(slot, item);
        items.put(slot, item);
        if (item == null || rightClickAction == null) {
            rightClickActions.remove(slot);
        } else {
            rightClickActions.put(slot, rightClickAction);
        }

        if (item == null || leftClickAction == null) {
            leftClickActions.remove(slot);
        } else {
            leftClickActions.put(slot, leftClickAction);
        }
    }

    protected ItemStack createItem(ItemType type, String name, String... lore) {
        ItemStack item = ItemStack.builder().itemType(type).build();

        item.offer(Keys.CUSTOM_NAME, Component.text(name));

        List<Component> loreComponents = Arrays.stream(lore)
                    .map(Component::text) // convierte cada String a un Component
                    .collect(Collectors.toList());

        item.offer(Keys.LORE, loreComponents);

        return item;
    }

    private void fill() {
        ItemStack filler = ItemStack.builder().itemType(ItemTypes.GLASS_PANE).build();
        filler.offer(Keys.CUSTOM_NAME, Component.text(""));
        filler.offer(Keys.LORE, Collections.emptyList());
        ItemStack blueGlass = ItemStack.builder().itemType(ItemTypes.BLUE_STAINED_GLASS_PANE).build();
        blueGlass.offer(Keys.CUSTOM_NAME, Component.text(""));
        blueGlass.offer(Keys.LORE, Collections.emptyList());

        int rows = inventory.capacity() / 9;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {
                int slot = row * 9 + col;
                if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                    setItem(slot, blueGlass, null);
                } else {
                    setItem(slot, filler, null);
                }
            }
        }
    }
    protected void clearGui(){
        fill();
    }
    private ContainerType getTypeFromRows(int rows) {
        switch (rows) {
            case 1:
                return ContainerTypes.GENERIC_9X1.get();
            case 2:
                return ContainerTypes.GENERIC_9X2.get();
            case 3:
                return ContainerTypes.GENERIC_9X3.get();
            case 4:
                return ContainerTypes.GENERIC_9X4.get();
            case 5:
                return ContainerTypes.GENERIC_9X5.get();
            case 6:
                return ContainerTypes.GENERIC_9X6.get();
            default:
                throw new IllegalArgumentException("Invalid number of rows: " + rows);
        }
    }

    class MyClickHandler implements SlotClickHandler {
        @Override
        public boolean handle(Cause cause, Container container, Slot slot, int slotIndex, ClickType<?> clickType) {
            cause.first(ServerPlayer.class).ifPresent(player -> {
                if (ClickTypes.CLICK_LEFT.get().equals(clickType)){
                    handleLeftClick(slotIndex,player);
                } else if (ClickTypes.CLICK_RIGHT.get().equals(clickType)) {
                    handleRightClick(slotIndex,player);
                }
            });
            return true; // true = cancelar acción
        }
    }
}

