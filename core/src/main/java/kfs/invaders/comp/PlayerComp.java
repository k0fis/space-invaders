package kfs.invaders.comp;

import kfs.invaders.KfsConst;
import kfs.invaders.ecs.KfsComp;

public class PlayerComp implements KfsComp {
    public int lives = KfsConst.PLAYER_START_LIVES;
    public int score = 0;
    public float shootCooldown = 0;
    public boolean alive = true;
    public float respawnTimer = 0;
}
