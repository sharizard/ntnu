package no.ntnu.shaharib.tdt4240.patternexercise.models;

import sheep.game.Sprite;
import sheep.graphics.Image;

/**
 * Created by ShahariarKabir on 10.02.2015.
 */
public class Ball extends Sprite {

    private static Ball ballInstance;

    private Ball(Image image) {
        super(image);
    }

    public static Ball getBallInstance(Image image) {
        if(null == ballInstance) {
            ballInstance = new Ball(image);
        }
        return ballInstance;
    }
}
