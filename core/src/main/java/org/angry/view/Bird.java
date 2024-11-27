package org.angry.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import org.angry.Model.Body;
import org.angry.Model.Vector;
import org.angry.Model.World;
import org.angry.Model.ControllerLogic;

public class Bird extends Sprite {
    public enum State {FLAYNIG, STADNG, FALLING, ONEARTH, STOPED}
    public enum BirdType {RED, BLUE, BLACK}  // Different bird types

    public State currentState;
    public State previosState;
    private BirdType birdType;
    private static BirdType nextBirdType = BirdType.RED; // Track which bird type to spawn next

    public World world;
    public Body b2body;
    public float g;
    private TextureRegion textureRegion;
    private Sound birdMusic;
    private boolean startMusic;

    // Properties for different bird types
    private static final float[] BIRD_MASSES = {1f, 0.8f, 0.6f, 1.2f};  // Mass for RED, YELLOW, BLUE, BLACK
    private static final float[] BIRD_SIZES = {23f, 21f, 20f, 25f};     // Size for each bird type
    private static final float[] BIRD_RESTITUTIONS = {1f, 1.2f, 0.9f, 0.8f}; // Bounce factors

    public Bird(World world, float g) {
        super(getNextBirdTexture());
        this.birdType = nextBirdType;
        cycleNextBirdType(); // Prepare the next bird type

        currentState = State.STADNG;
        previosState = State.STADNG;
        this.world = world;
        this.g = g;
        DefinBird();

        textureRegion = new TextureRegion(getTexture());
        setBounds(0, 0, 50, 50);
        setRegion(textureRegion);

        this.b2body.invMass = 0;
        birdMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/flaying.mp3"));
        birdMusic.setLooping(15, false);

        startMusic = true;
    }

//    private static Texture getNextBirdTexture() {
//        String texturePath = switch (nextBirdType) {
//            case RED -> "bird2.png";
//            case BLUE -> "bird4.png";
//            case BLACK -> "angrybirds/angry.png";
//        };
//        return new Texture(Gdx.files.internal(texturePath));
//    }
    private static Texture getNextBirdTexture() {
        String texturePath;
        if (nextBirdType == BirdType.RED) {
            texturePath = "bird2.png";
        } else if (nextBirdType == BirdType.BLUE) {
            texturePath = "bird4.png";
        } else if (nextBirdType == BirdType.BLACK) {
            texturePath = "angrybirds/angry.png";
        } else {
            throw new IllegalStateException("Unexpected bird type: " + nextBirdType);
        }
        return new Texture(Gdx.files.internal(texturePath));
    }

    private static void cycleNextBirdType() {
//        nextBirdType = switch (nextBirdType) {
//            case RED -> BirdType.BLUE;
//            case BLUE -> BirdType.BLACK;
//            case BLACK -> BirdType.RED;
//        };
        if(nextBirdType == BirdType.RED){
            nextBirdType = BirdType.BLUE;
        }
        else if(nextBirdType == BirdType.BLUE){
            nextBirdType = BirdType.BLACK;
        }
        else if(nextBirdType == BirdType.BLACK){
            nextBirdType = BirdType.RED;
        }
    }

    public BirdType getBirdType() {
        return birdType;
    }

    public void update(float dt) {
        setPosition(b2body.position.x - getWidth() / 2, b2body.position.y - getHeight() / 2);
        if (!isStoped() && currentState != State.STADNG && startMusic) {
            birdMusic.play();
            startMusic = false;
        }
        if (isStoped()) {
            ControllerLogic.isBird = false;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    start();
                }

                private void start() {
                    startMusic = true;
                }
            }, 3);
        }
        b2body.angularVelocity = 0;
        setOrigin(this.getWidth() / 2, this.getHeight() / 2);
        setRotation(b2body.orient * 2);
    }

    public State getState() {
        if (isZero(b2body.velocity.x, b2body.velocity.y) && previosState == State.STADNG) {
            return currentState = State.STADNG;
        } else if (isZero(b2body.velocity.x, b2body.velocity.y) && previosState != State.STADNG) {
            return currentState = State.STOPED;
        } else if (b2body.velocity.x > 0 && b2body.velocity.y > 0 && previosState != State.FALLING && previosState != State.ONEARTH) {
            return currentState = State.FLAYNIG;
        } else if (b2body.velocity.x > 0 && b2body.velocity.y < 0 && previosState != State.ONEARTH) {
            return currentState = State.FALLING;
        } else {
            return previosState = currentState = State.ONEARTH;
        }
    }

    private boolean isStoped() {
        return b2body.velocity.x < 1 && b2body.velocity.x > -1 && this.getState() != State.STADNG;
    }

    public void DefinBird() {
        Circle c = new Circle(BIRD_SIZES[birdType.ordinal()], BIRD_MASSES[birdType.ordinal()]);
        this.b2body = world.add(c, 150, 150);
        b2body.setOrient(0);
        b2body.restitution = BIRD_RESTITUTIONS[birdType.ordinal()];
        b2body.dynamicFriction = 0.1f;
        b2body.staticFriction = 0.1f;
    }

    public boolean isZero(float x, float y) {
        return x == 0 && y == 0;
    }
}