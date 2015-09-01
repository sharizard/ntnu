package no.ntnu.shaharib.helicopter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.ArrayList;

import no.ntnu.shaharib.utilities.Constants;
import sheep.collision.CollisionLayer;
import sheep.collision.CollisionListener;
import sheep.game.Sprite;
import sheep.game.State;
import sheep.graphics.Font;
import sheep.graphics.Image;
import sheep.input.TouchListener;
import sheep.math.Vector2;

/**
 * Created by ShahariarKabir on 27.01.2015.
 * shaharib@stud.ntnu.no
 *
 * Task 3 - Animation, timing and collision detection.
 *
 * Main sprite is helicopter, which does animate.
 * Rest of the sprites are balloons, the balloons does not have animation.
 *
 * Known bugs: Helicopter and the balloons might get stucked to left/right side
 * sometimes. Also they might get stucked to each other.
 * I've tried the setPosition method, didnt work most of the times.
 * I could have added walls instead of checking the screen boundaries so the sprites does
 * not get stucked to the sides.
 */
public class HelicopterAnimation extends State implements TouchListener {

    private Image helicopterImage_1 = new Image(R.drawable.helianim1);
    private Image helicopterImage_2 = new Image(R.drawable.helianim2);
    private Image helicopterImage_3 = new Image(R.drawable.helianim3);
    private Image helicopterImage_4 = new Image(R.drawable.helianim4);

    private Image backgroundImage = new Image(R.drawable.bg);
    private Image balloonImage = new Image(R.drawable.balloon);

    // Variables for animation and timing.
    private int heliSpriteNo = 1;
    private long lastSpriteChange = 0;

    private Sprite background, helicopter;

    // Width/height of the sprites, used to check the edges.
    private float helicopterWidth, helicopterHeight, balloonWidth, balloonHeight;

    private boolean facingRightHeli = true;

    private Font positionText;
    private String position;

    // Arraylist for the balloons.
    private ArrayList<Sprite> balloons;

    public HelicopterAnimation() {

        background = new Sprite(backgroundImage);

        helicopter = new Sprite(helicopterImage_1);
        helicopter.setPosition(Constants.WINDOW_WIDTH / 2 + 100, Constants.WINDOW_HEIGHT / 2 + 100);
        helicopter.setSpeed(80, 30);
        helicopter.setScale(1, -1);

        helicopterWidth = helicopterImage_1.getWidth();
        helicopterHeight = helicopterImage_1.getHeight();
        balloonWidth = balloonImage.getWidth();
        balloonHeight = balloonImage.getHeight();

        // Using some random speed on the balloons.
        balloons = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            balloons.add(new Sprite(balloonImage));
            balloons.get(i).setPosition(i*150 + 200, i*150 + 200);
            balloons.get(i).setSpeed((int)(Math.random()*400)-150, (int)(Math.random()*400)-150);
        }

