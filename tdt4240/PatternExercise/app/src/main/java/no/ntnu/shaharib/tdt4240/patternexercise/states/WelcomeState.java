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
public class WelcomeState extends State implements TouchListener {

    private static WelcomeState welcomeStateInstance;

    private Font welcomeFont, newGameFont, signatureText;

    private WelcomeState() {

        welcomeFont = new Font(255, 255, 255, 100, Typeface.SANS_SERIF, Typeface.NORMAL);
        newGameFont = new Font(255, 255, 255, 50, Typeface.SANS_SERIF, Typeface.NORMAL);
        signatureText = new Font(255, 255, 255, 25, Typeface.SANS_SERIF, Typeface.NORMAL);
        welcomeFont.setTextAlign(Paint.Align.CENTER);
        newGameFont.setTextAlign(Paint.Align.CENTER);
        signatureText.setTextAlign(Paint.Align.CENTER);

    }

    public void draw(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.BLACK);
            canvas.drawText("Welcome to Pong!", Constants.WINDOW_WIDTH/2,
                    Constants.WINDOW_HEIGHT/6, welcomeFont);
            canvas.drawText("Tap to start a new game.", Constants.WINDOW_WIDTH/2,
                    Constants.WINDOW_HEIGHT/4, newGameFont);
            canvas.drawText("By Shahariar Kabir Bhuiyan, shaharib@stud.ntnu.no",
                    Constants.WINDOW_WIDTH/2, Constants.WINDOW_HEIGHT/2, signatureText);
        }
    }

    public static WelcomeState getWelcomeStateInstance() {
        if(null == welcomeStateInstance) {
            welcomeStateInstance = new WelcomeState();
        }
        return welcomeStateInstance;
    }

    @Override
    public boolean onTouchUp(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            getGame().popState();
            getGame().pushState(new PongState());
            return true;
        }
        return false;
    }

}
