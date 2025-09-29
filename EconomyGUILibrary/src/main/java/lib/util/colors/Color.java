package lib.util.colors;

public class Color {
    private Colors color;
    private String stringColor;

    private Color(Colors color) {
        this.color = color;
    }
    private Color(String color) {
        this.stringColor = color;
    }

    public static Color of(Colors color){
        return new Color(color);
    }
    public static Color of(String color){
        return new Color(color);
    }

    public String formatToMinimessage(){
        return "<"+getHexadecimal()+">";
    }

    public String getHexadecimal(){
        if(color != null){
            stringColor = color.name();
        }
        switch (stringColor.toUpperCase()){
            case "BLACK": return "#000000";
            case "DARK_BLUE": return "#0000AA";
            case "DARK_GREEN": return "#00AA00";
            case "DARK_AQUA": return "#00AAAA";
            case "DARK_RED": return "#AA0000";
            case "DARK_PURPLE": return "#AA00AA";
            case "GOLD": return "#FFAA00";
            case "GRAY": return "#AAAAAA";
            case "DARK_GRAY": return "#555555";
            case "BLUE": return "#5555FF";
            case "GREEN": return "#55FF55";
            case "AQUA": return "#55FFFF";
            case "RED": return "#FF5555";
            case "LIGHT_PURPLE": return "#FF55FF";
            case "YELLOW": return "#FFFF55";
            case "WHITE": return "#FFFFFF";
            default: return stringColor;
        }
    }

    public String getVanillaFormat(){
        if(color != null){
            stringColor = color.name();
        }
        if(stringColor.startsWith("#")){
            return "§f"; // No vanilla support for hex colors
        }
        switch (stringColor.toUpperCase()){
            case "BLACK": return "§0";
            case "DARK_BLUE": return "§1";
            case "DARK_GREEN": return "§2";
            case "DARK_AQUA": return "§3";
            case "DARK_RED": return "§4";
            case "DARK_PURPLE": return "§5";
            case "GOLD": return "§6";
            case "GRAY": return "§7";
            case "DARK_GRAY": return "§8";
            case "BLUE": return "§9";
            case "GREEN": return "§a";
            case "AQUA": return "§b";
            case "RED": return "§c";
            case "LIGHT_PURPLE": return "§d";
            case "YELLOW": return "§e";
            case "WHITE": return "§f";
            default: return stringColor;
        }
    }

}
