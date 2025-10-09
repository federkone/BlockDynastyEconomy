package BlockDynasty.BukkitImplementation.adapters.GUI.adapters;

import BlockDynasty.BukkitImplementation.utils.Console;
import BlockDynasty.BukkitImplementation.utils.Version;
import lib.gui.components.ITextInput;
//determinar si se puede usar AnvilGUI
//en el caso de que no, usar un input directamente del chat
public class TextInputFactory {
    private static ITextInput textInput;

    public static void build(){
        if(Version.isMohist()){
            textInput = new TextInputChat();
        }else {
            textInput = new TextInputAnvil();
        }
    }

    public static ITextInput getTextInput() {
        if(textInput == null) build();
        return textInput;
    }


}
