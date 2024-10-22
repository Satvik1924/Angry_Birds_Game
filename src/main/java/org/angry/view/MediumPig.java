package org.angry.view;

import com.badlogic.gdx.math.Ellipse;
import org.angry.Model.World;

public class MediumPig extends Pig {

    public MediumPig(World world, Ellipse ellipse) {
        super(world, ellipse);
        this.health = 75; // Moderate health
        setSize(this.getWidth() * 0.75f, this.getHeight() * 0.75f); // Medium size
    }
}
