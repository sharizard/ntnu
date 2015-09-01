package no.ntnu.shaharib.tdt4240.patternexercise.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import sheep.game.Sprite;
import sheep.graphics.Image;

/**
 * Created by ShahariarKabir on 15.02.2015.
 */
public class Paddle extends Sprite {
    private float x;
    private float y;

    private String paddleName;

    private List<PropertyChangeListener> listener = new ArrayList<>();

    public Paddle(Image image, String paddleName) {
        super(image);
        x = 0;
        y = 0;
        this.paddleName = paddleName;
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        notifyListeners(this, paddleName + ", X-POS: ", this.x,
                this.x = x);
        notifyListeners(this, paddleName + ", Y-POS: ", this.y,
                this.y = y);
    }

    private void notifyListeners(Object object, String property, float oldValue,
                                 float newValue) {
        for(PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

}
