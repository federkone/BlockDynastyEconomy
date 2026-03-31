package net.blockdynasty.economy.hytale.adapters.sheduler;

import com.hypixel.hytale.server.core.universe.world.World;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.ILocation;

public class LocationAdapter implements ILocation {
    private World world;

    public LocationAdapter(World world) {
        this.world = world;
    }


    @Override
    public Object getRoot() {
        return world;
    }
}
