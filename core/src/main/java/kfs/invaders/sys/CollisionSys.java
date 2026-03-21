package kfs.invaders.sys;

import kfs.invaders.World;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

import java.util.ArrayList;
import java.util.List;

public class CollisionSys implements KfsSystem {

    private final World world;

    public CollisionSys(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        List<Entity> bullets = world.getEntitiesWith(BulletComp.class);
        List<Entity> toDelete = new ArrayList<>();

        for (Entity be : bullets) {
            BulletComp bullet = world.getComponent(be, BulletComp.class);
            PositionComp bPos = world.getComponent(be, PositionComp.class);
            SizeComp bSize = world.getComponent(be, SizeComp.class);
            if (bPos == null || bSize == null) continue;

            if (bullet.playerBullet) {
                // Player bullet vs aliens
                for (Entity ae : world.getEntitiesWith(AlienComp.class)) {
                    PositionComp aPos = world.getComponent(ae, PositionComp.class);
                    SizeComp aSize = world.getComponent(ae, SizeComp.class);
                    if (aPos == null || aSize == null) continue;

                    if (aabb(bPos, bSize, aPos, aSize)) {
                        world.onAlienKilled(ae);
                        toDelete.add(be);
                        break;
                    }
                }

                // Player bullet vs mystery ship
                for (Entity me : world.getEntitiesWith(MysteryShipComp.class)) {
                    PositionComp mPos = world.getComponent(me, PositionComp.class);
                    SizeComp mSize = world.getComponent(me, SizeComp.class);
                    if (mPos == null || mSize == null) continue;

                    if (aabb(bPos, bSize, mPos, mSize)) {
                        world.onMysteryKilled(me);
                        toDelete.add(be);
                        break;
                    }
                }
            } else {
                // Alien bullet vs player
                for (Entity pe : world.getEntitiesWith(PlayerComp.class)) {
                    PlayerComp player = world.getComponent(pe, PlayerComp.class);
                    if (!player.alive) continue;

                    PositionComp pPos = world.getComponent(pe, PositionComp.class);
                    SizeComp pSize = world.getComponent(pe, SizeComp.class);
                    if (pPos == null || pSize == null) continue;

                    if (aabb(bPos, bSize, pPos, pSize)) {
                        world.onPlayerHit();
                        toDelete.add(be);
                        break;
                    }
                }
            }

            // Any bullet vs shields
            for (Entity se : world.getEntitiesWith(ShieldBlockComp.class)) {
                PositionComp sPos = world.getComponent(se, PositionComp.class);
                SizeComp sSize = world.getComponent(se, SizeComp.class);
                if (sPos == null || sSize == null) continue;

                if (aabb(bPos, bSize, sPos, sSize)) {
                    ShieldBlockComp shield = world.getComponent(se, ShieldBlockComp.class);
                    shield.hp--;
                    if (shield.hp <= 0) {
                        world.deleteEntity(se);
                    }
                    toDelete.add(be);
                    break;
                }
            }
        }

        // Aliens touching shields destroy them
        for (Entity ae : world.getEntitiesWith(AlienComp.class)) {
            PositionComp aPos = world.getComponent(ae, PositionComp.class);
            SizeComp aSize = world.getComponent(ae, SizeComp.class);
            if (aPos == null || aSize == null) continue;

            for (Entity se : world.getEntitiesWith(ShieldBlockComp.class)) {
                PositionComp sPos = world.getComponent(se, PositionComp.class);
                SizeComp sSize = world.getComponent(se, SizeComp.class);
                if (sPos == null || sSize == null) continue;

                if (aabb(aPos, aSize, sPos, sSize)) {
                    world.deleteEntity(se);
                }
            }
        }

        for (Entity e : toDelete) {
            world.deleteEntity(e);
        }
    }

    private boolean aabb(PositionComp p1, SizeComp s1, PositionComp p2, SizeComp s2) {
        return p1.pos.x < p2.pos.x + s2.width
            && p1.pos.x + s1.width > p2.pos.x
            && p1.pos.y < p2.pos.y + s2.height
            && p1.pos.y + s1.height > p2.pos.y;
    }
}
