package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends ScreenAdapter {

    private static final float MOVE_TIME = 1F;
    private static final int SNAKE_MOVEMENT = 10;
    // 640 (width) / 32 (snake width) = 20, 480 / 32 = 15 (for a grid of 20 x 15)
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;


    private float timer = MOVE_TIME;
    private int snakeX = 0, snakeY = 0;
    private int snakeDirection = RIGHT;

    private SpriteBatch batch;
    private Texture snakeHead;

    @Override
    public void show() {
        batch = new SpriteBatch();
        snakeHead = new Texture(Gdx.files.internal("snakehead.png"));
    }

    @Override
    public void render(float delta) {
        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            snakeX += SNAKE_MOVEMENT;
        }
        checkForOutOfBounds();
        moveSnake();
        queryInput();
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(snakeHead, snakeX, snakeY);
        // Our rendering code will live here
        batch.draw(snakeHead, snakeX, snakeY);
        batch.end();
    }

    private void checkForOutOfBounds() {
        if (snakeX >= Gdx.graphics.getWidth()) {
            snakeX = 0;
        }
        if (snakeX < 0) {
            snakeX = Gdx.graphics.getWidth() - SNAKE_MOVEMENT;
        }
        if (snakeY >= Gdx.graphics.getHeight()) {
            snakeY = 0;
        }
        if (snakeY < 0) {
            snakeY = Gdx.graphics.getHeight() - SNAKE_MOVEMENT;
        }
    }

    private void moveSnake() {
        switch (snakeDirection) {
            case RIGHT: {
                snakeX += SNAKE_MOVEMENT;
                return;
            }
            case LEFT: {
                snakeX -= SNAKE_MOVEMENT;
                return;
            }
            case UP: {
                snakeY += SNAKE_MOVEMENT;
                return;
            }
            case DOWN: {
                snakeY -= SNAKE_MOVEMENT;
                return;
            }
        }
    }

    private void queryInput() {
        boolean leftKeyPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rightKeyPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean upKeyPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean downKeyPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (leftKeyPressed) snakeDirection = LEFT;
        if (rightKeyPressed) snakeDirection = RIGHT;
        if (upKeyPressed) snakeDirection = UP;
        if (downKeyPressed) snakeDirection = DOWN;
    }
}
