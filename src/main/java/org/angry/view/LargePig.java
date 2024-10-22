package org.angry.view;

import com.badlogic.gdx.math.Ellipse;
import org.angry.Model.World;

public class LargePig extends Pig {

    public LargePig(World world, Ellipse ellipse) {
        super(world, ellipse);
        this.health = 100; // Maximum health
        setSize(this.getWidth(), this.getHeight()); // Default large size
    }
}
