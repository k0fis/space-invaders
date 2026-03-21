package kfs.invaders.sys;

import kfs.invaders.World;
import kfs.invaders.comp.ExplosionComp;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

public class ExplosionSys implements KfsSystem {

    private final World world;

    public ExplosionSys(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        for (Entity e : world.getEntitiesWith(ExplosionComp.class)) {
            ExplosionComp exp = world.getComponent(e, ExplosionComp.class);
            exp.stateTime += delta;
            if (exp.stateTime >= exp.timer) {
                world.deleteEntity(e);
            }
        }
    }
}
