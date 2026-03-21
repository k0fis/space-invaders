package kfs.invaders.sys;

import kfs.invaders.KfsConst;
import kfs.invaders.World;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

import java.util.List;

public class FormationSys implements KfsSystem {

    private final World world;

    public FormationSys(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        for (Entity fe : world.getEntitiesWith(FormationComp.class)) {
            FormationComp formation = world.getComponent(fe, FormationComp.class);
            List<Entity> aliens = world.getEntitiesWith(AlienComp.class);

            if (aliens.isEmpty()) return;

            formation.stepTimer += delta;
            if (formation.stepTimer < formation.stepInterval) return;
            formation.stepTimer = 0;

            // Check if any alien hits the edge
            boolean hitEdge = false;
            for (Entity ae : aliens) {
                PositionComp pos = world.getComponent(ae, PositionComp.class);
                SizeComp size = world.getComponent(ae, SizeComp.class);
                if (pos == null || size == null) continue;

                if (formation.direction > 0 && pos.pos.x + size.width + formation.speed > world.getWidth() - 10) {
                    hitEdge = true;
                    break;
                }
                if (formation.direction < 0 && pos.pos.x - formation.speed < 10) {
                    hitEdge = true;
                    break;
                }
            }

            if (hitEdge) {
                // Move down and reverse
                for (Entity ae : aliens) {
                    PositionComp pos = world.getComponent(ae, PositionComp.class);
                    if (pos != null) {
                        pos.pos.y -= KfsConst.FORMATION_STEP_DOWN;
                    }
                }
                formation.direction = -formation.direction;
            } else {
                // Move sideways
                for (Entity ae : aliens) {
                    PositionComp pos = world.getComponent(ae, PositionComp.class);
                    if (pos != null) {
                        pos.pos.x += formation.direction * formation.speed;
                    }
                }
            }
        }
    }
}
