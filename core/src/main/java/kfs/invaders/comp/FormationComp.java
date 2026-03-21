package kfs.invaders.comp;

import kfs.invaders.ecs.KfsComp;

public class FormationComp implements KfsComp {
    public float direction = 1; // 1 = right, -1 = left
    public float speed;
    public float stepInterval;
    public float shootInterval;
    public float stepTimer = 0;
    public float shootTimer = 0;
    public boolean needsDropAndReverse = false;

    public FormationComp(float speed, float stepInterval, float shootInterval) {
        this.speed = speed;
        this.stepInterval = stepInterval;
        this.shootInterval = shootInterval;
    }
}
