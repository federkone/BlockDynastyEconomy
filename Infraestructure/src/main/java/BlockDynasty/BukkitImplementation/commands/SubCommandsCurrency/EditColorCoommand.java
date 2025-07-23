package BlockDynasty.BukkitImplementation.commands.SubCommandsCurrency;

import BlockDynasty.BukkitImplementation.scheduler.SchedulerFactory;
import BlockDynasty.Economy.aplication.useCase.currency.EditCurrencyUseCase;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyColorUnformat;
import BlockDynasty.Economy.domain.entities.currency.Exceptions.CurrencyNotFoundException;
import BlockDynasty.BukkitImplementation.config.file.F;
import BlockDynasty.BukkitImplementation.scheduler.Scheduler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EditColorCoommand implements CommandExecutor {
    private final EditCurrencyUseCase editCurrencyUseCase;

    public EditColorCoommand(EditCurrencyUseCase editCurrencyUseCase) {
        this.editCurrencyUseCase = editCurrencyUseCase;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 2) {
            sender.sendMessage("§0§lBLACK §7= black");
            sender.sendMessage("§1§lDARK BLUE §7= dark_blue");
            sender.sendMessage("§2§lDARK GREEN §7= dark_green");
            sender.sendMessage("§3§lDARK AQUA §7= dark_aqua");
            sender.sendMessage("§4§lDARK RED §7= dark_red");
            sender.sendMessage("§5§lDARK PURPLE §7= dark_purple");
            sender.sendMessage("§6§lGOLD §7= gold");
            sender.sendMessage("§7§lGRAY §7= gray");
            sender.sendMessage("§8§lDARK GRAY §7= dark_gray");
            sender.sendMessage("§9§lBLUE §7= blue");
            sender.sendMessage("§a§lGREEN §7= green");
            sender.sendMessage("§b§lAQUA §7= aqua");
            sender.sendMessage("§c§lRED §7= red");
            sender.sendMessage("§d§lLIGHT PURPLE §7= light_purple");
            sender.sendMessage("§e§lYELLOW §7= yellow");
            sender.sendMessage("§f§lWHITE §7= white|reset");

            sender.sendMessage(F.getCurrencyUsage_Color());
            return true;
        }


        String currencyName = args[0];
        String colorString = args[1].toUpperCase();

        SchedulerFactory.runAsync(() -> {
            try {
                editCurrencyUseCase.editColor(currencyName, colorString);
                sender.sendMessage(F.getPrefix() + "§7Color for §f" + currencyName + " §7updated: " + colorString);
            } catch (CurrencyNotFoundException e) {
                sender.sendMessage(F.getUnknownCurrency());
            } catch (CurrencyColorUnformat e) {
                sender.sendMessage(F.getPrefix() + "§cInvalid chat color.");
            }

        });
        return false;
    }
}
