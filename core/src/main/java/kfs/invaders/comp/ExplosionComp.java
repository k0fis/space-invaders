package kfs.invaders.comp;

import kfs.invaders.ecs.KfsComp;

public class ExplosionComp implements KfsComp {
    public float timer;
    public float stateTime = 0;

    public ExplosionComp(float timer) {
        this.timer = timer;
    }
}
