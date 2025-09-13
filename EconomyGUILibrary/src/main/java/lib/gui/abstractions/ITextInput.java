package lib.gui.abstractions;

import java.util.function.Function;

public interface ITextInput {
     void open(IEntityGUI owner, String title, String initialText, Function<String, String> function);
     void open(IGUI parent, IEntityGUI owner, String title, String initialText, Function<String, String> function);
}
