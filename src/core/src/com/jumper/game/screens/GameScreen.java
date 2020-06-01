package com.jumper.game.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.jumper.game.JumperGame;
import com.jumper.game.collections.BombCollection;
import com.jumper.game.collections.CoinCollection;
import com.jumper.game.interactables.Bomb;
import com.jumper.game.interactables.Coin;
import com.jumper.game.models.Player;

import java.util.Random;

public class GameScreen extends ApplicationAdapter implements Screen  {
    private static final String BACKGROUND = "bg.png";
    private static final int SPRITE_CHANGE_NUM = 6;

    private Stage stage;
    private Texture background;

    private int pause;
    private float gravity;

    private Player player;

    private CoinCollection coinCollection;
    private int coinSpawnRate;

    private BombCollection bombCollection;
    private int bombSpawnRate;

    BitmapFont font;

    int score;
    int gameState = 0;
    boolean endGame;
    TextButton playButton;

    private Random random;
    private Game game;

    public GameScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());

        background = new Texture(BACKGROUND);

        player = new Player(
                Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

        coinCollection = new CoinCollection();
        coinSpawnRate = 100;

        bombCollection = new BombCollection();
        bombSpawnRate = 0;

        random  = new Random();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        gravity = 0.2f;

        playButton = new TextButton("Game Over!", JumperGame.gameSkin);
        playButton.setWidth(Gdx.graphics.getWidth()/2);
        playButton.setPosition(Gdx.graphics.getWidth()/2-playButton.getWidth()/2,Gdx.graphics.getHeight()/2-playButton.getHeight()/2);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (gameState != 2) {
                    return false;
                }
                return true;
            }
        });
        stage.addActor(playButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.getBatch().begin();

        drawBackground();

        checkGameState();

        if (endGame) {
            stage.getBatch().end();

            stage.act();

            stage.draw();

            return;
        }

        handleObjects();

        handleInput();
        alterPlayerState();
        drawPlayer();
        checkCollision();

        font.draw(stage.getBatch(), String.valueOf(score), 100, 200);

        stage.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void handleObjects() {
        spawnCoins();
        drawCoins();

        spawnBombs();
        drawBombs();
    }

    private void spawnBombs() {
        if (bombSpawnRate < 250) {
            bombSpawnRate++;
            return;
        }

        bombSpawnRate = 0;

        Bomb bomb = new Bomb();
        bomb.X = Gdx.graphics.getWidth();
        bomb.Y = (int) (random.nextFloat() * Gdx.graphics.getHeight());

        bombCollection.add(bomb);
    }

    private void drawBombs() {
        Texture texture = bombCollection.getTexture();

        for (int i = 0; i < bombCollection.size(); i++) {
            Bomb bomb = bombCollection.get(i);
            stage.getBatch().draw(texture, bomb.X, bomb.Y);
            bomb.X -= 6;
            bomb.HitBox = new Rectangle(bomb.X, bomb.Y,
                    texture.getWidth(), texture.getHeight());
        }
    }

    private void checkCollision() {
        for (int i = 0; i < coinCollection.size(); i++) {
            if (Intersector.overlaps(player.getPlayerHitBox(), coinCollection.get(i).HitBox)) {
                score++;

                coinCollection.remove(i);
                break;
            }
        }

        for (int i = 0; i < bombCollection.size(); i++) {
            if (Intersector.overlaps(player.getPlayerHitBox(), bombCollection.get(i).HitBox)) {
                gameState = 2;
                break;
            }
        }
    }

    private void drawCoins() {
        Texture texture = coinCollection.getTexture();
        for (int i = 0; i < coinCollection.size(); i++) {
            Coin coin = coinCollection.get(i);
            stage.getBatch().draw(texture, coin.X, coin.Y);
            coin.X -= 4;
            coin.HitBox = new Rectangle(coin.X, coin.Y,
                    texture.getWidth(), texture.getHeight());
        }
    }

    private void spawnCoins() {
        if (coinSpawnRate < 100) {
            coinSpawnRate++;
            return;
        }

        coinSpawnRate = 0;

        Coin coin = new Coin();
        coin.X = Gdx.graphics.getWidth();
        coin.Y = (int) (random.nextFloat() * Gdx.graphics.getHeight());

        coinCollection.add(coin);
    }

    private void checkGameState() {
        if (gameState != 2) {
            return;
        }

        stage.getBatch().draw(player.getDeath(),
                player.getPosX(),
                player.getPosY());

        if (endGame) {
            return;
        }

        bombCollection.clear();
        coinCollection.clear();

        endGame = true;
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            player.jump();
        }
    }

    private void drawBackground() {
        stage.getBatch().draw(background,
                0, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void alterPlayerState() {
        if (pause < SPRITE_CHANGE_NUM) {
            pause++;
            return;
        }

        pause = 0;
        player.setState(player.getState() + 1);
    }

    private void drawPlayer() {
        player.setVelocity(player.getVelocity() + gravity);
        player.setPosY((int)(player.getPosY() - player.getVelocity()));

        stage.getBatch().draw(player.getCurrentTexture(),
                player.getPosX(),
                player.getPosY());

        player.updateHitBox();
    }
}
