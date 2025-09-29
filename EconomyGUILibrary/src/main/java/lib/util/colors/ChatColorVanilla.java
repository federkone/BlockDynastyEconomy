package lib.util.colors;

public class ChatColorVanilla implements ChatColorProvider {

    public String stringValueOf(Colors color) {
       return Color.of(color).getVanillaFormat();
    }
    @Override
    public String stringValueOf(String color) {
        return Color.of(color).getVanillaFormat();
    }

    @Override
    public String formatColorToPlaceholder(String string) {
        return Color.of(string).getVanillaFormat();
    }

    @Override
    public String formatColorToPlaceholder(Colors color) {
        return Color.of(color).getVanillaFormat();
    }



    //public static boolean isLegacyColor( String colorName ) {
    //    return !stringValueOf(colorName).equals("UNKNOWN");
    //}
}
