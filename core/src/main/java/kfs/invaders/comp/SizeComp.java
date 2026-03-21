package kfs.invaders.comp;

import kfs.invaders.ecs.KfsComp;

public class SizeComp implements KfsComp {
    public float width;
    public float height;

    public SizeComp(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
