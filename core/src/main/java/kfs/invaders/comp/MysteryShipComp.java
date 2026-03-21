package kfs.invaders.comp;

import kfs.invaders.ecs.KfsComp;

public class MysteryShipComp implements KfsComp {
    public int points;

    public MysteryShipComp(int points) {
        this.points = points;
    }
}
