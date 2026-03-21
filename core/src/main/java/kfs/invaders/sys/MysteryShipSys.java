package kfs.invaders.sys;

import kfs.invaders.KfsConst;
import kfs.invaders.World;
import kfs.invaders.comp.MysteryShipComp;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

public class MysteryShipSys implements KfsSystem {

    private final World world;
    private float timer;
    private float nextSpawn;

    public MysteryShipSys(World world) {
        this.world = world;
        resetTimer();
    }

    private void resetTimer() {
        timer = 0;
        nextSpawn = KfsConst.MYSTERY_MIN_INTERVAL +
            world.getRandom().nextFloat() * (KfsConst.MYSTERY_MAX_INTERVAL - KfsConst.MYSTERY_MIN_INTERVAL);
    }

    @Override
    public void update(float delta) {
        // Only spawn if no mystery ship currently exists
        if (!world.getEntitiesWith(MysteryShipComp.class).isEmpty()) return;

        timer += delta;
        if (timer >= nextSpawn) {
            world.spawnMysteryShip();
            resetTimer();
        }
    }
}
