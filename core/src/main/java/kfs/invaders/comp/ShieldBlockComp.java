package kfs.invaders.comp;

import kfs.invaders.ecs.KfsComp;

public class ShieldBlockComp implements KfsComp {
    public int hp;

    public ShieldBlockComp(int hp) {
        this.hp = hp;
    }
}
