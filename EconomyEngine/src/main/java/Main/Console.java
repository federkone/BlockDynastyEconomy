package Main;

public class Console {
    private static IConsole console;

    public static void setConsole(IConsole console) {
       Console.console = console;
    }

    public static void debug(String message) {
        console.debug(message);
    }


    public static void log(String message) {
        console.log(message);
    }


    public static void logError(String message) {
        console.logError(message);
    }

}
