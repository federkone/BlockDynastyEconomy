package net.blockdynasty.economy.gui.gui.templates.workGui;

import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.PaginatedPanel;
import net.blockdynasty.economy.libs.abstractions.platform.entity.IPlayer;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.services.TrabajosService.TrabajosService;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PlayersFromCategory extends PaginatedPanel<IPlayer> {
    //singleton para controlar el spam de notificaciones.....
    private HashMap<String, Long> lastNotificationTimes = new HashMap<>();
    private String category;
    private IEntityGUI player;

    public PlayersFromCategory(IEntityGUI player, IGUI parent,String category,TrabajosService trabajosService) {
        super("Jugadores Disponibles", 5, player, parent, 14);
        this.player = player;
        this.category = category;


        List<IPlayer> players = new ArrayList<>(trabajosService.getJugadoresEnLineaDeCategoria(category));
        players = players.stream().filter(p -> !p.getName().equals(player.getName())).collect(Collectors.toList());
        showItemsPage(players);
    }

    @Override
    protected IItemStack createItemFor(IPlayer target) {
        RecipeItem recipe = RecipeItem.builder()
                .setMaterial(Materials.PLAYER_HEAD)
                .setName(target.getName())
                .build();
        return Item.of(recipe);
    }

    @Override
    protected void functionLeftItemClick(IPlayer target) {
        long currentTime = System.currentTimeMillis();
        Long lastNotificationTime = lastNotificationTimes.getOrDefault(target.getName(), 0L);
        Long remainingTime = currentTime - lastNotificationTime;

        if (remainingTime > 60000) { // 60 seconds/ 60 seconds
            target.sendMessage(ChatColor.stringValueOf(Colors.AQUA)+"[Linkedin] -> "+ChatColor.stringValueOf(Colors.YELLOW)+player.getName() +ChatColor.stringValueOf(Colors.GREEN)+" esta interesado en contactarte para el trabajo de "+ChatColor.stringValueOf(Colors.YELLOW)+ category+ ChatColor.stringValueOf(Colors.GREEN)+". Contactate con el para mas información!");
            player.sendMessage(ChatColor.stringValueOf(Colors.AQUA)+"[Linkedin] -> "+ChatColor.stringValueOf(Colors.GREEN)+"Le has enviado un mensaje a "+ChatColor.stringValueOf(Colors.YELLOW)+target.getName()+ChatColor.stringValueOf(Colors.GREEN)+" para contactarlo sobre el trabajo de "+ChatColor.stringValueOf(Colors.YELLOW)+category);
            lastNotificationTimes.put(target.getName(), currentTime);
        }else {
            long secondsLeft = (60000 - remainingTime) / 1000;
            player.sendMessage(ChatColor.stringValueOf(Colors.AQUA)+"[Linkedin] -> "+ChatColor.stringValueOf(Colors.RED)+"Ya has contactado a "+ChatColor.stringValueOf(Colors.YELLOW)+target.getName()+ChatColor.stringValueOf(Colors.RED)+" recientemente. Espera "+ChatColor.stringValueOf(Colors.YELLOW)+secondsLeft+" segundos"+ChatColor.stringValueOf(Colors.RED)+" para volver a contactarlo.");
        }
    }
}