        positionText = new Font(255, 17, 17, 50, Typeface.SANS_SERIF, Typeface.NORMAL);
        position = "";
    }

    public void draw(Canvas canvas) {
        if(canvas != null) {
            background.draw(canvas);
            helicopter.draw(canvas);
            for(Sprite balloon : balloons) {
                balloon.draw(canvas);
            }
            // Using Paint to draw text on the screen which is showing the helicopter position.
            Paint p = new Paint();
            p.setColor(Color.RED);
            //canvas.drawText("Position helicopter: (" + helicopter.getX() + ", " + helicopter.getY() + ")", 30, 70, p);
            canvas.drawText(position, 30, 70, positionText);
        }
    }

    public void update(float dt) {

        // Helicopter, checking whether helicopter is about to leave the screen.
        if(helicopter.getX() > (Constants.WINDOW_WIDTH - helicopterWidth/2)) {
            System.out.println("HelicopterCrash east border!");
            turnLeft(helicopter);
            facingRightHeli = false;
        }

        else if(helicopter.getX() < 0 + helicopterWidth/2) {
            System.out.println("HelicopterCrash west border!");
            turnRight(helicopter);
            facingRightHeli = true;
        }

        else if((helicopter.getY() > (Constants.WINDOW_HEIGHT - helicopterHeight))) {
            System.out.println("HelicopterCrash south border!");
            helicopter.setSpeed(helicopter.getSpeed().getX(), -helicopter.getSpeed().getY());
            helicopter.setPosition(helicopter.getPosition().getX(), helicopter.getPosition().getY()-1);
        }

        else if(helicopter.getY() < helicopterHeight) {
            System.out.println("HelicopterCrash north border!");
            helicopter.setSpeed(helicopter.getSpeed().getX(), -helicopter.getSpeed().getY());
            helicopter.setPosition(helicopter.getPosition().getX(), helicopter.getPosition().getY()+1);
        }

        // Going through every sprite element on the arraylist, also the balloons.
        for(int i = 0; i < balloons.size(); i++) {
            // cheching whether ballons is about to leave screen or not.
            if (balloons.get(i).getX() > (Constants.WINDOW_WIDTH - balloonWidth) || balloons.get(i).getX() < balloonWidth) {
                balloons.get(i).setSpeed(-balloons.get(i).getSpeed().getX(), balloons.get(i).getSpeed().getY());
            }

            else if (balloons.get(i).getY() > (Constants.WINDOW_HEIGHT - balloonHeight) || balloons.get(i).getY() < balloonHeight) {
                balloons.get(i).setSpeed(balloons.get(i).getSpeed().getX(), -balloons.get(i).getSpeed().getY());
            }

            // COllision detection, checks whether helicopter is about to collide with one of the balloons.
            else if(helicopter.collides(balloons.get(i))) {
                System.out.println("Crash each other!");
                balloons.get(i).setSpeed(-balloons.get(i).getSpeed().getX(), -balloons.get(i).getSpeed().getY());
                balloons.get(i).setPosition(balloons.get(i).getPosition().getX() + 1, balloons.get(i).getPosition().getY() + 1);

                helicopter.setSpeed(-helicopter.getSpeed().getX(), -helicopter.getSpeed().getY());
                if(helicopter.getSpeed().getX() < 0 && facingRightHeli) helicopter.setScale(1, 1);
                else helicopter.setScale(-1, 1);
            }
            else {
                // This loops check wether the balloons are about to collide with each other or not.
                // They bounces of each other in the opposite direction they came from.
                for (int j = i + 1; j < balloons.size(); j++) {
                    if (balloons.get(i).collides(balloons.get(j))) {
                        balloons.get(i).setSpeed(-balloons.get(i).getSpeed().getX(), -balloons.get(i).getSpeed().getY());
                        balloons.get(j).setSpeed(-balloons.get(j).getSpeed().getX(), -balloons.get(j).getSpeed().getY());
                    }
                }
            }
        }

        // ANimation, after 100ms, we change helicopter sprite.
        // Last sprite change keeps track of the time when we last changed helicopter sprite.
        if((System.currentTimeMillis() - lastSpriteChange) >= 100) {
            nextHeliSprite(helicopter);
            lastSpriteChange = System.currentTimeMillis();
        }

        position = "Position helicopter: (" + helicopter.getX() + ", " + helicopter.getY() + ")";

        helicopter.update(dt);
        for(Sprite balloon : balloons) {
            balloon.update(dt);
        }

    }

    // Method for turning a sprite to left (also lets it facing left when hit the right edge).
    public void turnLeft(Sprite sprite) {
        sprite.setScale(-1, 1);
        sprite.setSpeed(-sprite.getSpeed().getX(), sprite.getSpeed().getY());
        sprite.setPosition(sprite.getPosition().getX()-1, sprite.getPosition().getY());
    }

    public void turnRight(Sprite sprite) {
        sprite.setScale(1, 1);
        sprite.setSpeed(-sprite.getSpeed().getX(), sprite.getSpeed().getY());
        sprite.setPosition(sprite.getPosition().getX()+1, sprite.getPosition().getY());
    }

    // Method for animation. Starts with killing the sprite, then replacing it depended on
    // which spriteNr we have.
    public void nextHeliSprite(Sprite sprite) {
        Vector2 position = sprite.getPosition();
        Vector2 speed = sprite.getSpeed();
        sprite.die();

        // Chech if helicopter is facing right first so we know which picture to use.
        if(facingRightHeli) {
            switch(heliSpriteNo) {
                case 1:
                    helicopter = new Sprite(helicopterImage_2);
                    heliSpriteNo++;
                    break;
                case 2:
                    helicopter = new Sprite(helicopterImage_3);
                    heliSpriteNo++;
                    break;
                case 3:
                    helicopter = new Sprite(helicopterImage_4);
                    heliSpriteNo++;
                    break;
                default:
                    helicopter = new Sprite(helicopterImage_1);
                    heliSpriteNo = 1;
                    break;
            }
            // turns the helicopter so it is facing the right way.
            helicopter.setScale(1, 1);
        }
        else {
            switch(heliSpriteNo) {
                case 1:
                    helicopter = new Sprite(helicopterImage_2);
                    heliSpriteNo++;
                    break;
                case 2:
                    helicopter = new Sprite(helicopterImage_3);
                    heliSpriteNo++;
                    break;
                case 3:
                    helicopter = new Sprite(helicopterImage_4);
                    heliSpriteNo++;
                    break;
                default:
                    helicopter = new Sprite(helicopterImage_1);
                    heliSpriteNo = 1;
                    break;
            }
            // turn/flip the helicopter so it is facing the right way.
            helicopter.setScale(-1, 1);
        }
        helicopter.setPosition(position);
        helicopter.setSpeed(speed);
    }

    // For touch function, if you tap, the helicopter will move to that position.
    @Override
    public boolean onTouchUp(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(eventX - helicopter.getX() > 0) {
                if(!facingRightHeli) {
                    helicopter.setScale(-1, 1);
                    facingRightHeli = true;
                }
            }
            else {
                helicopter.setScale(1,1);
                facingRightHeli = false;
            }
            helicopter.setSpeed(eventX - helicopter.getX(), eventY - helicopter.getY());
            return true;
        }
        return false;
    }

}
