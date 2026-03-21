package kfs.invaders.sys;

import kfs.invaders.KfsConst;
import kfs.invaders.World;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

public class GameStateSys implements KfsSystem {

    private final World world;

    public GameStateSys(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        if (world.isGameOver()) return;

        // Check player death
        for (Entity pe : world.getEntitiesWith(PlayerComp.class)) {
            PlayerComp player = world.getComponent(pe, PlayerComp.class);
            if (player.lives <= 0) {
                world.gameOver();
                return;
            }
        }

        // Check aliens reached player level
        for (Entity ae : world.getEntitiesWith(AlienComp.class)) {
            PositionComp pos = world.getComponent(ae, PositionComp.class);
            if (pos != null && pos.pos.y <= KfsConst.PLAYER_Y + KfsConst.PLAYER_HEIGHT) {
                world.gameOver();
                return;
            }
        }

        // Check all aliens dead → next wave
        if (world.getEntitiesWith(AlienComp.class).isEmpty()) {
            world.nextWave();
        }
    }
}
