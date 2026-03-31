package net.blockdynasty.economy.gui.placeholder;

import net.blockdynasty.economy.core.aplication.useCase.UseCaseFactory;
import net.blockdynasty.providers.services.ServiceProvider;

import java.util.UUID;

public class PlaceholderFactory {
    private final PlaceHolderSuppler suppler;

    public PlaceholderFactory() {
        this.suppler = new PlaceHolderSuppler();
        ServiceProvider.register(IPlaceHolderDynastyEconomy.class,suppler);
    }

    public void updateDependencies(UseCaseFactory useCaseFactory) {
        this.suppler.updateDependencies(useCaseFactory);
    }

    public void unregister() {
        ServiceProvider.unregister(IPlaceHolderDynastyEconomy.class, suppler);
    }

    public void disableService() {
        this.suppler.disable();
    }

    public UUID getId() {
        return suppler.getId();
    }
}
