package BlockDynasty.BukkitImplementation.adapters.GUI.adapters.textInput;

import BlockDynasty.BukkitImplementation.utils.Version;
import lib.gui.components.ITextInput;

public class TextInputFactory {
    private static ITextInput textInput;

    public static ITextInput getTextInput() {
        if(textInput == null) build();
        return textInput;
    }

    private static void build(){
        if(Version.hasSupportAnvilGUI()){
            textInput = new TextInputAnvil();
        }else {
            textInput = new TextInputChat();
        }
    }
}
