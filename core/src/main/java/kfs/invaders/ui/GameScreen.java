package kfs.invaders.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import kfs.invaders.KfsConst;
import kfs.invaders.KfsMain;
import kfs.invaders.SoundManager;
import kfs.invaders.World;

public class GameScreen extends BaseScreen {

    private final World world;
    private final SpriteBatch batch;
    private final BitmapFont hudFont;
    private final SoundManager soundManager;

    public GameScreen(KfsMain game) {
        super(game);
        batch = new SpriteBatch();
        hudFont = new BitmapFont(Gdx.files.internal("fonts/PressStart2P-10.fnt"));
        soundManager = new SoundManager();

        world = new World(KfsConst.WORLD_WIDTH, KfsConst.WORLD_HEIGHT, soundManager);
        world.startGame(() -> {
            Gdx.app.postRunnable(() -> game.setScreen(new GameOverScreen(game, world.getScore())));
        });
    }

    @Override
    public void show() {
        // Clear input processor — we use polling in InputSys, not stage events
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cap delta to prevent huge jumps
        float dt = Math.min(delta, 0.05f);

        world.update(dt);

        FitViewport viewport = (FitViewport) stage.getViewport();
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        batch.begin();
        world.render(batch);

        // HUD
        hudFont.setColor(Color.WHITE);
        hudFont.draw(batch, "SCORE: " + world.getScore(), 10, KfsConst.WORLD_HEIGHT - 10);
        hudFont.draw(batch, "LIVES: " + world.getLives(), KfsConst.WORLD_WIDTH - 130, KfsConst.WORLD_HEIGHT - 10);
        hudFont.draw(batch, "WAVE: " + world.getWave(), KfsConst.WORLD_WIDTH / 2f - 40, KfsConst.WORLD_HEIGHT - 10);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        hudFont.dispose();
        soundManager.dispose();
        world.done();
    }
}
