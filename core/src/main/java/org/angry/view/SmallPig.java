package org.angry.view;

import com.badlogic.gdx.math.Ellipse;
import org.angry.Model.World;

public class SmallPig extends Pig {

    public SmallPig(World world, Ellipse ellipse) {
        super(world, ellipse);
        this.health = 50; // Less health than the medium and large pigs
        setSize(this.getWidth() * 0.5f, this.getHeight() * 0.5f); // Smaller size
    }
}
