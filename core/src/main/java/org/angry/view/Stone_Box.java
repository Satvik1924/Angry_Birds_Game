package org.angry.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import org.angry.controller.AngryBirds;
import org.angry.Model.Body;
import org.angry.Model.World;

public class Stone_Box extends Sprite implements InputProcessor {
    private World world;
    private Body body;
    private TextureRegion textureRegion;
    public Rectangle rect;
    private Polygon p;
    private float df,sf;
    private boolean dragged=false;

    public Stone_Box(World world, Rectangle rectangle,float df,float sf) {
        super(new Texture("stone_rectangle.png"));
        System.out.println(df+"  "+sf);
        this.df=df;
        this.sf=sf;this.world = world;
        this.rect = rectangle;
        DefinBox();
        if (rectangle.getWidth() > rectangle.getHeight()) {
            textureRegion = new TextureRegion(getTexture(), 0, 0, 1265, 1500);
        } else {
            textureRegion = new TextureRegion(getTexture(), 0, 0, 1265, 2830);
        }
        setRegion(textureRegion);
        setBounds(0, 0, rect.getWidth()*2, rect.getHeight()*2);
        if (EasyLevel.inputMultiplexer != null) {
            EasyLevel.inputMultiplexer.addProcessor(this);
        }
        if (MediumLevel.inputMultiplexer != null) {
            MediumLevel.inputMultiplexer.addProcessor(this);
        }
        if (HardLevel.inputMultiplexer != null) {
            HardLevel.inputMultiplexer.addProcessor(this);
        }
    }

    public void update(float dt) {
        rect.setCenter(body.position.x, body.position.y);

        if(dragged){
            body.position.x = Gdx.input.getX();
            body.position.y = Gdx.graphics.getHeight() - Gdx.input.getY();
            body.velocity.set(0,0);
        }
        rect.setPosition(body.position.x, body.position.y);
        setPosition(body.position.x - getWidth() / 2, body.position.y - getHeight() / 2);
        setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        this.setRotation(this.body.orient* MathUtils.radDeg);
    }

    private void DefinBox() {

        rect.setWidth(rect.getWidth()/2);
        rect.setHeight(rect.getHeight()/2);
        p = new Polygon(rect.getWidth() , rect.getHeight());
        this.body = world.add(p, (int) (rect.getX() + rect.getWidth()), (int) (rect.getY() + rect.height));
        body.setOrient(0);
        body.restitution = 1f;
        body.dynamicFriction = df;
        body.staticFriction = sf;

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (rect.contains(screenX, Gdx.graphics.getHeight() - screenY)&& !AngryBirds.isOneDarged) {
            dragged=true;
            AngryBirds.isOneDarged=true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("Up");
        dragged=false;
        AngryBirds.isOneDarged=false;
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (rect.contains(screenX, Gdx.graphics.getHeight() - screenY)&& !AngryBirds.isOneDarged) {
            dragged=true;
            AngryBirds.isOneDarged=true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
