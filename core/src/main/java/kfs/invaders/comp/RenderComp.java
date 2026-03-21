package kfs.invaders.comp;

import kfs.invaders.ecs.KfsComp;

public class RenderComp implements KfsComp {
    public String textureName;

    public RenderComp(String textureName) {
        this.textureName = textureName;
    }
}
