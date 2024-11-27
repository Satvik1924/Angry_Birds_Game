package org.angry.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import org.angry.Model.Body;
import org.angry.Model.ControllerLogic;
import org.angry.Model.Handel;
import org.angry.Model.World;
import org.angry.Model.Vector;
import org.angry.controller.AngryBirds;

public class LargePig extends Sprite implements InputProcessor {
    public enum State {GOOD, BAD, VERYBAD, DAID}

    private World world;
    public Body pig2body;
    public Ellipse ellipse;
    private State currentState, prevState;
    public float health = 100;
    private boolean dragged = false;
    private Sound streach;

    public LargePig(World world, Ellipse ellipse) {
        super(new Texture("king_pig.png")); // Use king_pig.png texture
        currentState = State.GOOD;
        prevState = State.GOOD;
        this.world = world;
        this.ellipse = ellipse;

        DefinBox();

        // Set bounds and origin based on the ellipse size
        setBounds(0, 0, ellipse.width, ellipse.height);
        setOrigin(this.getWidth() / 2, this.getHeight() / 2);

        // Load sound
        streach = Gdx.audio.newSound(Gdx.files.internal("sounds/pig.mp3"));
        streach.setLooping(100, false);
    }

    public void update(float dt) {
        currentState = getState(); // Update state based on health

        if (dragged) {
            pig2body.position.x = Gdx.input.getX();
            pig2body.position.y = Gdx.graphics.getHeight() - Gdx.input.getY();
            pig2body.velocity.set(0, 0);
        }
        if (currentState != State.DAID) {
            setPosition(pig2body.position.x - this.getWidth() / 2, pig2body.position.y - this.getHeight() / 2);
            setRotation(pig2body.shape.body.orient * MathUtils.radDeg);
            ellipse.setPosition(pig2body.position.x, pig2body.position.y);

            // Reduce angular velocity smoothly
            if (pig2body.angularVelocity > 0) {
                pig2body.angularVelocity = Math.max(pig2body.angularVelocity - Gdx.graphics.getDeltaTime() * 0.4f, 0);
            } else if (pig2body.angularVelocity < 0) {
                pig2body.angularVelocity = Math.min(pig2body.angularVelocity + Gdx.graphics.getDeltaTime() * 0.4f, 0);
            }
        } else {
            // Handle destruction
            world.bodies.remove(pig2body);
            pig2body = null;

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    ControllerLogic.LargecircleArray.removeValue(LargePig.this, false);
                    streach.play();
                }
            }, 0.2f);
        }

        if (pig2body != null) {
            colle();
        }
    }

    public boolean isDestroyed() {
        return currentState == State.DAID;
    }

    private void DefinBox() {
        Circle c = new Circle(ellipse.width / 2, 1f);
        this.pig2body = world.add(c, ((int) ellipse.x), (int) ellipse.y);
        pig2body.setOrient(0);
        pig2body.restitution = 0.2f;
        pig2body.dynamicFriction = 0.2f;
        pig2body.staticFriction = 0.5f;
    }

    public static int getLargeCircleArrayLength() {
        return ControllerLogic.LargecircleArray.size;
    }

    void colle() {
        for (Handel m : world.contacts) {
            for (int i = 0; i < m.contactCount; i++) {
                Vector v = m.contacts[i];
                if (v.y > pig2body.position.y && (m.B == this.pig2body || m.A == this.pig2body)) {
                    this.health -= 10;
                }
            }
        }
    }

    public State getState() {
        prevState = currentState;
        if (health >= 75) {
            return currentState = State.GOOD;
        } else if (health >= 50) {
            return currentState = State.BAD;
        } else if (health > 0) {
            return currentState = State.VERYBAD;
        } else {
            return currentState = State.DAID;
        }
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
        if (pig2body == null) {
            return false;
        }
        com.badlogic.gdx.math.Circle cc = new com.badlogic.gdx.math.Circle(pig2body.position.x, pig2body.position.y, 25);
        if (cc.contains(screenX, Gdx.graphics.getHeight() - screenY) && !AngryBirds.isOneDarged) {
            dragged = true;
            AngryBirds.isOneDarged = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        dragged = false;
        AngryBirds.isOneDarged = false;
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pig2body == null) {
            return false;
        }
        com.badlogic.gdx.math.Circle c = new com.badlogic.gdx.math.Circle(pig2body.position.x, pig2body.position.y, 25);
        if (c.contains(screenX, Gdx.graphics.getHeight() - screenY) && !AngryBirds.isOneDarged) {
            dragged = true;
            AngryBirds.isOneDarged = true;
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
