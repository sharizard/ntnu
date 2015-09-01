package no.ntnu.shaharib.tdt4240.patternexercise.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import no.ntnu.shaharib.tdt4240.patternexercise.R;
import no.ntnu.shaharib.tdt4240.patternexercise.models.Ball;
import no.ntnu.shaharib.tdt4240.patternexercise.models.Paddle;
import no.ntnu.shaharib.tdt4240.patternexercise.utils.Constants;
import sheep.game.Sprite;
import sheep.game.State;
import sheep.graphics.Font;
import sheep.graphics.Image;
import sheep.input.TouchListener;

/**
 * Created by ShahariarKabir on 10.02.2015.
 */
public class PongState extends State implements TouchListener, PropertyChangeListener{

    // Our sprites, two paddles, one ball, two walls.
    private Paddle paddle_1, paddle_2;
    private Ball ball;
    private Sprite wallEast, wallWest;

    private Image paddleImage, ballImage, middleWallImage, wallImage;

    // Sprite for the midline using dots.
    private ArrayList<Sprite> dotMiddle;

    private Font font;

    // Variables which keeps track of the score and increasing speed
    private int score_1, score_2;
    private int speedIncreaseTime;
    private long lastSpeedChange;

    public PongState() {
        ballImage = new Image(R.drawable.ball);
        middleWallImage = new Image(R.drawable.dotwall);
        paddleImage = new Image(R.drawable.paddle);
        wallImage = new Image(R.drawable.wall);

        paddle_1 = new Paddle(paddleImage);
        paddle_1.setPosition(Constants.WINDOW_WIDTH / 2, 10);
        paddle_1.addChangeListener(this);

        paddle_2 = new Paddle(paddleImage);
        paddle_2.setPosition(Constants.WINDOW_WIDTH / 2, Constants.WINDOW_HEIGHT - 60);
        paddle_2.addChangeListener(this);

        ball = Ball.getBallInstance(ballImage);
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
            EndGameState endGameState = EndGameState.getEndGameState();
            getGame().pushState(endGameState);
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
            paddle_2.setPosition(Constants.WINDOW_WIDTH - paddleImage.getWidth() / 2, paddle_2.getPosition().getY());
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

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        System.out.println("Changed property: " + event.getPropertyName() + " [old -> "
                + event.getOldValue() + "] | [new -> " + event.getNewValue() +"]");
    }

}

