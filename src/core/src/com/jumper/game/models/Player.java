package com.jumper.game.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import java.util.Locale;

public class Player {
    private static final int PLAYER_MOVEMENT_STATES = 4;
    private static final String PLAYER_MOVEMENT_TEMPLATE = "player-%d.png";
    private static final String PLAYER_DEATH = "player-dead.png";
    private static final float PLAYER_JUMP_VELOCITY = -10.0f;

    private Texture[] movement;
    private Texture death;
    private Rectangle hitBox;
    private int state;
    private float velocity;
    private int posY;
    private int posX;

    public Player(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.movement = new Texture[PLAYER_MOVEMENT_STATES];
        this.death = new Texture(PLAYER_DEATH);

        for (int i = 0; i < PLAYER_MOVEMENT_STATES; i++) {
            this.movement[i] =
                    new Texture(String.format(Locale.getDefault(),PLAYER_MOVEMENT_TEMPLATE, i));
        }
    }

    public void jump() {
        this.velocity = PLAYER_JUMP_VELOCITY;
    }

    public Texture getCurrentTexture() {
        return movement[state];
    }

    public void updateHitBox() {
        hitBox = new Rectangle(
                getPosX(),
                getPosY(),
                getCurrentTexture().getWidth(),
                getCurrentTexture().getHeight());
    }

    public Rectangle getPlayerHitBox() {
        return hitBox;
    }

    public Texture getDeath() {
        return death;
    }

    public int getPosX() {
        return posX - this.getCurrentTexture().getWidth() / 2;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        if (posY <= 0) {
            posY = 0;
        }

        this.posY = posY;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state >= movement.length - 1) {
            state = 0;
        }

        this.state = state;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
