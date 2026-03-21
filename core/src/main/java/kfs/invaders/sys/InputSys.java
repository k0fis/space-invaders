package kfs.invaders.sys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import kfs.invaders.KfsConst;
import kfs.invaders.World;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

public class InputSys implements KfsSystem {

    private final World world;

    public InputSys(World world) {
        this.world = world;
    }

    @Override
    public void update(float delta) {
        for (Entity e : world.getEntitiesWith(PlayerComp.class)) {
            PlayerComp player = world.getComponent(e, PlayerComp.class);
            VelocityComp vel = world.getComponent(e, VelocityComp.class);
            PositionComp pos = world.getComponent(e, PositionComp.class);
            SizeComp size = world.getComponent(e, SizeComp.class);

            if (!player.alive) {
                vel.vel.x = 0;
                return;
            }

            // Movement
            vel.vel.x = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                vel.vel.x = -KfsConst.PLAYER_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                vel.vel.x = KfsConst.PLAYER_SPEED;
            }

            // Touch/mouse input
            if (Gdx.input.isTouched()) {
                float touchX = Gdx.input.getX() * (KfsConst.WORLD_WIDTH / Gdx.graphics.getWidth());
                float playerCenter = pos.pos.x + size.width / 2f;
                float diff = touchX - playerCenter;
                if (Math.abs(diff) > 10) {
                    vel.vel.x = diff > 0 ? KfsConst.PLAYER_SPEED : -KfsConst.PLAYER_SPEED;
                }
            }

            // Shooting
            player.shootCooldown -= delta;
            boolean wantShoot = Gdx.input.isKeyPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyJustPressed(Input.Keys.UP)
                || Gdx.input.isTouched();

            if (wantShoot && player.shootCooldown <= 0) {
                player.shootCooldown = KfsConst.PLAYER_SHOOT_COOLDOWN;
                world.spawnPlayerBullet(
                    pos.pos.x + size.width / 2f - KfsConst.BULLET_WIDTH / 2f,
                    pos.pos.y + size.height
                );
            }
        }
    }
}
