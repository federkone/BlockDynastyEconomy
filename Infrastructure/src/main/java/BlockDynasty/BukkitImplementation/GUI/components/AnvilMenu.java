package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import BlockDynasty.BukkitImplementation.GUI.MaterialAdapter;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class AnvilMenu {

    /**
     * Opens an AnvilGUI that stays open and displays response messages
     *
     * @param owner The player to show the GUI to
     * @param title Title of the anvil GUI
     * @param initialText Initial text in the input field
     * @param function Function that processes input and returns response message (or null to close)
     */
    public static void open(Player owner, String title, String initialText, Function<String, String> function) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    owner.playSound(owner.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String input = stateSnapshot.getText();
                    String response = function.apply(input);

                    if (response == null) {
                        return List.of(AnvilGUI.ResponseAction.close());
                    }

                    // Otherwise, show the response and keep GUI open
                    return List.of(AnvilGUI.ResponseAction.replaceInputText(response));
                })
                .text(initialText)
                .title(title)
                .plugin(BlockDynastyEconomy.getInstance())  //this probably is needed to check plugin version for compatibility
                .open(owner);
    }

    public static void open(IGUI parent,Player owner, String title, String initialText, Function<String, String> function) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    owner.playSound(owner.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
                    // When output slot is clicked (process input)
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        String input = stateSnapshot.getText();
                        String response = function.apply(input);

                        if (response == null) {
                            return List.of(AnvilGUI.ResponseAction.close());
                        }

                        // Show response and keep GUI open
                        return List.of(AnvilGUI.ResponseAction.replaceInputText(response));
                    }

                    // When left slot (barrier) is clicked, go back
                    else if (slot == AnvilGUI.Slot.INPUT_LEFT) {
                        return List.of(
                                AnvilGUI.ResponseAction.close(),
                                AnvilGUI.ResponseAction.run(() -> parent.open())
                        );
                    }

                    return Collections.emptyList();
                })
                .text(initialText)
                .title(title)
                .plugin(BlockDynastyEconomy.getInstance())  //this probably is needed to check plugin version for compatibility
                .itemLeft(createBackItem())
                .itemOutput(new ItemStack(Material.PAPER))
                .open(owner);
    }

    private static ItemStack createBackItem() {
        ItemStack backItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = backItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§cBack");
            meta.setLore(Arrays.asList("§7Click to go back"));
            backItem.setItemMeta(meta);
        }
        return backItem;
    }

}
