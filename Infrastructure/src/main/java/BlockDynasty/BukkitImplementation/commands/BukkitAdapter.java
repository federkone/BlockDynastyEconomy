package BlockDynasty.BukkitImplementation.commands;

import BlockDynasty.BukkitImplementation.scheduler.ContextualTask;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import lib.commands.abstractions.PlatformAdapter;
import lib.commands.abstractions.Source;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


//permite explicarle a la libreria como obtener un jugador y como ejecutar un comando
public class BukkitAdapter implements PlatformAdapter {
    @Override
    public Source getPlayer(String name) {
        Player player = Bukkit.getPlayer(name);
        return new SourceAdapter(player);
    }

    @Override
    public void dispatchCommand(String command) {
        Scheduler.run(ContextualTask.build(()-> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command)));
    }
}
