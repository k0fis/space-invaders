package kfs.invaders.comp;

import com.badlogic.gdx.math.Vector2;
import kfs.invaders.ecs.KfsComp;

public class VelocityComp implements KfsComp {
    public final Vector2 vel;

    public VelocityComp() {
        this.vel = new Vector2(0, 0);
    }

    public VelocityComp(float vx, float vy) {
        this.vel = new Vector2(vx, vy);
    }
}
