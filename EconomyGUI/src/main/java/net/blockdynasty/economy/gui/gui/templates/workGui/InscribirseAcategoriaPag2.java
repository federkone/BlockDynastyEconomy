package net.blockdynasty.economy.gui.gui.templates.workGui;

import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.PlatformGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.services.TrabajosService.TrabajosService;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;

public class InscribirseAcategoriaPag2 extends CategoriasMenuPag2 {
    private PlatformGUI platformGUI;
    private TrabajosService trabajosService;

    public InscribirseAcategoriaPag2(IEntityGUI owner, IGUI parent,PlatformGUI platformGUI,TrabajosService trabajosService) {
        super(owner, parent);
        this.platformGUI = platformGUI;
        this.trabajosService = trabajosService;

        setButton(49, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/e9cdb9af38cf41daa53bc8cda7665c509632d14e678f0f19f263f46e541d8a30")
                        .setName("Cancelar postulaciones")
                        .build()))
                .setLeftClickAction(sender ->{
                    WorkGuiFactory.quitarCategorias(sender,this).open();
                })
                .build());
    }

    @Override
    protected void executeWithCategory(IEntityGUI owner, String category){
        if (!trabajosService.puedePostularse(owner)){
            owner.sendMessage(ChatColor.stringValueOf(Colors.AQUA) + "[Linkedin] ->"+ChatColor.stringValueOf(Colors.RED)+" Has alcanzado el limite de postulaciones");
            return;
        }
        this.trabajosService.inscribirJugadorACategoria(owner, category);
        owner.sendMessage(ChatColor.stringValueOf(Colors.AQUA) + "[Linkedin] -> "+ChatColor.stringValueOf(Colors.GREEN)+"Te has postulado para el trabajo de "+ChatColor.stringValueOf(Colors.YELLOW)+ category+".");
        platformGUI.getOnlinePlayers().forEach(player -> {
            player.sendMessage(ChatColor.stringValueOf(Colors.AQUA) +"[Linkedin] ->"+ChatColor.stringValueOf(Colors.GREEN)+" El jugador "+ChatColor.stringValueOf(Colors.YELLOW)+owner.getName()+ChatColor.stringValueOf(Colors.GREEN)+" Se ha postulado para trabajar como: "+ChatColor.stringValueOf(Colors.YELLOW)+category+ChatColor.stringValueOf(Colors.GREEN)+". Utiliza /linkedin para contratarlo");
        });
    }
}
