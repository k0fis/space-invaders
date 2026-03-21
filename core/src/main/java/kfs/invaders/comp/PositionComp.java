package kfs.invaders.comp;

import com.badlogic.gdx.math.Vector2;
import kfs.invaders.ecs.KfsComp;

public class PositionComp implements KfsComp {
    public final Vector2 pos;

    public PositionComp(float x, float y) {
        this.pos = new Vector2(x, y);
    }
}
