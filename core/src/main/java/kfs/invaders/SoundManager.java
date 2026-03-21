package kfs.invaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private final Map<String, Sound> sounds = new HashMap<>();

    public SoundManager() {
        loadSound("shoot", "sounds/shoot.wav");
        loadSound("explosion", "sounds/explosion.wav");
        loadSound("player_die", "sounds/player_die.wav");
        loadSound("mystery", "sounds/mystery.wav");
    }

    private void loadSound(String name, String path) {
        FileHandle file = Gdx.files.internal(path);
        if (file.exists()) {
            sounds.put(name, Gdx.audio.newSound(file));
        }
    }

    public void play(String name) {
        Sound s = sounds.get(name);
        if (s != null) {
            s.play(0.5f);
        }
    }

    public void dispose() {
        for (Sound s : sounds.values()) {
            s.dispose();
        }
        sounds.clear();
    }
}
