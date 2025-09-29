package lib.util.colors;

public class ChatColor {
    private static ChatColorProvider chatColorProvider;
    static {
        //chatColorProvider = new ChatColorVanilla();
        chatColorProvider = new ChatColorModern();
    }

    public static void setupVanilla(){
        chatColorProvider = new ChatColorVanilla();
    }
    public static void setupModern(){
        chatColorProvider = new ChatColorModern();
    }

    public static String stringValueOf(Colors color) {
        return chatColorProvider.stringValueOf(color);
    }
    public static String stringValueOf(String color) {
        return chatColorProvider.stringValueOf(color);
    }

    public static String formatColorToPlaceholder(String string) {
        return chatColorProvider.formatColorToPlaceholder(string);
    }
}
