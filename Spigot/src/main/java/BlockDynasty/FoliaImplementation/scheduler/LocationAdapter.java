package BlockDynasty.FoliaImplementation.scheduler;

import lib.scheduler.ILocation;
import org.bukkit.Location;

public class LocationAdapter implements ILocation {
    Location location;

    public LocationAdapter(Location location) {
        this.location = location;
    }

    public Location getRoot(){
        return location;
    }

    public static LocationAdapter of(Location location) {
        return new LocationAdapter(location);
    }
}
