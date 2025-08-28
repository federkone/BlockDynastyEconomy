package BlockDynasty.utils;

import net.kyori.adventure.text.format.NamedTextColor;

public class ChatColor {

    public static NamedTextColor TextColorvalueOf(String colorName) {
       switch (colorName) {
           case "BLACK": return NamedTextColor.BLACK;
           case "DARK_BLUE":return NamedTextColor.DARK_BLUE;
           case "DARK_GREEN":return NamedTextColor.DARK_GREEN;
           case "DARK_AQUA": return NamedTextColor.DARK_AQUA;
           case "DARK_RED": return NamedTextColor.DARK_RED;
           case "DARK_PURPLE":return NamedTextColor.DARK_PURPLE;
           case "GOLD":return NamedTextColor.GOLD;
           case "GRAY":return NamedTextColor.GRAY;
           case "DARK_GRAY":return NamedTextColor.DARK_GRAY;
           case "BLUE":return NamedTextColor.BLUE;
           case "GREEN":return NamedTextColor.GREEN;
           case "AQUA": return NamedTextColor.AQUA;
           case "RED":return NamedTextColor.RED;
           case "LIGHT_PURPLE":return NamedTextColor.LIGHT_PURPLE;
           case "YELLOW":return NamedTextColor.YELLOW;
           case "WHITE" :return NamedTextColor.WHITE;
           default:return NamedTextColor.WHITE;
       }
    }

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

    /*  private static final int BLACK_VALUE = 0x000000;
  private static final int DARK_BLUE_VALUE = 0x0000aa;
  private static final int DARK_GREEN_VALUE = 0x00aa00;
  private static final int DARK_AQUA_VALUE = 0x00aaaa;
  private static final int DARK_RED_VALUE = 0xaa0000;
  private static final int DARK_PURPLE_VALUE = 0xaa00aa;
  private static final int GOLD_VALUE = 0xffaa00;
  private static final int GRAY_VALUE = 0xaaaaaa;
  private static final int DARK_GRAY_VALUE = 0x555555;
  private static final int BLUE_VALUE = 0x5555ff;
  private static final int GREEN_VALUE = 0x55ff55;
  private static final int AQUA_VALUE = 0x55ffff;
  private static final int RED_VALUE = 0xff5555;
  private static final int LIGHT_PURPLE_VALUE = 0xff55ff;
  private static final int YELLOW_VALUE = 0xffff55;
  private static final int WHITE_VALUE = 0xffffff;*/
}
