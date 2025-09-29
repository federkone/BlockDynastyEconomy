package lib.util.colors;

public interface ChatColorProvider {
    String stringValueOf(Colors color);
    String stringValueOf(String color);

    String formatColorToPlaceholder(String string);
    String formatColorToPlaceholder(Colors color);
}
