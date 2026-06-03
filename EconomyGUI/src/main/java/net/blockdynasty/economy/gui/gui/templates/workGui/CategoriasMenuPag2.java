package net.blockdynasty.economy.gui.gui.templates.workGui;

import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.AbstractPanel;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;

public class CategoriasMenuPag2 extends AbstractPanel {

    public CategoriasMenuPag2(IEntityGUI owner, IGUI parent) {
        super("Categorías", 6, owner, parent);

        setButton(10, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/a5bae982f483547d35afb16986a4b1d811c54da1bbfa5834362dc0af01f5692c")
                        .setName("Constructor")
                        .setLore("Construye estructuras siguiendo pedidos de jugadores.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Constructor");
                })
                .build());

        setButton(12, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/465d225e3ab682fccabdf7cce7dc4538280150375506f836698f6e0f800b5c38")
                        .setName("Decorador")
                        .setLore("Diseña interiores y mejora la estética de construcciones.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Decorador");
                })
                .build());

        setButton(14, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/3683465f7d0da2dd054d04eb974d7db92904781177fd080385b6bc8ac0127b09")
                        .setName("Recolector")
                        .setLore("Encargado de reunir recursos variados de distintas zonas.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Recolector");
                })
                .build());

        setButton(45, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/f84f597131bbe25dc058af888cb29831f79599bc67c95c802925ce4afba332fc")
                        .setName("Atrás")
                        .setLore("Ir Hacia Atrás.")
                        .build()))
                .setLeftClickAction(sender ->{
                    parent.open();
                })
                .build());
    }

    protected void executeWithCategory(IEntityGUI owner, String categoria){
        WorkGuiFactory.getPlayersFromCategory(owner,categoria,this).open();
    }
}
