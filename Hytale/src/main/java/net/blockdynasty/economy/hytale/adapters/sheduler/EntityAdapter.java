package net.blockdynasty.economy.hytale.adapters.sheduler;

import com.hypixel.hytale.server.core.entity.Entity;
import net.blockdynasty.economy.libs.abstractions.platform.scheduler.IEntity;

public class EntityAdapter implements IEntity {
    private Entity entity;
    public EntityAdapter(Entity entity) {
        this.entity = entity;
    }
    @Override
    public Object getRoot() {
        return entity;
    }
}
