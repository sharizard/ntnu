package no.ntnu.shaharib.tdt4240.patternexercise;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import no.ntnu.shaharib.tdt4240.patternexercise.states.WelcomeState;
import no.ntnu.shaharib.tdt4240.patternexercise.utils.Constants;
import sheep.game.Game;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Game game = new Game(this, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.WINDOW_HEIGHT = displayMetrics.heightPixels;
        Constants.WINDOW_WIDTH = displayMetrics.widthPixels;

        WelcomeState welcomeState = WelcomeState.getWelcomeStateInstance();

        game.pushState(welcomeState);
        setContentView(game);


    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
