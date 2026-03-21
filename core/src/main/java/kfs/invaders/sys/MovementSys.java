package kfs.invaders.sys;

import kfs.invaders.KfsConst;
import kfs.invaders.World;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

public class MovementSys implements KfsSystem {

    private final World world;

    public MovementSys(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        for (Entity e : world.getEntitiesWith(PositionComp.class, VelocityComp.class)) {
            PositionComp pos = world.getComponent(e, PositionComp.class);
            VelocityComp vel = world.getComponent(e, VelocityComp.class);

            // Don't move aliens — they're moved by FormationSys
            if (world.getComponent(e, AlienComp.class) != null) continue;

            pos.pos.x += vel.vel.x * delta;
            pos.pos.y += vel.vel.y * delta;

            // Clamp player to screen bounds
            PlayerComp player = world.getComponent(e, PlayerComp.class);
            if (player != null) {
                SizeComp size = world.getComponent(e, SizeComp.class);
                if (pos.pos.x < 0) pos.pos.x = 0;
                if (size != null && pos.pos.x + size.width > KfsConst.WORLD_WIDTH) {
                    pos.pos.x = KfsConst.WORLD_WIDTH - size.width;
                }
            }
        }

        // Handle player respawn
        for (Entity e : world.getEntitiesWith(PlayerComp.class)) {
            PlayerComp player = world.getComponent(e, PlayerComp.class);
            if (!player.alive) {
                player.respawnTimer -= delta;
                if (player.respawnTimer <= 0) {
                    player.alive = true;
                    PositionComp pos = world.getComponent(e, PositionComp.class);
                    if (pos != null) {
                        pos.pos.x = KfsConst.WORLD_WIDTH / 2f - KfsConst.PLAYER_WIDTH / 2f;
                        pos.pos.y = KfsConst.PLAYER_Y;
                    }
                }
            }
        }
    }
}
