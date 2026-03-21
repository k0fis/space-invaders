package kfs.invaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import kfs.invaders.comp.*;
import kfs.invaders.ecs.Entity;
import kfs.invaders.ecs.KfsWorld;
import kfs.invaders.sys.*;

import java.util.*;

public class World extends KfsWorld {

    public static final String TEX_PLAYER = "player";
    public static final String TEX_ALIEN1 = "alien1";
    public static final String TEX_ALIEN2 = "alien2";
    public static final String TEX_ALIEN3 = "alien3";
    public static final String TEX_MYSTERY = "mystery";
    public static final String TEX_BULLET_PLAYER = "bullet_player";
    public static final String TEX_BULLET_ALIEN = "bullet_alien";
    public static final String TEX_SHIELD = "shield";
    public static final String TEX_EXPLOSION = "explosion";

    private final Map<String, Texture> textures = new HashMap<>();
    private final Random random = new Random();

    private final float width;
    private final float height;
    private final SoundManager sound;

    private int wave = 1;
    private boolean gameOverFlag = false;
    private Runnable gameOverCallback;

    public World(float width, float height, SoundManager sound) {
        this.width = width;
        this.height = height;
        this.sound = sound;
        loadTextures();
    }

    public void startGame(Runnable gameOverCallback) {
        this.gameOverCallback = gameOverCallback;
        this.wave = 1;
        this.gameOverFlag = false;
        reset();
        loadTextures();

        addSys(new InputSys(this));
        addSys(new FormationSys(this));
        addSys(new AlienShootSys(this));
        addSys(new MysteryShipSys(this));
        addSys(new MovementSys(this));
        addSys(new CollisionSys(this));
        addSys(new BoundsSys(this));
        addSys(new ExplosionSys(this));
        addSys(new GameStateSys(this));
        addSys(new RenderSys(this));

        createPlayer();
        createFormation();
        createShields();
        createAlienFormation();
    }

    private void loadTextures() {
        textures.values().forEach(Texture::dispose);
        textures.clear();

        textures.put(TEX_PLAYER, generatePlayerTexture());
        textures.put(TEX_ALIEN1, generateAlienTexture(1));
        textures.put(TEX_ALIEN2, generateAlienTexture(2));
        textures.put(TEX_ALIEN3, generateAlienTexture(3));
        textures.put(TEX_MYSTERY, generateMysteryTexture());
        textures.put(TEX_BULLET_PLAYER, generateRect(3, 10, Color.LIME));
        textures.put(TEX_BULLET_ALIEN, generateRect(3, 10, Color.RED));
        textures.put(TEX_SHIELD, generateRect(6, 6, Color.GREEN));
        textures.put(TEX_EXPLOSION, generateExplosionTexture());
    }

    public Texture getTexture(String name) {
        Texture t = textures.get(name);
        if (t == null) {
            Gdx.app.error("World", "Texture not found: " + name);
        }
        return t;
    }

