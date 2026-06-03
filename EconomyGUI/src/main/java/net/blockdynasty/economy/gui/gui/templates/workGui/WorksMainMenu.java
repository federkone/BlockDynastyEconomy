package net.blockdynasty.economy.gui.gui.templates.workGui;

import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.AbstractPanel;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;

public class WorksMainMenu extends AbstractPanel {

    public WorksMainMenu(IEntityGUI owner) {
        super("Trabajos", 3, owner);

        setButton(11, Button.builder()
                        .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/a7ed66f5a70209d821167d156fdbc0ca3bf11ad54ed5d86e75c265f7e5029ec1")
                        .setName("Contratar")
                        .setLore("Contratá un usuario","que trabaje para vos")
                        .build()))
                .setLeftClickAction(sender ->{
                    new CategoriasMenuPag1(sender,this).open();
                })
                .build());

        setButton(15, Button.builder()
                    .setItemStack(Item.of(RecipeItem.builder()
                    .setTexture("http://textures.minecraft.net/texture/a3bc7b3389d1dc847c66bc39573739d5bca52ce89b48daca5844df152c00df29")
                    .setName("Postularse")
                    .setLore("Ofrecé tus servicios a cambio"," de una remuneración.")
                    .build()))
                .setLeftClickAction(sender ->{
                    WorkGuiFactory.inscribirseAcategoria(sender,this).open();
                })
                .build());
    }
}
