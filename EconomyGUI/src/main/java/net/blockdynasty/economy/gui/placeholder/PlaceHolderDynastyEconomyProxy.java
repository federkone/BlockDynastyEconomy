package net.blockdynasty.economy.gui.placeholder;

import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;

import java.util.UUID;

public class PlaceHolderDynastyEconomyProxy implements IPlaceHolderDynastyEconomy {
    private final PlaceHolderSuppler suppler;

    public PlaceHolderDynastyEconomyProxy(PlaceHolderSuppler suppler) {
        this.suppler = suppler;
    }

    @Override
    public String onRequest(IEntityCommands player, String s) {
        return this.suppler.getInternal().onRequest(player, s);
    }

    @Override
    public UUID getId() {
        return this.suppler.getId();
    }
}
