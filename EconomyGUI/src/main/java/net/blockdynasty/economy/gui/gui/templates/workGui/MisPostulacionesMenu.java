package net.blockdynasty.economy.gui.gui.templates.workGui;

import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.PaginatedPanel;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.libs.services.TrabajosService.TrabajosService;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;


public class MisPostulacionesMenu extends PaginatedPanel<String> {
    private TrabajosService trabajosService;
    private IEntityGUI owner;

    public MisPostulacionesMenu(IEntityGUI owner, IGUI parent,TrabajosService trabajosService) {
        super("Mis postulaciones", 3, owner, parent,5);
        this.trabajosService = trabajosService;
        this.owner = owner;

        showItemsPage(trabajosService.getPostulacionesDe(owner.getName()));
    }


    @Override
    protected IItemStack createItemFor(String item) {
        return Item.of(RecipeItem.builder()
                        .setName(item)
                        .setLore(ChatColor.stringValueOf(Colors.RED)+"Click Izquierdo para eliminar")
                        .setTexture(Categorias.getTexture(item))
                .build());
    }

    @Override
    protected void functionLeftItemClick(String item) {
        this.trabajosService.removerInscripcionJugadorACategoria(owner, item);
        WorkGuiFactory.quitarCategorias(owner,this.getParent()).open();
        owner.sendMessage(ChatColor.stringValueOf(Colors.AQUA)+"[Linkedin] ->"+ChatColor.stringValueOf(Colors.GREEN)+" Postulación Eliminada");
    }


}
