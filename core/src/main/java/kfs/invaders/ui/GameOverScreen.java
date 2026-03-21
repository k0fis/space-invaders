package kfs.invaders.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import kfs.invaders.KfsMain;
import kfs.invaders.ScoreClient;

public class GameOverScreen extends BaseScreen {

    private final int score;
    private final Table table;
    private String playerName = "AAA";

    public GameOverScreen(KfsMain game, int score) {
        super(game);
        this.score = score;

        table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        showEnterName();
    }

    private void showEnterName() {
        table.clear();

        addHeader();

        // Name input row
        Table inputRow = new Table();

        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle(skin.get(TextField.TextFieldStyle.class));
        tfStyle.font = fontMiddle;
        tfStyle.fontColor = Color.WHITE;

        TextField nameField = new TextField(playerName, tfStyle);
        nameField.setMaxLength(6);
        nameField.setAlignment(Align.center);
        nameField.setTextFieldFilter((textField, c) -> Character.isLetter(c));
        nameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nameField.selectAll();
            }
        });
        inputRow.add(nameField).width(130).height(50).padRight(10);

        TextButton submitButton = new TextButton("SUBMIT", getTextButtonStyle(fontMiddle, Color.LIME));
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerName = nameField.getText().toUpperCase().trim();
                if (playerName.isEmpty()) playerName = "AAA";
                showSubmitting();
                ScoreClient.submitScore(playerName, score, new ScoreClient.SubmitCallback() {
                    @Override
                    public void onSuccess(long rank, int personalBest, boolean isNewRecord) {
                        showResult(rank, personalBest, isNewRecord);
                    }

                    @Override
                    public void onError(String message) {
                        showError("NETWORK ERROR");
                    }
                });
            }
        });
        inputRow.add(submitButton).width(180).height(50);
        table.add(inputRow).padBottom(30).row();

        addPlayAgainAndMenu();
    }

    private void showSubmitting() {
        table.clear();
        addHeader();

        Label.LabelStyle style = new Label.LabelStyle(fontMiddle, Color.CYAN);
        table.add(new Label("SUBMITTING...", style)).padBottom(60).row();
    }

    private void showResult(long rank, int personalBest, boolean isNewRecord) {
        table.clear();
        addHeader();

        // Rank display
        if (isNewRecord) {
            Label.LabelStyle recordStyle = new Label.LabelStyle(fontMiddle, Color.LIME);
            table.add(new Label("#" + rank + " NEW RECORD!", recordStyle)).padBottom(10).row();
        } else {
            Label.LabelStyle rankStyle = new Label.LabelStyle(fontMiddle, Color.CYAN);
            table.add(new Label("RANK #" + rank, rankStyle)).padBottom(10).row();
        }

        // Personal best
        Label.LabelStyle bestStyle = new Label.LabelStyle(fontSmall, Color.WHITE);
        table.add(new Label("BEST: " + personalBest, bestStyle)).padBottom(40).row();

        // Leaderboard button
        TextButton lbButton = new TextButton("LEADERBOARD", getTextButtonStyle(fontMiddle, Color.GOLD));
        lbButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LeaderboardScreen(game, score));
            }
        });
        table.add(lbButton).width(350).height(60).padBottom(20).row();

        addPlayAgainAndMenu();
    }

    private void showError(String message) {
        table.clear();
        addHeader();

        Label.LabelStyle errStyle = new Label.LabelStyle(fontMiddle, Color.RED);
        table.add(new Label(message, errStyle)).padBottom(40).row();

        // Retry button → back to ENTER_NAME
        TextButton retryButton = new TextButton("RETRY", getTextButtonStyle(fontMiddle, Color.YELLOW));
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showEnterName();
            }
        });
        table.add(retryButton).width(250).height(60).padBottom(20).row();

        addPlayAgainAndMenu();
    }

    private void addHeader() {
        Label.LabelStyle titleStyle = new Label.LabelStyle(fontBig, Color.RED);
        table.add(new Label("GAME OVER", titleStyle)).padBottom(40).row();

        Label.LabelStyle scoreStyle = new Label.LabelStyle(fontMiddle, Color.YELLOW);
        table.add(new Label("SCORE: " + score, scoreStyle)).padBottom(30).row();
    }

    private void addPlayAgainAndMenu() {
        TextButton playAgainButton = new TextButton("PLAY AGAIN", getTextButtonStyle(fontMiddle, Color.WHITE));
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
        table.add(playAgainButton).width(300).height(60).padBottom(20).row();

        TextButton menuButton = new TextButton("MENU", getTextButtonStyle(fontMiddle, Color.WHITE));
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
            }
        });
        table.add(menuButton).width(250).height(60).padBottom(20).row();
    }
}
