package net.blockdynasty.economy.gui.gui.templates.workGui;

import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.AbstractPanel;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;

//cada 2 espacios
//10 -> boton aplanador
//12 -> boton minero
//14 -> boton Talador
//15 -> boton cazador
//27 -> Granjero
//29 -> AFK
//31 -> explorador
//33 -> luchador
public class CategoriasMenuPag1 extends AbstractPanel {

    public CategoriasMenuPag1(IEntityGUI owner, IGUI parent) {
        super("Categorías", 6, owner, parent);

        setButton(10, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/18d7d19a3fd8e26524aab32f944dc7d2f560e1d8fe178e893ceea74cfcf")
                        .setName("Aplanador")
                        .setLore("Limpia y nivela terrenos para construcciones.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Aplanador");
                })
                .build());

        setButton(12, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/b6ea2135838461534372f2da6c862d21cd5f3d2c7119f2bb674bbd42791")
                        .setName("Minero")
                        .setLore("Recolecta minerales y recursos bajo tierra.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Minero");
                })
                .build());

        setButton(14, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/64ba49384dba7b7acdb4f70e9361e6d57cbbcbf720cf4f16c2bb83e4557")
                        .setName("Talador")
                        .setLore("Tala árboles y recolecta grandes cantidades de madera.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Talador");
                })
                .build());

        setButton(16, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/50dfc8a3563bf996f5c1b74b0b015b2cceb2d04f94bbcdafb2299d8a5979fac1")
                        .setName("Cazador")
                        .setLore("Caza criaturas y consigue drops valiosos.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Cazador");
                })
                .build());

        setButton(28, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/33235c98db07f976def27ceaaca89c7717831b4bcc7f2f3a14622e78c2dbe")
                        .setName("Granjero")
                        .setLore("Cultiva alimentos y administra granjas automáticas o manuales.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Granjero");
                })
                .build());

        setButton(30, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/5703ec7638791df4ac2f208942d0fd737f163d355c783b452f26652fd1878062")
                        .setName("AFK")
                        .setLore("Permanece conectado para generar tiempo o recursos AFK.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "AFK");
                })
                .build());

        setButton(32, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/e6315e380545b99ee9c8d1b217cde2d8884ae873e00744e0d05d7663f41882cf")
                        .setName("Explorador")
                        .setLore("Busca estructuras, biomas y recursos lejanos.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Explorador");
                })
                .build());

        setButton(34, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/b01cbfb414760efe504d4af739708a18b89b6145d7659bcf526f1e42d7bedb37")
                        .setName("Luchador")
                        .setLore("Ofrece apoyo en batallas, expediciones y enfrentamientos.")
                        .build()))
                .setLeftClickAction(sender ->{
                    executeWithCategory(sender, "Luchador");
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

        setButton(53, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder()
                        .setTexture("http://textures.minecraft.net/texture/96339ff2e5342ba18bdc48a99cca65d123ce781d878272f9d964ead3b8ad370")
                        .setName("Siguiente página")
                        .setLore("Ir Hacia la siguiente página.")
                        .build()))
                .setLeftClickAction(sender ->{
                    openNextPage();
                })
                .build());
    }

    protected void executeWithCategory(IEntityGUI owner, String category){
        WorkGuiFactory.getPlayersFromCategory(owner,category,this).open();
    }

    protected void openNextPage(){
        new CategoriasMenuPag2(owner,this).open();
    }
}
