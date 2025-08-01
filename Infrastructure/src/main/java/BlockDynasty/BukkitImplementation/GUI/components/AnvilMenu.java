package BlockDynasty.BukkitImplementation.GUI.components;

import BlockDynasty.BukkitImplementation.BlockDynastyEconomy;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class AnvilMenu {

    /**
     * Opens an AnvilGUI that stays open and displays response messages
     *
     * @param player The player to show the GUI to
     * @param title Title of the anvil GUI
     * @param initialText Initial text in the input field
     * @param function Function that processes input and returns response message (or null to close)
     */
    public static void open(Player player, String title, String initialText, Function<String, String> function) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
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
                .plugin(BlockDynastyEconomy.getInstance())
                .open(player);
    }
}
