package BlockDynasty.FoliaImplementation.scheduler;

import lib.scheduler.IEntity;
import org.bukkit.entity.Entity;

public class EntityAdapter implements IEntity {
    private final Entity entity;

    public EntityAdapter(Entity entity) {
        this.entity = entity;
    }

    public Entity getRoot(){
        return entity;
    }

    public static EntityAdapter of(Entity entity) {
        return new EntityAdapter(entity);
    }
}
