package com.jumper.game.collections;

import com.badlogic.gdx.graphics.Texture;
import com.jumper.game.interactables.Bomb;

import java.util.ArrayList;

public class BombCollection {
    private Texture texture;
    private ArrayList<Bomb> bombs;

    public BombCollection() {
        texture = new Texture("bomb.png");
        bombs = new ArrayList<>();
    }

    public Texture getTexture() {
        return texture;
    }

    public Bomb get(int i) {
        return bombs.get(i);
    }

    public void add(Bomb bomb) {
        bombs.add(bomb);
    }

    public int size() {
        return bombs.size();
    }

    public void clear() {
        bombs.clear();
    }
}
