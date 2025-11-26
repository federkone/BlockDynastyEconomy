package adapters;

import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.ITextInput;

import java.util.function.Function;

public class TextInput implements ITextInput {



    @Override
    public void open(IEntityGUI owner, String title, String initialText, Function<String, String> function) {

    }

    @Override
    public void open(IGUI parent, IEntityGUI owner, String title, String initialText, Function<String, String> function) {

    }

    @Override
    public ITextInput asInputChat() {
        return null;
    }
}
