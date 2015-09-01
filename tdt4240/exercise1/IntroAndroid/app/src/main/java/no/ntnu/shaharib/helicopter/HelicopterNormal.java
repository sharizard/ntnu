package no.ntnu.shaharib.helicopter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import no.ntnu.shaharib.utilities.Constants;
import sheep.game.Sprite;
import sheep.game.State;
import sheep.graphics.Font;
import sheep.graphics.Image;
import sheep.input.TouchListener;

/**
 * Created by ShahariarKabir on 19.01.2015.
 * shaharib@stud.ntnu.no
 *
 * This is the first and second task of the intro android exercise.
 *
 * Helicopter sprite flying around. When it hit the edges of the screen, it
 * changes it position.
 *
 * Use the touch function to control the movement.
 * - Drag to drag helicopter. However, it will stand still after you let it go.
 * - Tap somewhere on the screen and the helicopter will fly that way.
 *
 * Helicopter coordinates is showing on the screen.
 *
 * Known bugs:
 * - Helicopter might get stucked to the edges. Tried to use setPosition after hitting
 * edge, didnt work well.
 *
 */
public class HelicopterNormal extends State implements TouchListener {

    private Image helicopter = new Image(R.drawable.heli);
    private Image background = new Image(R.drawable.bg);
    private Sprite heliSprite, backSprite;

    private float helicopterWidth, helicopterHeight;

    public boolean facingRight = true;

    private Font positionText;
    private String position;

    public HelicopterNormal() {
        backSprite = new Sprite(background);
        heliSprite = new Sprite(helicopter);

        // default start position and speed.
        heliSprite.setPosition(300, 600);
        heliSprite.setScale(-1, 1);
        heliSprite.setSpeed(100, 0);

        helicopterWidth = helicopter.getWidth();
        helicopterHeight = helicopter.getHeight();

        positionText = new Font(255, 17, 17, 50, Typeface.SANS_SERIF, Typeface.NORMAL);
        position = "";
    }

    public void draw(Canvas canvas) {
        if(canvas != null) {
            backSprite.draw(canvas);
            heliSprite.draw(canvas);
            canvas.drawText(position, 30, 70, positionText);
        }
    }

    public void update(float dt) {
        // The if statements below check wether helicopter is about to leave screen or not.
        // Should bounce the opposite way when hitting an edge.
        if(heliSprite.getX() > Constants.WINDOW_WIDTH - helicopterWidth) {
            System.out.println("Crash east border!");
            turnLeft();
        }

        if(heliSprite.getX() < helicopterWidth) {
            System.out.println("Crash west border!");
            turnRight();
        }

        if(heliSprite.getY() > Constants.WINDOW_HEIGHT - helicopterHeight) {
            System.out.println("Crash south border!");
            heliSprite.setPosition(heliSprite.getPosition().getX(), heliSprite.getPosition().getY()-1);
            heliSprite.setSpeed(heliSprite.getSpeed().getX(), -heliSprite.getSpeed().getY());
        }

        if(heliSprite.getY() < helicopterHeight + 20) {
            System.out.println("Crash north border!");
            heliSprite.setPosition(heliSprite.getPosition().getX(), heliSprite.getPosition().getY()+1);
            heliSprite.setSpeed(heliSprite.getSpeed().getX(), -heliSprite.getSpeed().getY());
        }

        position = "Position helicopter: (" + heliSprite.getX() + ", " + heliSprite.getY() + ")";
        heliSprite.update(dt);
    }

    @Override
    // ONTouchUp method for what should happen when one is tapping on the screen.
    public boolean onTouchUp(MotionEvent event) {
        // if tap, the helicopter should move to the location where you've tapped.
        if(event.getAction() == MotionEvent.ACTION_UP) {
            // Makes sure that helicopter is facing right direction.
            if(event.getX() - heliSprite.getX() > 0) {
                if(!facingRight) {
                    heliSprite.setScale(-1, 1);
                    facingRight = true;
                }
            }
            else {
                heliSprite.setScale(1,1);
                facingRight = false;
            }
            heliSprite.setSpeed(event.getX() - heliSprite.getX(), event.getY() - heliSprite.getY());
            return true;
        }
        return false;
    }

    @Override
    // Method so you can drag the helicopter around the screen.
    public boolean onTouchMove(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            heliSprite.setPosition(event.getX(), event.getY());
            heliSprite.setSpeed(50, 100);
            return true;
        }
        return false;
    }

    // method for turning left and right below.
    public void turnLeft() {
        heliSprite.setScale(1, 1);
        heliSprite.setSpeed(-heliSprite.getSpeed().getX(), heliSprite.getSpeed().getY());
        facingRight = false;
    }

    public void turnRight() {
        heliSprite.setScale(-1, 1);
        heliSprite.setSpeed(-heliSprite.getSpeed().getX(), heliSprite.getSpeed().getY());
        facingRight = true;
    }
}
