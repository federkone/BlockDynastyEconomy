package net.blockdynasty.economy.gui.gui.components.generics;

import net.blockdynasty.economy.libs.abstractions.platform.materials.Materials;
import net.blockdynasty.economy.libs.abstractions.platform.recipes.RecipeItem;
import net.blockdynasty.economy.gui.gui.components.IButton;
import net.blockdynasty.economy.gui.gui.components.IEntityGUI;
import net.blockdynasty.economy.gui.gui.components.IItemStack;
import net.blockdynasty.economy.gui.gui.components.factory.Item;

import java.util.function.Consumer;

public class Button implements IButton {
    private final IItemStack itemStack;
    private final Consumer<IEntityGUI> leftClickAction;
    private final Consumer<IEntityGUI> rightClickAction;

    private Button(IItemStack itemStack, Consumer<IEntityGUI> leftClickAction, Consumer<IEntityGUI> rightClickAction) {
        this.itemStack = itemStack;
        this.leftClickAction = leftClickAction;
        this.rightClickAction = rightClickAction;
    }

    @Override
    public Consumer<IEntityGUI> getLeftClickAction() {
        return leftClickAction;
    }

    @Override
    public Consumer<IEntityGUI> getRightClickAction() {
        return rightClickAction;
    }

    @Override
    public IItemStack getItemStack() {
        return itemStack;
    }

    public static ButtonBuilder builder() {
        return new ButtonBuilder();
    }

    //builder pattern.
    public static class ButtonBuilder {
        private IItemStack itemStack = null;
        private Consumer<IEntityGUI> leftClickAction = null;
        private Consumer<IEntityGUI> rightClickAction = null;

        public ButtonBuilder setItemStack(IItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public ButtonBuilder setLeftClickAction(Consumer<IEntityGUI> leftClickAction) {
            this.leftClickAction = leftClickAction;
            return this;
        }

        public ButtonBuilder setRightClickAction(Consumer<IEntityGUI> rightClickAction) {
            this.rightClickAction = rightClickAction;
            return this;
        }

        public Button build() {
            if(itemStack == null) {
                itemStack= Item.of(RecipeItem.builder()
                                .setMaterial(Materials.STONE)
                                .setName("Default Button")
                        .build());
            }
            return new Button(itemStack, leftClickAction, rightClickAction);
        }
    }


}