    public SoundManager getSound() {
        return sound;
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getWave() { return wave; }
    public Random getRandom() { return random; }

    // --- Entity factories ---

    public void createPlayer() {
        Entity e = createEntity();
        addComponent(e, new PositionComp(width / 2f - KfsConst.PLAYER_WIDTH / 2f, KfsConst.PLAYER_Y));
        addComponent(e, new VelocityComp());
        addComponent(e, new SizeComp(KfsConst.PLAYER_WIDTH, KfsConst.PLAYER_HEIGHT));
        addComponent(e, new RenderComp(TEX_PLAYER));
        addComponent(e, new PlayerComp());
    }

    public void createFormation() {
        float speedMul = 1f + (wave - 1) * 0.15f;
        float stepInterval = KfsConst.FORMATION_BASE_STEP_INTERVAL / speedMul;
        float shootInterval = KfsConst.FORMATION_BASE_SHOOT_INTERVAL / speedMul;
        float speed = KfsConst.FORMATION_BASE_SPEED * speedMul;

        Entity e = createEntity();
        addComponent(e, new FormationComp(speed, stepInterval, shootInterval));
    }

    public void createAlienFormation() {
        float totalWidth = KfsConst.FORMATION_COLS * KfsConst.ALIEN_SPACING_X;
        float startX = (width - totalWidth) / 2f + KfsConst.ALIEN_SPACING_X / 2f - KfsConst.ALIEN_WIDTH / 2f;

        for (int row = 0; row < KfsConst.FORMATION_ROWS; row++) {
            int points;
            String tex;
            if (row == 0) { points = 30; tex = TEX_ALIEN3; }
            else if (row <= 2) { points = 20; tex = TEX_ALIEN2; }
            else { points = 10; tex = TEX_ALIEN1; }

            for (int col = 0; col < KfsConst.FORMATION_COLS; col++) {
                float x = startX + col * KfsConst.ALIEN_SPACING_X;
                float y = KfsConst.FORMATION_START_Y - row * KfsConst.ALIEN_SPACING_Y;

                Entity alien = createEntity();
                addComponent(alien, new PositionComp(x, y));
                addComponent(alien, new SizeComp(KfsConst.ALIEN_WIDTH, KfsConst.ALIEN_HEIGHT));
                addComponent(alien, new RenderComp(tex));
                addComponent(alien, new AlienComp(row, col, points));
            }
        }
    }

    public void createShields() {
        float shieldWidth = 7 * KfsConst.SHIELD_BLOCK_SIZE;
        float totalShieldsWidth = KfsConst.SHIELD_COUNT * shieldWidth;
        float spacing = (width - totalShieldsWidth) / (KfsConst.SHIELD_COUNT + 1);

        for (int s = 0; s < KfsConst.SHIELD_COUNT; s++) {
            float baseX = spacing + s * (shieldWidth + spacing);
            float baseY = KfsConst.SHIELD_Y;

            // Classic inverted-U shape: 7 wide x 5 tall, with bottom-center gap
            int[][] shape = {
                {0, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 1, 1},
                {1, 1, 0, 0, 0, 1, 1},
            };

            for (int row = 0; row < shape.length; row++) {
                for (int col = 0; col < shape[row].length; col++) {
                    if (shape[row][col] == 1) {
                        float x = baseX + col * KfsConst.SHIELD_BLOCK_SIZE;
                        // row 0 is top, so invert Y
                        float y = baseY + (shape.length - 1 - row) * KfsConst.SHIELD_BLOCK_SIZE;

                        Entity block = createEntity();
                        addComponent(block, new PositionComp(x, y));
                        addComponent(block, new SizeComp(KfsConst.SHIELD_BLOCK_SIZE, KfsConst.SHIELD_BLOCK_SIZE));
                        addComponent(block, new RenderComp(TEX_SHIELD));
                        addComponent(block, new ShieldBlockComp(3));
                    }
                }
            }
        }
    }

    public void spawnPlayerBullet(float x, float y) {
        // Only one player bullet at a time
        if (!getEntitiesWith(BulletComp.class, b -> ((BulletComp) b).playerBullet).isEmpty()) {
            return;
        }
        Entity e = createEntity();
        addComponent(e, new PositionComp(x, y));
        addComponent(e, new VelocityComp(0, KfsConst.PLAYER_BULLET_SPEED));
        addComponent(e, new SizeComp(KfsConst.BULLET_WIDTH, KfsConst.BULLET_HEIGHT));
        addComponent(e, new RenderComp(TEX_BULLET_PLAYER));
        addComponent(e, new BulletComp(true));
        sound.play("shoot");
    }

    public void spawnAlienBullet(float x, float y) {
        Entity e = createEntity();
        addComponent(e, new PositionComp(x, y));
        addComponent(e, new VelocityComp(0, -KfsConst.ALIEN_BULLET_SPEED));
        addComponent(e, new SizeComp(KfsConst.BULLET_WIDTH, KfsConst.BULLET_HEIGHT));
        addComponent(e, new RenderComp(TEX_BULLET_ALIEN));
        addComponent(e, new BulletComp(false));
    }

    public void spawnMysteryShip() {
        boolean fromLeft = random.nextBoolean();
        float x = fromLeft ? -KfsConst.MYSTERY_WIDTH : width;
        float vx = fromLeft ? KfsConst.MYSTERY_SPEED : -KfsConst.MYSTERY_SPEED;
        int[] possiblePoints = {50, 100, 150, 200, 300};
        int points = possiblePoints[random.nextInt(possiblePoints.length)];

        Entity e = createEntity();
        addComponent(e, new PositionComp(x, height - 50));
        addComponent(e, new VelocityComp(vx, 0));
        addComponent(e, new SizeComp(KfsConst.MYSTERY_WIDTH, KfsConst.MYSTERY_HEIGHT));
        addComponent(e, new RenderComp(TEX_MYSTERY));
        addComponent(e, new MysteryShipComp(points));
        sound.play("mystery");
    }

    public void spawnExplosion(float x, float y) {
        Entity e = createEntity();
        addComponent(e, new PositionComp(x, y));
        addComponent(e, new SizeComp(KfsConst.ALIEN_WIDTH, KfsConst.ALIEN_HEIGHT));
        addComponent(e, new RenderComp(TEX_EXPLOSION));
        addComponent(e, new ExplosionComp(KfsConst.EXPLOSION_DURATION));
    }

    // --- Game events ---

    public void onAlienKilled(Entity alien) {
        AlienComp ac = getComponent(alien, AlienComp.class);
        PositionComp pc = getComponent(alien, PositionComp.class);
        if (ac != null && pc != null) {
            addScore(ac.points);
            spawnExplosion(pc.pos.x, pc.pos.y);
        }
        deleteEntity(alien);
        sound.play("explosion");
    }

    public void onMysteryKilled(Entity mystery) {
        MysteryShipComp mc = getComponent(mystery, MysteryShipComp.class);
        PositionComp pc = getComponent(mystery, PositionComp.class);
        if (mc != null && pc != null) {
            addScore(mc.points);
            spawnExplosion(pc.pos.x, pc.pos.y);
        }
        deleteEntity(mystery);
        sound.play("explosion");
    }

    public void onPlayerHit() {
        for (Entity pe : getEntitiesWith(PlayerComp.class)) {
            PlayerComp p = getComponent(pe, PlayerComp.class);
            p.lives--;
            p.alive = false;
            p.respawnTimer = KfsConst.PLAYER_RESPAWN_TIME;
            sound.play("player_die");

            PositionComp pc = getComponent(pe, PositionComp.class);
            if (pc != null) {
                spawnExplosion(pc.pos.x, pc.pos.y);
            }
        }
    }

    public void nextWave() {
        wave++;
        // Remove old aliens, bullets, formation
        for (Entity e : getEntitiesWith(AlienComp.class)) deleteEntity(e);
        for (Entity e : getEntitiesWith(BulletComp.class)) deleteEntity(e);
        for (Entity e : getEntitiesWith(FormationComp.class)) deleteEntity(e);
        for (Entity e : getEntitiesWith(MysteryShipComp.class)) deleteEntity(e);
        for (Entity e : getEntitiesWith(ExplosionComp.class)) deleteEntity(e);
        for (Entity e : getEntitiesWith(ShieldBlockComp.class)) deleteEntity(e);

        createFormation();
        createAlienFormation();
        createShields();
    }

    public void gameOver() {
        if (!gameOverFlag) {
            gameOverFlag = true;
            if (gameOverCallback != null) {
                gameOverCallback.run();
            }
        }
    }

    public boolean isGameOver() {
        return gameOverFlag;
    }

    public int getScore() {
        for (Entity e : getEntitiesWith(PlayerComp.class)) {
            return getComponent(e, PlayerComp.class).score;
        }
        return 0;
    }

    public int getLives() {
        for (Entity e : getEntitiesWith(PlayerComp.class)) {
            return getComponent(e, PlayerComp.class).lives;
        }
        return 0;
    }

    private void addScore(int points) {
        for (Entity e : getEntitiesWith(PlayerComp.class)) {
            getComponent(e, PlayerComp.class).score += points;
        }
    }

    @Override
    public void done() {
        super.done();
        textures.values().forEach(Texture::dispose);
    }

    // --- Procedural texture generation ---

    private static Texture generateRect(int w, int h, Color color) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    private static Texture generatePlayerTexture() {
        int w = 36, h = 20;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(Color.LIME);
        // Base
        pm.fillRectangle(0, h - 6, w, 6);
        // Middle
        pm.fillRectangle(4, h - 12, w - 8, 6);
        // Cannon
        pm.fillRectangle(w / 2 - 2, 0, 4, h - 12);
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    private static Texture generateAlienTexture(int type) {
        int w = 28, h = 20;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);

        switch (type) {
            case 1: // Bottom rows — squid shape
                pm.setColor(Color.WHITE);
                pm.fillRectangle(4, 4, 20, 12);
                pm.fillRectangle(0, 8, 4, 8);
                pm.fillRectangle(24, 8, 4, 8);
                pm.setColor(Color.BLACK);
                pm.fillRectangle(8, 8, 4, 4);
                pm.fillRectangle(16, 8, 4, 4);
                break;
            case 2: // Middle rows — crab shape
                pm.setColor(Color.CYAN);
                pm.fillRectangle(6, 4, 16, 12);
                pm.fillRectangle(2, 8, 24, 6);
                pm.fillRectangle(0, 4, 4, 4);
                pm.fillRectangle(24, 4, 4, 4);
                pm.setColor(Color.BLACK);
                pm.fillRectangle(10, 8, 3, 3);
                pm.fillRectangle(15, 8, 3, 3);
                break;
            case 3: // Top row — octopus shape
                pm.setColor(Color.MAGENTA);
                pm.fillRectangle(8, 2, 12, 16);
                pm.fillRectangle(4, 6, 20, 8);
                pm.fillRectangle(0, 10, 28, 4);
                pm.setColor(Color.BLACK);
                pm.fillRectangle(10, 6, 3, 3);
                pm.fillRectangle(15, 6, 3, 3);
                break;
        }

        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    private static Texture generateMysteryTexture() {
        int w = 40, h = 16;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(Color.RED);
        pm.fillRectangle(8, 4, 24, 8);
        pm.fillRectangle(4, 6, 32, 4);
        pm.fillRectangle(0, 8, 40, 4);
        pm.fillRectangle(12, 2, 16, 4);
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    private static Texture generateExplosionTexture() {
        int w = 28, h = 20;
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(Color.YELLOW);
        // Star burst pattern
        pm.fillRectangle(12, 0, 4, h);
        pm.fillRectangle(0, 8, w, 4);
        pm.fillRectangle(4, 2, 4, 4);
        pm.fillRectangle(20, 2, 4, 4);
        pm.fillRectangle(4, 14, 4, 4);
        pm.fillRectangle(20, 14, 4, 4);
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }
}
