package BlockDynasty.GUI.components;

import BlockDynasty.SpongePlugin;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.units.qual.C;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.menu.ClickType;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.menu.handler.SlotClickHandler;
import org.spongepowered.api.item.inventory.type.ViewableInventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

//puedo mantener funciones y escuchar click como lo realizado en Abstract GUI
public class AnvilMenu {
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Map<Integer, Consumer<ServerPlayer>> leftClickActions = new HashMap<>();
    private final Map<Integer, Consumer<ServerPlayer>> rightClickActions = new HashMap<>();

    public static void open(ServerPlayer owner, String title, String initialText, Function<String, String> function){
        ViewableInventory inv= ViewableInventory.builder()
                .type(ContainerTypes.ANVIL)
                .completeStructure()
                .plugin(SpongePlugin.getPlugin())
                .build();
        inv.set(0,ItemStack.builder().itemType(ItemTypes.GLASS_PANE).build());
        inv.set(1,ItemStack.builder().itemType(ItemTypes.GLASS_PANE).build());

        InventoryMenu menu = InventoryMenu.of(inv);
        menu.setTitle(Component.text(title));
        menu.registerSlotClick(new MyClickHandler());

        menu.open(owner);
    }

    public static void open(IGUI parent,ServerPlayer owner, String title, String initialText, Function<String, String> function) {
        ViewableInventory inv=ViewableInventory.builder()
                .type(ContainerTypes.ANVIL)
                .completeStructure()
                .plugin(SpongePlugin.getPlugin())
                .build();

        inv.set(0, ItemStack.of(ItemTypes.GLASS_PANE));
        inv.set(1, ItemStack.of(ItemTypes.ACACIA_SIGN));

        InventoryMenu menu = InventoryMenu.of(inv).setReadOnly(true);
        menu.setTitle(Component.text(title));
        menu.registerSlotClick(new MyClickHandler());

        menu.open(owner);
    }


    private static ItemStack createBackItem() {
        ItemStack backItem = ItemStack.of(ItemTypes.BARRIER);
            //meta.setLore(Arrays.asList("§7Click to go back"));
        return backItem;
    }

    static class MyClickHandler implements SlotClickHandler {
        @Override
        public boolean handle(Cause cause, Container container, Slot slot, int slotIndex, ClickType<?> clickType) {
            cause.first(ServerPlayer.class).ifPresent(player -> {
                if (ClickTypes.CLICK_LEFT.get().equals(clickType)){
                    player.sendMessage(Component.text("You clicked left"+ "slot "+ slotIndex));
                } else if (ClickTypes.CLICK_RIGHT.get().equals(clickType)) {
                    player.sendMessage(Component.text("You clicked right"));
                }
            });
            return true; // true = cancelar acción
        }
    }

    /*@Listener
public void onInventoryChange(ChangeInventoryEvent event, @First ServerPlayer player) {
    if (event.inventory().equals(inv)) {
        // Obtener el texto del slot de resultado
        Optional<ItemStack> resultOpt = inv.getSlot(2).get().peek(); // Slot 2 es el resultado
        if (resultOpt.isPresent()) {
            Component nombre = resultOpt.get().get(Keys.CUSTOM_NAME).orElse(Component.empty());
            String textoIngresado = nombre.toPlain();
            // Procesar el texto
        }
    }
}*/


    /*// Ítem decorativo (ej: una pluma) en el slot derecho
ItemStack pluma = ItemStack.builder()
    .itemType(ItemTypes.FEATHER)
    .add(Keys.CUSTOM_NAME, Component.text("Ingresa texto a la izquierda"))
    .build();
inv.set(1, pluma); // Slot de ingreso derecho

// Escuchar clic en el slot de resultado
@Listener
public void onInventoryClick(ClickInventoryEvent event, @First ServerPlayer player) {
    if (event.inventory().equals(inv) && event.getTargetSlot().getIndex() == 2) {
        event.setCancelled(true); // Evita que el jugador tome el ítem
        // Obtener el texto del slot de resultado
        Optional<ItemStack> resultOpt = inv.getSlot(2).get().peek();
        if (resultOpt.isPresent()) {
            Component nombre = resultOpt.get().get(Keys.CUSTOM_NAME).orElse(Component.empty());
            String textoIngresado = nombre.toPlain();
            player.sendMessage(Component.text("Ingresaste: " + textoIngresado));
            player.closeInventory();
        }
    }
}*/
}
