package lib.messages;

public class ChatColor {

    public static String stringValueOf(String colorName) {
        switch (colorName) {
            case "BLACK": return "§0";
            case "DARK_BLUE":return "§1";
            case "DARK_GREEN":return "§2";
            case "DARK_AQUA": return "§3";
            case "DARK_RED": return "§4";
            case "DARK_PURPLE":return "§5";
            case "GOLD":return "§6";
            case "GRAY":return "§7";
            case "DARK_GRAY":return "§8";
            case "BLUE":return "§9";
            case "GREEN":return "§a";
            case "AQUA": return "§b";
            case "RED":return "§c";
            case "LIGHT_PURPLE":return "§d";
            case "YELLOW":return "§e";
            case "WHITE" :return "§f";
            default:return "§f";
        }
    }
}
