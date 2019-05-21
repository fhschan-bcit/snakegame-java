package com.mygdx.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;


public class GameScreen extends ScreenAdapter {

    private static final float MOVE_TIME = 1F;
    private static final int SNAKE_MOVEMENT = 32;
    // 640 (width) / 32 (snake width) = 20, 480 / 32 = 15 (for a grid of 20 x 15)
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;


    private float timer = MOVE_TIME;
    private int snakeX = 0, snakeY = 0;
    private int snakeXBeforeUpdate = 0, snakeYBeforeUpdate = 0;
    private int snakeDirection = RIGHT;

    private boolean isAppleAvailable = false;
    private int appleX = 50, appleY = 50;

    private SpriteBatch batch;
    private Texture snakeHead;
    private Texture apple;
    private Texture snakeBody;

    private Array<BodyPart> bodyParts = new Array<>();

    @Override
    public void show() {
        batch = new SpriteBatch();
        snakeHead = new Texture(Gdx.files.internal("snakehead.png"));
        snakeBody = new Texture(Gdx.files.internal("snakehead.png"));
        apple = new Texture(Gdx.files.internal("apple.png"));
    }

    @Override
    public void render(float delta) {
        queryInput();
        // Set timer setting
        timer -= delta;
        if (timer < 0.5) {
            timer = MOVE_TIME;
            moveSnake();
            checkForOutOfBounds();
            updateBodyPartsPosition();
        }

        checkAndPlaceApple();
        checkAppleCollision();
        clearScreen();
        draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        batch.begin();
        batch.draw(snakeHead, snakeX, snakeY);
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.draw(batch);
        }
        // Our rendering code will live here
        // Set apple render settings
        if (isAppleAvailable) {
            batch.draw(apple, appleX, appleY);
        }
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
        snakeXBeforeUpdate = snakeX;
        snakeYBeforeUpdate = snakeY;
        switch (snakeDirection) {
            case RIGHT: {
                snakeX += SNAKE_MOVEMENT;
//                return;
            }
            case LEFT: {
                snakeX -= SNAKE_MOVEMENT;
//                return;
            }
            case UP: {
                snakeY += SNAKE_MOVEMENT;
//                return;
            }
            case DOWN: {
                snakeY -= SNAKE_MOVEMENT;
//                return;
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

    private void checkAndPlaceApple() {
        if (!isAppleAvailable) {
            do {
                appleX = MathUtils.random(Gdx.graphics.getWidth() / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT;
                appleY = MathUtils.random(Gdx.graphics.getHeight() / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT;
                isAppleAvailable = true;
            } while (appleX == snakeX && appleY == snakeY);
        }
    }

    private void checkAppleCollision() {
        if (isAppleAvailable && (appleX == snakeX) && (appleY == snakeY)) {
            BodyPart bodyPart = new BodyPart(snakeBody);
            bodyPart.updateBodyPosition(snakeX, snakeY);
            bodyParts.insert(0, bodyPart);
            isAppleAvailable = false;
        }
    }

    private void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPosition(snakeXBeforeUpdate, snakeYBeforeUpdate);
            bodyParts.add(bodyPart);
        }
    }

    public class BodyPart {
        private int x, y;
        private Texture texture;

        public BodyPart(Texture texture) {
            this.texture = texture;
        }

        public void updateBodyPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Batch batch) {
            if (!(x == snakeX && y == snakeY)) {
                batch.draw(texture, x, y);
            }
        }
    }
}
