package kfs.invaders;

import com.badlogic.gdx.Game;
import kfs.invaders.ui.MainScreen;

public class KfsMain extends Game {

    public MusicManager music;

    @Override
    public void create() {
        music = new MusicManager("music/");
        setScreen(new MainScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (music != null) {
            music.dispose();
        }
    }
}
