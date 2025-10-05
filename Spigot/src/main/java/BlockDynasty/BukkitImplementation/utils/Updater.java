
package BlockDynasty.BukkitImplementation.utils;

import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import lib.scheduler.ContextualTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater {
    private int project = -1;
    private URL checkURL;
    private String newVersion;
    private String currentVersion;
    private JavaPlugin plugin;

    private Updater(JavaPlugin plugin, int project) {
        this.project = project;
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + project);
        } catch (MalformedURLException ex) {
            Console.logError("Invalid URL for updater.");
        }
    }

    private String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + project;
    }

    private boolean checkForUpdates() {
        if (checkURL == null) return false;
        try {
            URLConnection con = checkURL.openConnection();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                this.newVersion = reader.readLine();
            }
            return newVersion != null && !newVersion.equalsIgnoreCase(currentVersion);
        } catch (IOException e) {
            Console.log("Can't check update: " + e.getMessage());
            return false;
        }
    }

    private void checkAsync() {
        Scheduler.run(ContextualTask.build( ()->{
            if (checkForUpdates()) {
                Console.log("¡New version Available : " + newVersion + "!");
                Console.log("Download in: " + getResourceURL());
            }
        } ));
    }

    public static Updater check(JavaPlugin plugin, int project) {
        if(project == -1){
            return null;
        }
        Updater updater = new Updater(plugin,project);
        updater.checkAsync();
        return  updater;
    }

}
