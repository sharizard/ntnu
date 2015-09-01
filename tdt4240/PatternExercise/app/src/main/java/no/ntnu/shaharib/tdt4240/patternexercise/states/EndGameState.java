package no.ntnu.shaharib.tdt4240.patternexercise.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

import no.ntnu.shaharib.tdt4240.patternexercise.utils.Constants;
import sheep.game.State;
import sheep.graphics.Font;
import sheep.input.TouchListener;

/**
 * Created by ShahariarKabir on 10.02.2015.
 */
public class EndGameState extends State implements TouchListener {

    private Font gameOverFont, tapToContinueFont;
    private static EndGameState endGameState;

    private EndGameState() {
        gameOverFont = new Font(255, 255, 255, 100, Typeface.SANS_SERIF, Typeface.NORMAL);
        tapToContinueFont = new Font(255, 255, 255, 50, Typeface.SANS_SERIF, Typeface.NORMAL);
        gameOverFont.setTextAlign(Paint.Align.CENTER);
        tapToContinueFont.setTextAlign(Paint.Align.CENTER);
    }

    public void draw(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.BLACK);
            canvas.drawText("Game over!", Constants.WINDOW_WIDTH/2,
                    Constants.WINDOW_HEIGHT/2, gameOverFont);
            canvas.drawText("Tap to continue.", Constants.WINDOW_WIDTH/2,
                    Constants.WINDOW_HEIGHT/4, gameOverFont);
        }
    }

    public static EndGameState getEndGameState() {
        if(null == endGameState) {
            endGameState = new EndGameState();
        }
        return endGameState;
    }

    @Override
    public boolean onTouchUp(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            getGame().popState();
            getGame().pushState(WelcomeState.getWelcomeStateInstance());
            return true;
        }
        return false;
    }
}
