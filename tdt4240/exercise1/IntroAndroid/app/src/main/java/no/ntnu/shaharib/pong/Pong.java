package no.ntnu.shaharib.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.ArrayList;

import no.ntnu.shaharib.helicopter.R;
import no.ntnu.shaharib.utilities.Constants;
import sheep.game.Sprite;
import sheep.game.State;
import sheep.graphics.Font;
import sheep.graphics.Image;
import sheep.input.TouchListener;

/**
 * Created by ShahariarKabir on 31.01.2015.
 *
 * Task 4 - Create Pong
 *
 * Two paddles on each side of screen.
 * Can move vertically.
 * Multiplayer, might implement AI for design pattern task.
 *
 * Ball start at the center of screen.
 *
 * Points are given if ball slips past a paddle. Winner decided after 5 points, not 21! Takes a long time to test.
 *
 * Paddles are quite big now, so easy to hit the ball. But touch surface is small.
 *
 * Ball increases speed every 5 seconds.
 *
 * Known bugs:
 * - The ball might get stucked to the walls or the paddle sometimes.
 * - Paddle mostly when it hits the edge of the paddle.
 */
public class Pong extends State implements TouchListener{

    // Our sprites, two paddles, one ball, two walls.
    private Sprite paddle_1, paddle_2;
    private Sprite ball;
    private Sprite wallEast, wallWest;

    private Image paddleImage, ballImage, middleWallImage, wallImage;

    // Sprite for the midline using dots.
    private ArrayList<Sprite> dotMiddle;

    private Font font;

    // Variables which keeps track of the score and increasing speed
    private int score_1, score_2;
    private int speedIncreaseTime;
    private long lastSpeedChange;

    public Pong() {
        ballImage = new Image(R.drawable.ball);
        middleWallImage = new Image(R.drawable.dotwall);
        paddleImage = new Image(R.drawable.paddle);
        wallImage = new Image(R.drawable.wall);

        paddle_1 = new Sprite(paddleImage);
        paddle_2 = new Sprite(paddleImage);
        ball = new Sprite(ballImage);
        wallEast = new Sprite(wallImage);
        wallWest = new Sprite(wallImage);

        dotMiddle = new ArrayList<Sprite>();

        // 30 elements so we can use Pong in landscape mode
        for(int i = 0; i < 30; i++) {
            Sprite dot = new Sprite(new Image(R.drawable.dotwall));
            dot.setPosition(i*100, Constants.WINDOW_HEIGHT/2);
            dotMiddle.add(dot);
        }

        score_1 = 0;
        score_2 = 0;
        speedIncreaseTime = 5000;
        lastSpeedChange = 0;

        font = new Font(255, 255, 255, 100, Typeface.SANS_SERIF, Typeface.NORMAL);

        // setting default position.
        ball.setPosition(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT / 2);
        ball.setSpeed(300, 500);

        paddle_1.setPosition(Constants.WINDOW_WIDTH/2, 10);
        paddle_2.setPosition(Constants.WINDOW_WIDTH/2, Constants.WINDOW_HEIGHT-60);

        wallWest.setPosition(10, Constants.WINDOW_HEIGHT/2);
        wallEast.setPosition(Constants.WINDOW_WIDTH-10, Constants.WINDOW_HEIGHT/2);
    }

    // Draws text, sprites.
    public void draw(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.BLACK);
            canvas.drawText(score_1 + "", 100, (Constants.WINDOW_HEIGHT / 2) + 180, font);
            canvas.drawText(score_2 + "", 100, (Constants.WINDOW_HEIGHT / 2) - 100, font);

            ball.draw(canvas);
            wallWest.draw(canvas);
            wallEast.draw(canvas);
            paddle_1.draw(canvas);
            paddle_2.draw(canvas);

            for (Sprite dot : dotMiddle) {
                dot.draw(canvas);
            }
        }
    }

    public void update(float dt) {
        ball.update(dt);
        paddle_1.update(dt);
        paddle_2.update(dt);
        wallWest.update(dt);
        wallEast.update(dt);

        for(Sprite dot : dotMiddle) {
            dot.update(dt);
        }

        // if one of the players gets a score 5, the game ends.
        // not 21, too much!
        if(score_1 == 5 || score_2 == 5) {
            // pop current state, push a new state.
            getGame().popState();
            getGame().pushState(new EndGame());
        }

        // the if statemensts tells us what will happen when the ball
        // collides with the walls, the paddle or goes beyond the paddle.
        if(ball.collides(wallEast) || ball.collides(wallWest)) {
            ball.setSpeed(-ball.getSpeed().getX(), ball.getSpeed().getY());
        }

        else if(ball.collides(paddle_1) || ball.collides(paddle_2)) {
            ball.setSpeed(ball.getSpeed().getX(), -ball.getSpeed().getY());
        }

        // increases the players score if ball goes beyond paddle.
        else if(ball.getY() < paddle_1.getY()) {
            ball.setPosition(Constants.WINDOW_WIDTH/2, Constants.WINDOW_HEIGHT/2);
            ball.setSpeed(300, 500);
            score_1++;
        }
        else if(ball.getY() > paddle_2.getY()) {
            ball.setPosition(Constants.WINDOW_WIDTH/2, Constants.WINDOW_HEIGHT/2);
            ball.setSpeed(300, 500);
            score_2++;
        }

        // we dont want the paddle to go outside the screen.
        if(paddle_1.collides(wallWest)) {
            paddle_1.setPosition(paddleImage.getWidth()/2, paddle_1.getPosition().getY());
        }
        else if(paddle_1.collides(wallEast)) {
            paddle_1.setPosition(Constants.WINDOW_WIDTH - paddleImage.getWidth()/2, paddle_1.getPosition().getY());
        }

        if(paddle_2.collides(wallWest)) {
            paddle_2.setPosition(paddleImage.getWidth()/2, paddle_2.getPosition().getY());
        }
        else if(paddle_2.collides(wallEast)) {
            paddle_2.setPosition(Constants.WINDOW_WIDTH - paddleImage.getWidth()/2, paddle_2.getPosition().getY());
        }

        // increases the speed on the ball every 5 second if no one scores.
        if(System.currentTimeMillis() - lastSpeedChange >= speedIncreaseTime) {
            if(ball.getSpeed().getY() > 0) {
                ball.setSpeed(ball.getSpeed().getX(), ball.getSpeed().getY() + 50);
            }
            else {
                ball.setSpeed(ball.getSpeed().getX(), ball.getSpeed().getY() - 50);
            }
            System.out.println("Ball fart: " + ball.getSpeed().getY());
            lastSpeedChange = System.currentTimeMillis();
        }
    }

    // Using touch to move the paddle. Not multitouch, only one player at a time.
    @Override
    public boolean onTouchMove(MotionEvent event) {
        if(event.getY() > Constants.WINDOW_HEIGHT - 300) {
            paddle_2.setPosition(event.getX(), paddle_2.getY());
            return true;
        }
        if(event.getY() < 300) {
            paddle_1.setPosition(event.getX(), paddle_1.getY());
            return true;
        }
        return false;
    }

}
