package lib.util.colors;

public class ChatColorModern implements ChatColorProvider {

    public String stringValueOf(Colors color) {
        return Color.of(color).formatToMinimessage();
    }

    @Override
    public String stringValueOf(String color) {
        return Color.of(color).formatToMinimessage();
    }

    @Override
    public String formatColorToPlaceholder(String string) {
        return "&"+Color.of(string).getHexadecimal();
    }

    @Override
    public String formatColorToPlaceholder(Colors color) {
        return "&"+Color.of(color).getHexadecimal();
    }


}
