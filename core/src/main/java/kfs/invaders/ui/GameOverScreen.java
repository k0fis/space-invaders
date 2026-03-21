package kfs.invaders.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import kfs.invaders.KfsMain;
import kfs.invaders.ScoreClient;

public class GameOverScreen extends BaseScreen {

    private final int score;
    private final Table table;
    private final char[] nameChars = {'A', 'A', 'A', 'A', 'A', 'A'};

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

        // Instruction
        Label.LabelStyle hintStyle = new Label.LabelStyle(fontSmall, Color.GRAY);
        table.add(new Label("TAP TO CHANGE", hintStyle)).padBottom(15).row();

        // Letter picker - 3 tappable letters
        Table nameRow = new Table();
        for (int i = 0; i < nameChars.length; i++) {
            final int pos = i;
            TextButton letterBtn = new TextButton(
                String.valueOf(nameChars[pos]),
                getTextButtonStyle(fontBig, Color.WHITE));
            letterBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    nameChars[pos] = (char) ((nameChars[pos] - 'A' + 1) % 26 + 'A');
                    letterBtn.setText(String.valueOf(nameChars[pos]));
                }
            });
            nameRow.add(letterBtn).width(55).height(60).pad(3);
        }
        table.add(nameRow).padBottom(20).row();

        // Submit
        TextButton submitButton = new TextButton("SUBMIT", getTextButtonStyle(fontMiddle, Color.LIME));
        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String name = new String(nameChars);
                showSubmitting();
                ScoreClient.submitScore(name, score, new ScoreClient.SubmitCallback() {
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
        table.add(submitButton).width(250).height(60).padBottom(20).row();

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

        if (isNewRecord) {
            Label.LabelStyle recordStyle = new Label.LabelStyle(fontMiddle, Color.LIME);
            table.add(new Label("#" + rank + " NEW RECORD!", recordStyle)).padBottom(10).row();
        } else {
            Label.LabelStyle rankStyle = new Label.LabelStyle(fontMiddle, Color.CYAN);
            table.add(new Label("RANK #" + rank, rankStyle)).padBottom(10).row();
        }

        Label.LabelStyle bestStyle = new Label.LabelStyle(fontSmall, Color.WHITE);
        table.add(new Label("BEST: " + personalBest, bestStyle)).padBottom(40).row();

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
