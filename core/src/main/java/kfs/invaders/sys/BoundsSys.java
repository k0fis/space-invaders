package kfs.invaders.sys;

import kfs.invaders.World;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

public class BoundsSys implements KfsSystem {

    private final World world;

    public BoundsSys(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        // Remove bullets that go off screen
        for (Entity e : world.getEntitiesWith(BulletComp.class)) {
            PositionComp pos = world.getComponent(e, PositionComp.class);
            if (pos == null) continue;
            if (pos.pos.y > world.getHeight() + 20 || pos.pos.y < -20) {
                world.deleteEntity(e);
            }
        }

        // Remove mystery ships that go off screen
        for (Entity e : world.getEntitiesWith(MysteryShipComp.class)) {
            PositionComp pos = world.getComponent(e, PositionComp.class);
            if (pos == null) continue;
            if (pos.pos.x > world.getWidth() + 60 || pos.pos.x < -60) {
                world.deleteEntity(e);
            }
        }
    }
}
