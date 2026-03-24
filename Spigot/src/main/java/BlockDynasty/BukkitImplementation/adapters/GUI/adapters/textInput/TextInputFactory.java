package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.textInput;

import BlockDynasty.BukkitImplementation.utils.Version;
import lib.gui.components.ITextInput;
import org.bukkit.plugin.java.JavaPlugin;

public class TextInputFactory {
    private static ITextInput textInput;
    private static JavaPlugin plugin;

    public static ITextInput getTextInput(JavaPlugin plugin) {
        if(textInput == null) build(plugin);
        return textInput;
    }

    private static void build(JavaPlugin plugin) {
        if(Version.hasSupportAnvilGUI()){
            textInput = new TextInputAnvil(plugin);
        }else {
            textInput = new TextInputChat(plugin);
        }
    }
}
