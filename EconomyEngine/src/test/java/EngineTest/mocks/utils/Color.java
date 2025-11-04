package EngineTest.mocks.utils;

public class Color {
    private static Colors colors;

    private Color(boolean modern) {
        if (modern){
            colors = new ModernColors();
        }else{
            colors = new VanillaColors();
        }
    }

    public static Color init(boolean modern) {
        return new Color(modern);
    }

    public static String parse(String text){
        return colors.parse(text);
    }
}
