package kfs.invaders.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import kfs.invaders.KfsMain;
import kfs.invaders.ScoreClient;

import java.util.List;

public class MainScreen extends BaseScreen {

    public MainScreen(KfsMain game) {
        super(game);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle(fontBig, Color.LIME);
        Label titleLabel = new Label("SPACE", titleStyle);
        table.add(titleLabel).padBottom(5).row();
        Label titleLabel2 = new Label("INVADERS", titleStyle);
        table.add(titleLabel2).padBottom(30).row();

        // Dedication
        Label.LabelStyle smallStyle = new Label.LabelStyle(fontSmall, Color.CYAN);
        Label dedicationLabel = new Label("pro Kubu", smallStyle);
        table.add(dedicationLabel).padBottom(20).row();

        // High score (loaded async from server)
        Label.LabelStyle hiStyle = new Label.LabelStyle(fontSmall, Color.YELLOW);
        Label hiScoreLabel = new Label("", hiStyle);
        table.add(hiScoreLabel).padBottom(40).row();

        ScoreClient.getTopScores(1, new ScoreClient.TopScoresCallback() {
            @Override
            public void onSuccess(List<ScoreClient.ScoreEntry> scores) {
                if (!scores.isEmpty()) {
                    ScoreClient.ScoreEntry top = scores.get(0);
                    hiScoreLabel.setText("HI-SCORE: " + top.score + " " + top.playerName);
                }
            }

            @Override
            public void onError(String message) {
                // offline - no high score shown
            }
        });

        // Play button
        TextButton playButton = new TextButton("PLAY", getTextButtonStyle(fontMiddle, Color.WHITE));
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
        table.add(playButton).width(250).height(60).padBottom(20).row();

        // Instructions
        Label.LabelStyle instrStyle = new Label.LabelStyle(fontSmall, Color.GRAY);
        table.add(new Label("Arrows/Touch: Move", instrStyle)).padBottom(8).row();
        table.add(new Label("Space/Touch: Shoot", instrStyle)).padBottom(8).row();

        stage.addActor(table);
    }
}
