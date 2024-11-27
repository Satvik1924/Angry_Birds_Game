package org.angry.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import org.angry.view.Polygon;

public class B2WorldCreator {
    public static World world;

    public B2WorldCreator(World world, TiledMap map) {
        this.world = world;

        //Static Ground
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Polygon p = new Polygon(Gdx.graphics.getWidth(), rect.getHeight());
            Body body = world.add(p, Gdx.graphics.getWidth(),0);
            body.setStatic();
            body.setOrient(0);
            body.dynamicFriction = 0.5f;
            body.staticFriction= 0.5f;
            ControllerLogic.EARTH=body;
        }
    }
}
