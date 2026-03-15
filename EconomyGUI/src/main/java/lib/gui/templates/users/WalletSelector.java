package lib.gui.templates.users;

import BlockDynasty.Economy.domain.entities.account.Player;
import abstractions.platform.materials.Materials;
import abstractions.platform.recipes.RecipeItem;
import lib.gui.GUIFactory;
import lib.gui.components.IEntityGUI;
import lib.gui.components.IGUI;
import lib.gui.components.factory.Item;
import lib.gui.components.generics.AbstractPanel;
import lib.gui.components.generics.Button;

public class WalletSelector extends AbstractPanel {
    private Player target;
    private IEntityGUI sender;

    public WalletSelector(IEntityGUI sender, Player target, IGUI parent) {
        super("Select Wallet", 3, sender, parent);
        this.target = target;
        this.sender = sender;
        setupButtons();
    }

    private void setupButtons(){
        setButton(12, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.BOOK).setName("From Wallet").build()))
                .setLeftClickAction(event -> {GUIFactory.currencyListToPayPanel(sender,target,this).open();})
                .build()
        );

        setButton(14, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.CHEST).setName("From Inventory").build()))
                .setLeftClickAction(event -> {GUIFactory.currencyListToPayItemsPanel(sender,target,this).open();})
                .build()
        );
        //4 lore button
        setButton(4, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.PAPER)
                        .setName("What is this?")
                        .setLore("Select the wallet you want to use to pay.","The first option will use your account balance"," while the second option will use items from your inventory.")
                        .build()))
                .build()
        );

        //22 back button
        setButton(22, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.BARRIER).setName("Back").build()))
                .setLeftClickAction(event -> {this.getParent().open();})
                .build()
        );
    }
}
