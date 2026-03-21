package kfs.invaders.sys;

import kfs.invaders.World;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

import java.util.*;

public class AlienShootSys implements KfsSystem {

    private final World world;

    public AlienShootSys(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        for (Entity fe : world.getEntitiesWith(FormationComp.class)) {
            FormationComp formation = world.getComponent(fe, FormationComp.class);
            formation.shootTimer += delta;

            if (formation.shootTimer < formation.shootInterval) return;
            formation.shootTimer = 0;

            // Find the lowest alien in each column
            Map<Integer, Entity> lowestInCol = new HashMap<>();
            Map<Integer, Float> lowestY = new HashMap<>();

            for (Entity ae : world.getEntitiesWith(AlienComp.class)) {
                AlienComp alien = world.getComponent(ae, AlienComp.class);
                PositionComp pos = world.getComponent(ae, PositionComp.class);
                if (alien == null || pos == null) continue;

                Float currentLowest = lowestY.get(alien.col);
                if (currentLowest == null || pos.pos.y < currentLowest) {
                    lowestY.put(alien.col, pos.pos.y);
                    lowestInCol.put(alien.col, ae);
                }
            }

            if (lowestInCol.isEmpty()) return;

            // Pick a random column to shoot from
            List<Entity> shooters = new ArrayList<>(lowestInCol.values());
            Entity shooter = shooters.get(world.getRandom().nextInt(shooters.size()));
            PositionComp pos = world.getComponent(shooter, PositionComp.class);
            SizeComp size = world.getComponent(shooter, SizeComp.class);

            if (pos != null && size != null) {
                world.spawnAlienBullet(
                    pos.pos.x + size.width / 2f - 1.5f,
                    pos.pos.y - 10
                );
            }
        }
    }
}
