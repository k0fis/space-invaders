package kfs.invaders.comp;

import kfs.invaders.ecs.KfsComp;

public class AlienComp implements KfsComp {
    public int row;
    public int col;
    public int points;

    public AlienComp(int row, int col, int points) {
        this.row = row;
        this.col = col;
        this.points = points;
    }
}
