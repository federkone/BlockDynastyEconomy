package net.blockdynasty.economy.gui.gui.templates.users;

import net.blockdynasty.economy.core.domain.entities.account.Player;
import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.gui.gui.GUIFactory;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IGUI;
import net.blockdynasty.economy.gui.gui.components.factory.Item;
import net.blockdynasty.economy.gui.gui.components.generics.AbstractPanel;
import net.blockdynasty.economy.gui.gui.components.generics.Button;
import net.blockdynasty.economy.libs.util.colors.ChatColor;
import net.blockdynasty.economy.libs.util.colors.Colors;

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
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.BOOK)
                        .setName(ChatColor.stringValueOf(Colors.GREEN)+"From Wallet")
                        .setLore(ChatColor.stringValueOf(Colors.WHITE)+"Use your account balance to pay.")
                        .build()))
                .setLeftClickAction(event -> {
                    GUIFactory.currencyListToPayPanel(sender,target,this).open();
                })
                .build()
        );

        setButton(14, Button.builder()
                .setItemStack(Item.of(RecipeItem.builder().setMaterial(Materials.CHEST)
                        .setName(ChatColor.stringValueOf(Colors.YELLOW)+"From Inventory")
                        .setLore(ChatColor.stringValueOf(Colors.WHITE)+"Use items from your inventory to pay.")
                        .build()))
                .setLeftClickAction(event -> {
                    GUIFactory.currencyListToPayItemsPanel(sender,target,this).open();
                })
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
