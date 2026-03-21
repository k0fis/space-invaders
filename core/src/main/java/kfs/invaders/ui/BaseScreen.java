package kfs.invaders.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import kfs.invaders.KfsConst;
import kfs.invaders.KfsMain;

public class BaseScreen extends ScreenAdapter {

    protected final KfsMain game;
    protected final Stage stage;
    protected final Skin skin;
    protected final BitmapFont fontSmall;
    protected final BitmapFont fontMiddle;
    protected final BitmapFont fontBig;

    protected BaseScreen(KfsMain game) {
        this.game = game;
        stage = new Stage(new FitViewport(KfsConst.WORLD_WIDTH, KfsConst.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        fontBig = new BitmapFont(Gdx.files.internal("fonts/PressStart2P-32.fnt"));
        fontMiddle = new BitmapFont(Gdx.files.internal("fonts/PressStart2P-16.fnt"));
        fontSmall = new BitmapFont(Gdx.files.internal("fonts/PressStart2P-10.fnt"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        fontSmall.dispose();
        fontBig.dispose();
        fontMiddle.dispose();
    }

    protected TextButton.TextButtonStyle getTextButtonStyle(BitmapFont font, Color fontColor) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.2f, 0.3f, 0.5f, 0.75f));
        pixmap.fill();

        Texture pixmapTex = new Texture(pixmap);
        pixmap.dispose();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(pixmapTex));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = drawable;
        buttonStyle.down = drawable.tint(Color.YELLOW);
        buttonStyle.fontColor = fontColor;
        buttonStyle.downFontColor = fontColor;

        return buttonStyle;
    }
}
