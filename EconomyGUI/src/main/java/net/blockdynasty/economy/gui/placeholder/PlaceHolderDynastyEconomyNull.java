package net.blockdynasty.economy.gui.placeholder;

import net.blockdynasty.economy.gui.commands.abstractions.IEntityCommands;

import java.util.UUID;

public class PlaceHolderDynastyEconomyNull implements IPlaceHolderDynastyEconomy {
    private final UUID id;

    public PlaceHolderDynastyEconomyNull(UUID id){
        this.id = id;
    }

    @Override
    public String onRequest(IEntityCommands player, String s) {
        return "Loading...";
    }

    @Override
    public UUID getId() {
        return this.id;
    }
}
