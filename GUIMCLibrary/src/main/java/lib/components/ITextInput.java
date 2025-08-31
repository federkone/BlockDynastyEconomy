package lib.components;

import java.util.function.Function;

public interface ITextInput {
     void open(IPlayer owner, String title, String initialText, Function<String, String> function);
     void open(IGUI parent, IPlayer owner, String title, String initialText, Function<String, String> function);
}
