package no.ntnu.shaharib.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import no.ntnu.shaharib.utilities.Constants;
import sheep.game.Sprite;
import sheep.game.State;
import sheep.graphics.Font;
import sheep.input.TouchListener;

/**
 * Created by ShahariarKabir on 01.02.2015.
 * shaharib@stud.ntnu.no
 *
 * ENd game screen after a pong game.
 * It simply displays "Game over, tap to play again.".
 *
 * Doesnt show the winner, might fix it for the next exercise using
 * a player object to keep track of score etc.
 *
 */
public class EndGame extends State implements TouchListener {

    // This state will show two texts.
    private Font gameOver, tapToPlayAgain;

    public EndGame() {

        gameOver = new Font(255, 255, 255, 100, Typeface.SANS_SERIF, Typeface.NORMAL);
        tapToPlayAgain = new Font(255, 255, 255, 50, Typeface.SANS_SERIF, Typeface.NORMAL);
        gameOver.setTextAlign(Paint.Align.CENTER);
        tapToPlayAgain.setTextAlign(Paint.Align.CENTER);

    }

    // drwaing the necessary elements on the screen.
    public void draw(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.BLACK);
            canvas.drawText("Game over!", Constants.WINDOW_WIDTH/2, Constants.WINDOW_HEIGHT / 2, gameOver);
            canvas.drawText("Tap to play again.", Constants.WINDOW_WIDTH/2, Constants.WINDOW_HEIGHT/2 + 400, tapToPlayAgain);
        }
    }

    // Tap on the screen to pop current state, push a new state
    // aka start a new game.
    @Override
    public boolean onTouchUp(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            getGame().popState();
            getGame().pushState(new Pong());
            return true;
        }
        return false;
    }
}
