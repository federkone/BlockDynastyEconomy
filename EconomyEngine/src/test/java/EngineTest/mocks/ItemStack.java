package EngineTest.mocks;

import EngineTest.mocks.utils.Color;
import lib.gui.components.IItemStack;
import lib.gui.components.Materials;

import java.util.ArrayList;
import java.util.List;

public class ItemStack implements IItemStack {
    private String name= "";
    private List<String> lore= new ArrayList<>();
    private Materials material;

    public ItemStack(Materials material){
        this.material=material;
    }

    @Override
    public IItemStack setDisplayName(String name) {
        this.name = Color.parse(name);
        return this;
    }

    @Override
    public IItemStack setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    @Override
    public Object getHandle() {
        return this;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
