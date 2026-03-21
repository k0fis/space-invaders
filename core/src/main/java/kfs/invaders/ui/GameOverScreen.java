package kfs.invaders.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import kfs.invaders.KfsMain;

public class GameOverScreen extends BaseScreen {

    public GameOverScreen(KfsMain game, int score) {
        super(game);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Game Over title
        Label.LabelStyle titleStyle = new Label.LabelStyle(fontBig, Color.RED);
        Label gameOverLabel = new Label("GAME OVER", titleStyle);
        table.add(gameOverLabel).padBottom(40).row();

        // Score
        Label.LabelStyle scoreStyle = new Label.LabelStyle(fontMiddle, Color.YELLOW);
        Label scoreLabel = new Label("SCORE: " + score, scoreStyle);
        table.add(scoreLabel).padBottom(60).row();

        // Play Again button
        TextButton playAgainButton = new TextButton("PLAY AGAIN", getTextButtonStyle(fontMiddle, Color.WHITE));
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
        table.add(playAgainButton).width(300).height(60).padBottom(20).row();

        // Menu button
        TextButton menuButton = new TextButton("MENU", getTextButtonStyle(fontMiddle, Color.WHITE));
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
            }
        });
        table.add(menuButton).width(250).height(60).padBottom(20).row();

        stage.addActor(table);
    }
}
