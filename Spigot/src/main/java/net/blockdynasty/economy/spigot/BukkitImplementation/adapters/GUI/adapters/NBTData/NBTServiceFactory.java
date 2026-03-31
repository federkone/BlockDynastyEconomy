package net.blockdynasty.economy.spigot.BukkitImplementation.adapters.GUI.adapters.NBTData;

import net.blockdynasty.economy.spigot.BukkitImplementation.utils.Version;
import org.bukkit.plugin.java.JavaPlugin;

public class NBTServiceFactory {


    public static NBTService get(JavaPlugin plugin) {
        if (Version.isLegacy()){
            return new NBTApi();
        }else {
            return new NBTModern(plugin);
        }
    }
}
