package BlockDynasty.BukkitImplementation.GUI.adapters;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TextInput implements ITextInput {

    @Override
    public void open(IPlayer owner, String title, String initialText, Function<String, String> function) {
        Player player = (Player) owner.getHandle();
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    player.playSound(player.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
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
                .open(player);
    }

    @Override
    public void open(IGUI parent, IPlayer owner, String title, String initialText, Function<String, String> function) {
        Player player = (Player) owner.getHandle();
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    player.playSound(player.getLocation(), MaterialAdapter.getClickSound(), 0.3f, 1.0f);
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
                .open(player);
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
