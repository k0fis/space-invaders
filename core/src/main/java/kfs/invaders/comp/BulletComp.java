package kfs.invaders.comp;

import kfs.invaders.ecs.KfsComp;

public class BulletComp implements KfsComp {
    public boolean playerBullet;

    public BulletComp(boolean playerBullet) {
        this.playerBullet = playerBullet;
    }
}
