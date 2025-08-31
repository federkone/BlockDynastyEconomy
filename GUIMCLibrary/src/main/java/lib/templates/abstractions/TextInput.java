package lib.templates.abstractions;

import lib.components.IGUI;
import lib.components.IPlayer;
import lib.components.ITextInput;

import java.util.function.Function;

public class TextInput implements ITextInput {
    public void open(IPlayer owner, String title, String initialText, Function<String, String> function) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void open(IGUI parent, IPlayer owner, String title, String initialText, Function<String, String> function) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
