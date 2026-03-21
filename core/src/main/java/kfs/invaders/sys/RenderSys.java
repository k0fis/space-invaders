package kfs.invaders.sys;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import kfs.invaders.World;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsSystem;

public class RenderSys implements KfsSystem {

    private final World world;

    public RenderSys(World world) {
        this.world = world;
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Entity e : world.getEntitiesWith(RenderComp.class, PositionComp.class, SizeComp.class)) {
            RenderComp render = world.getComponent(e, RenderComp.class);
            PositionComp pos = world.getComponent(e, PositionComp.class);
            SizeComp size = world.getComponent(e, SizeComp.class);

            Texture tex = world.getTexture(render.textureName);
            if (tex == null) continue;

            // Hide player when not alive (blink effect)
            PlayerComp player = world.getComponent(e, PlayerComp.class);
            if (player != null && !player.alive) {
                // Blink during respawn
                if ((int)(player.respawnTimer * 8) % 2 == 0) continue;
            }

            // Fade explosions
            ExplosionComp explosion = world.getComponent(e, ExplosionComp.class);
            if (explosion != null) {
                float alpha = 1f - (explosion.stateTime / explosion.timer);
                batch.setColor(1, 1, 1, alpha);
            }

            // Tint damaged shields
            ShieldBlockComp shield = world.getComponent(e, ShieldBlockComp.class);
            if (shield != null) {
                float greenIntensity = shield.hp / 3f;
                batch.setColor(0.3f, greenIntensity, 0.3f, 1f);
            }

            batch.draw(tex, pos.pos.x, pos.pos.y, size.width, size.height);

            if (explosion != null || shield != null) {
                batch.setColor(Color.WHITE);
            }
        }
    }
}
