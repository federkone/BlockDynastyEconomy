package BlockDynasty.BukkitImplementation.utils;

import java.util.Arrays;

public class Version {
    private static final String currentVersion = org.bukkit.Bukkit.getBukkitVersion();

    public static boolean match(String version) {
        return currentVersion.startsWith(version);
    }

    public static boolean match(String... versions) {
        return Arrays.stream(versions).anyMatch(currentVersion::startsWith);
    }

    public static boolean isLegacy() {
        return Version.match("1.8","1.9","1.10","1.11","1.12");
    }
}