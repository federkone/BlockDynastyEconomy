package BlockDynasty.BukkitImplementation.utils;

import java.util.Arrays;

public class Version {
    private static final String currentVersion = org.bukkit.Bukkit.getBukkitVersion();
    private static final boolean hasAdventure = JavaUtil.classExists("net.kyori.adventure.text.Component")&&JavaUtil.classExists("net.kyori.adventure.text.minimessage.MiniMessage");

    public static boolean match(String version) {
        return currentVersion.startsWith(version);
    }

    public static boolean match(String... versions) {
        return Arrays.stream(versions).anyMatch(currentVersion::startsWith);
    }

    /**
     *  this is useful for item Creations
     **/
    public static boolean isLegacy() {
        return Version.match("1.8","1.9","1.10","1.11","1.12");
    }

    /**
     * Check if the server supports Adventure Text API
     */
    public static boolean hasSupportAdventureText() {
        return hasAdventure;
    }
}