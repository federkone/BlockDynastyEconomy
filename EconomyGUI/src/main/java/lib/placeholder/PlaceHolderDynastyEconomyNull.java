package lib.placeholder;

import lib.commands.abstractions.IEntityCommands;

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
