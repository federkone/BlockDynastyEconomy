package net.blockdynasty.economy.gui.placeholder;

import net.blockdynasty.economy.core.aplication.useCase.UseCaseFactory;

import java.util.UUID;
import java.util.function.Supplier;

public class PlaceHolderSuppler implements Supplier<IPlaceHolderDynastyEconomy> {
    private final UUID id;
    private final IPlaceHolderDynastyEconomy proxy;

    private volatile IPlaceHolderDynastyEconomy placeHolderInternal;
    private volatile UseCaseFactory useCaseFactory;

    public PlaceHolderSuppler() {
        this.id = UUID.randomUUID();
        this.placeHolderInternal = new PlaceHolderDynastyEconomyNull(id);
        this.proxy = new PlaceHolderDynastyEconomyProxy(this);
    }

    public void updateDependencies(UseCaseFactory useCaseFactory){
        this.useCaseFactory = useCaseFactory;
        this.placeHolderInternal= new PlaceHolderDynastyEconomy(useCaseFactory,id);
    }

    public void disable(){
        this.placeHolderInternal = new PlaceHolderDynastyEconomyNull(id);
    }

    public IPlaceHolderDynastyEconomy getInternal() {
        return placeHolderInternal;
    }

    @Override
    public IPlaceHolderDynastyEconomy get() {
        return proxy;
    }

    public UUID getId() {
        return this.id;
    }
}
