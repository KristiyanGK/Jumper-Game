package com.jumper.game.collections;

import com.badlogic.gdx.graphics.Texture;
import com.jumper.game.interactables.Coin;

import java.util.ArrayList;

public class CoinCollection {
    private Texture texture;
    private ArrayList<Coin> coins;

    public CoinCollection() {
        texture = new Texture("coin.png");
        coins = new ArrayList<>();
    }

    public Texture getTexture() {
        return texture;
    }

    public void remove(int i) {
        coins.remove(i);
    }

    public Coin get(int i) {
        return coins.get(i);
    }

    public void add(Coin coin) {
        coins.add(coin);
    }

    public int size() {
        return coins.size();
    }

    public void clear() {
        coins.clear();
    }
}
