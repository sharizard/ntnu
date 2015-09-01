package no.ntnu.shaharib.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import no.ntnu.shaharib.helicopter.R;
import no.ntnu.shaharib.helicopter.HelicopterNormal;
import no.ntnu.shaharib.helicopter.HelicopterAnimation;
import no.ntnu.shaharib.pong.Pong;
import no.ntnu.shaharib.utilities.Constants;
import sheep.game.Game;
/**
 * Created by ShahariarKabir on 19.01.2015.
 * shaharib@stud.ntnu.no
 *
 * Main activity, creates a game object, pushes states of the differnet tasks
 * according to our choice.
 */
public class MyGame extends Activity {
    /* Called when the acitivity is first created */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the game
        Game game = new Game(this, null);
        setContentView(R.layout.activity_main);

        // gets the current height/width of emulator/mobile screen.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.WINDOW_HEIGHT = displayMetrics.heightPixels;
        Constants.WINDOW_WIDTH = displayMetrics.widthPixels;

        // Starts new states according to what we want.
        if(getIntent().getExtras().getInt("menuItem") == 1) {
            game.pushState(new HelicopterNormal());
            setContentView(game);
        }
        else if(getIntent().getExtras().getInt("menuItem") == 2) {
            game.pushState(new HelicopterAnimation());
            setContentView(game);
        }
        else if(getIntent().getExtras().getInt("menuItem") == 3) {
            game.pushState(new Pong());
            setContentView(game);
        }
        else {
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

}
