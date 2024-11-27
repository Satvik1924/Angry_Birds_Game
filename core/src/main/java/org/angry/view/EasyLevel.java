package org.angry.view;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.angry.controller.AngryBirds;
import org.angry.Model.*;
import org.angry.controller.AngryPhysics;
import org.angry.controller.GameController;

import java.io.*;
import java.util.concurrent.ExecutorService;

public class EasyLevel extends InputAdapter implements Screen, ILevelScreen, Serializable {
    private static final long serialVersionUID = 1L; // Version ID for serialization
    private String level;
    private int score;
    private boolean levelCompleted;

    // Transient fields (not serializable)
    public static OrthographicCamera gameCam;
    private transient Viewport gamePort;
    private transient AngryBirds game;
    private transient GameController gameController;
    private transient TmxMapLoader mapLoader;
    private transient TiledMap map;
    private transient OrthogonalTiledMapRenderer maprenderer;
    private transient World world;
    public transient Bird bird;
    private transient AngryPhysics physics;
    private transient Stage stage;
    private transient Texture slingShotPart;
    private transient ShapeRenderer shapeRenderer;
    private transient AngryActor angryActor;
    public transient static InputMultiplexer inputMultiplexer;
    public transient static TextureAtlas atlas;
    private transient Sound music;
    private transient Animation<TextureRegion> record;
    private transient Sprite rSprite;
    private transient ExecutorService executor;
    private transient BitmapFont font;

    // Other fields
    private boolean isPaused = false;
    public boolean paused = false;
    private float stateTime = 0;
    private boolean gameOver = false;
    private int chances = 4;

    public EasyLevel(AngryBirds game) {
        this.game = game;
        this.level = "Easy"; // Default level name
        resetGame();
        initializeTransientFields();
    }

    private void initializeTransientFields() {
        inputMultiplexer = new InputMultiplexer();
        shapeRenderer = new ShapeRenderer();
        atlas = new TextureAtlas("ui/angr_birds.pack");
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(AngryBirds.V_WIDTH, AngryBirds.V_HIEGHT, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("medium_map.tmx");
        maprenderer = new OrthogonalTiledMapRenderer(map, 1);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(ImpulseMath.DT, 60);
        bird = new Bird(world, 0);
        gameController = new GameController(world, game.batch);
        physics = new AngryPhysics(bird);
        angryActor = new AngryActor(this.game, this.physics, this.bird);
        stage = new Stage();
        stage.addActor(angryActor);
        slingShotPart = new Texture("angrybirds/slingpart.png");
        font = new BitmapFont(); // Default font
        font.setColor(0, 0, 0, 1); // Set font color (black here)
        font.getData().setScale(2); // Adjust font size
    }

    private void resetGame() {
        score = 0;       // Reset score to 0
        chances = 4;     // Reset chances to 4
        gameOver = false; // Reset the game over flag
        levelCompleted = false; // Reset level completion status
    }

    public void saveState(String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(this.level);
            out.writeInt(this.score);
            out.writeInt(this.chances);
            out.writeBoolean(this.gameOver);
            out.writeInt(ControllerLogic.circleArray.size); // Number of small pigs
            out.writeInt(ControllerLogic.MediumcircleArray.size); // Number of medium pigs
            out.writeInt(ControllerLogic.LargecircleArray.size); // Number of large pigs
            Gdx.app.log("EasyLevel", "Game state saved successfully.");
        } catch (IOException e) {
            Gdx.app.log("EasyLevel", "Failed to save game state: " + e.getMessage());
        }
    }

    public static EasyLevel loadState(String fileName, AngryBirds game) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            String level = (String) in.readObject();
            int score = in.readInt();
            int chances = in.readInt();
            boolean gameOver = in.readBoolean();
            // Restore other relevant fields

            EasyLevel loadedLevel = new EasyLevel(game);
            loadedLevel.level = level;
            loadedLevel.score = score;
            loadedLevel.chances = chances;
            loadedLevel.gameOver = gameOver;
            Gdx.app.log("EasyLevel", "Game state loaded successfully.");
            return loadedLevel;
        } catch (IOException | ClassNotFoundException e) {
            Gdx.app.log("EasyLevel", "Failed to load game state: " + e.getMessage());
            return null;
        }
    }


    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    @Override
    public void show() {
        inputMultiplexer.addProcessor(gameController.stage);
        inputMultiplexer.addProcessor(new org.angry.controller.InputProcessor(physics));
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);
        new B2WorldCreator(world, map); // Generate floor/obstacles from map

        ControllerLogic.boxArray.clear();
        ControllerLogic.IceboxArray.clear();
        ControllerLogic.StoneboxArray.clear();
        ControllerLogic.circleArray.clear();
        ControllerLogic.LargecircleArray.clear();
        ControllerLogic.MediumcircleArray.clear();

        initializeHardcodedObjects();

        Music s = Gdx.audio.newMusic(Gdx.files.internal("sounds/game.wav")); // Medium level music
        s.setLooping(true);
        s.setVolume(10);
        s.play();

        if (ControllerLogic.ISRERUN) {
            bird.b2body.position.set(ControllerLogic.POS.x, ControllerLogic.POS.y);
            bird.b2body.velocity.set(ControllerLogic.vel.x, ControllerLogic.vel.y);
            bird.b2body.shape.initialize();
        }
    }

    private void initializeHardcodedObjects() {
        initializeBox();
        ControllerLogic.circleArray.add(new Pig(world, new Ellipse(725.00f, 250.00f, 39.86f, 32.99f)));
        ControllerLogic.LargecircleArray.add(new LargePig(world, new Ellipse(670.00f, 60.00f, 39.86f, 32.99f))); // X = 650
        ControllerLogic.MediumcircleArray.add(new MediumPig(world, new Ellipse(770.00f, 60.00f, 39.86f, 32.99f))); // X = 770
    }

    private void initializeBox() {
        ControllerLogic.IceboxArray.add(new Ice_Box(world, new Rectangle(600.00F, 60.00F, 30.67F, 160.00F), 1f, 1f));
        ControllerLogic.boxArray.add(new Box(world, new Rectangle(700.00F, 60.00F, 46.67F, 149.33F), 1f, 1f));
        ControllerLogic.StoneboxArray.add(new Stone_Box(world, new Rectangle(820.00F, 60.00F, 30.67F, 160.33F), 1f, 1f));
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
        // Handle any specific logic for pausing/resuming, e.g., stopping animations or physics
        if (paused) {
            Gdx.app.log("EasyLevel", "Game Paused");
        } else {
            Gdx.app.log("EasyLevel", "Game Resumed");
        }
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a background color (white here)
        Gdx.gl.glClearColor(1, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set the view for the map renderer
        maprenderer.setView(gameCam);
        maprenderer.render();

        if (gameOver) {
            displayGameOver(); // Display "Game Over" message
            return; // Stop further rendering/updates
        }

        // If the game is paused, stop updating game logic but keep rendering the scene
        if (isPaused) {
            game.batch.setProjectionMatrix(gameCam.combined);
            game.batch.begin();
            bird.draw(game.batch);  // Still render the bird and other entities while paused
            for (Box box : ControllerLogic.boxArray) {
                box.draw(game.batch);  // Still render the boxes
            }
            for (Ice_Box box : ControllerLogic.IceboxArray) {
                box.draw(game.batch);  // Still render the boxes
            }
            for (Stone_Box box : ControllerLogic.StoneboxArray) {
                box.draw(game.batch);  // Still render the boxes
            }
            for (Pig pig : ControllerLogic.circleArray) {
                pig.draw(game.batch);  // Still render the pigs
            }
            for (LargePig pig : ControllerLogic.LargecircleArray) {
                pig.draw(game.batch);  // Still render the pigs
            }
            for (MediumPig pig : ControllerLogic.MediumcircleArray) {
                pig.draw(game.batch);  // Still render the pigs
            }
            game.batch.end();
            return;  // Skip updating logic if paused
        }

        // Proceed with the game update if not paused
        update(delta);

        // Draw the game objects (birds, boxes, pigs, etc.)
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        drawChances();
        drawScore();
        bird.draw(game.batch);  // Draw the bird

        // Update and draw each box in the game
        for (Box box : ControllerLogic.boxArray) {
            box.update(delta);  // Update the box logic
            box.draw(game.batch);  // Draw the box
        }
        for (Ice_Box box : ControllerLogic.IceboxArray) {
            box.update(delta);  // Update the box logic
            box.draw(game.batch);  // Draw the box
        }
        for (Stone_Box box : ControllerLogic.StoneboxArray) {
            box.update(delta);  // Update the box logic
            box.draw(game.batch);  // Draw the box
        }

        // Update and draw each pig in the game
        for (int i = 0; i < ControllerLogic.circleArray.size; i++) {
            Pig pig = ControllerLogic.circleArray.get(i);
            pig.update(delta);

            if (pig.isDestroyed()) { // Assume Pig has an `isDestroyed` method or equivalent
                ControllerLogic.circleArray.removeIndex(i); // Remove pig from the array
                score += 100; // Increase score by 100
                i--; // Adjust index since we've removed an item
            } else {
                pig.draw(game.batch); // Draw pig if it's still active
            }
        }

        for (int i = 0; i < ControllerLogic.MediumcircleArray.size; i++) {
            MediumPig pig = ControllerLogic.MediumcircleArray.get(i);
            pig.update(delta);

            if (pig.isDestroyed()) { // Assume Pig has an `isDestroyed` method or equivalent
                ControllerLogic.MediumcircleArray.removeIndex(i); // Remove pig from the array
                score += 50; // Increase score by 100
                i--; // Adjust index since we've removed an item
            } else {
                pig.draw(game.batch); // Draw pig if it's still active
            }
        }

        for (int i = 0; i < ControllerLogic.LargecircleArray.size; i++) {
            LargePig pig = ControllerLogic.LargecircleArray.get(i);
            pig.update(delta);

            if (pig.isDestroyed()) { // Assume Pig has an `isDestroyed` method or equivalent
                ControllerLogic.LargecircleArray.removeIndex(i); // Remove pig from the array
                score += 150; // Increase score by 100
                i--; // Adjust index since we've removed an item
            } else {
                pig.draw(game.batch); // Draw pig if it's still active
            }
        }

        game.batch.end();

        // Draw the slingshot part
        game.batch.setProjectionMatrix(gameController.stage.getCamera().combined);
        gameController.setState(bird.getState().toString());
        gameController.stage.draw();

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();

        game.batch.begin();
        game.batch.draw(slingShotPart, 130, 104);

        // Draw the recording indicator if it's active
        if (ControllerLogic.RECORDING) {
            game.batch.draw(record.getKeyFrame(stateTime, true), 0, 520);
        }
        game.batch.end();

        // Update the physics world
        world.step();
        stateTime += delta;

        // Check if all pigs are defeated to complete the level
        if (arePigsDefeated()) {
            game.setScreen(new LevelCompleted(game));  // Transition to the "Level Complete" screen
        }
    }

    private void drawChances() {
        font.draw(game.batch, "Chances: " + (chances - 1), 10, 470); // Display chances in the top-left corner
    }

    private void drawScore() {
        font.draw(game.batch, "Score: " + score, 10, 440); // Display score at the top-left
    }

    private void displayGameOver() {
        game.batch.begin();
        font.draw(game.batch, "Game Over! Press ESC to return to the menu.", 150, 250);
        game.batch.end();
    }

    public void update(float dt) {
        bird.update(dt);
        if (!ControllerLogic.isBird) {
            ControllerLogic.isBird = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    newBird();
                }

                private void newBird() {
                    Timer.instance().clear();
                    world.bodies.remove(bird.b2body);
                    bird = new Bird(world, 0);
                    physics.setBird(bird);
                    angryActor.setBird(bird);
                    bird.b2body.position.set(150, 150); // Reset to slingshot position
                    bird.b2body.velocity.set(0, 0);
                    ControllerLogic.RECORDING = false;
                    if (ControllerLogic.ISRERUN) {
                        ControllerLogic.ISRERUN = false;
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
                    }
                }
            }, 3);
        }
        gameCam.update();
    }

    private boolean arePigsDefeated() {
        if(Pig.getCircleArrayLength() != 0){
            return false;
        }
        if(LargePig.getLargeCircleArrayLength() != 0){
            return false;
        }
        if(MediumPig.getMediumCircleArrayLength() != 0){
            return false;
        }
        return true;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        gameCam.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        maprenderer.dispose();
        map.dispose();
        gameController.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            resetGame();
            ControllerLogic.boxArray.clear();
            ControllerLogic.IceboxArray.clear();
            ControllerLogic.StoneboxArray.clear();
            ControllerLogic.LargecircleArray.clear();
            ControllerLogic.MediumcircleArray.clear();
            ControllerLogic.circleArray.clear();
            ImpulseMath.DT = 1.0f / 20.0f;
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MediumLevel(game));
        }

        if (keycode == Input.Keys.ESCAPE) {
            ControllerLogic.boxArray.clear();
            ControllerLogic.IceboxArray.clear();
            ControllerLogic.StoneboxArray.clear();
            ControllerLogic.circleArray.clear();
            ControllerLogic.LargecircleArray.clear();
            ControllerLogic.MediumcircleArray.clear();
            ImpulseMath.DT = 1.0f / 20.0f;
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
        }

        if (keycode == Input.Keys.R) {
            ControllerLogic.RECORDING = true;
        }

        if (keycode == Input.Keys.P) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new Paused(game, this));
        }

        if (keycode == Input.Keys.S) { // Save game state
            EasyLevel currentLevel = this;
            currentLevel.saveState("game_state_easy.ser");
            Gdx.app.log("EasyLevel", "Game state saved.");
        }
        else if (keycode == Input.Keys.L) { // Load game state
            EasyLevel loadedLevel = EasyLevel.loadState("game_state_easy.ser", game);
            if (loadedLevel != null) {
                game.setScreen(loadedLevel); // Switch to the loaded level
                Gdx.app.log("EasyLevel", "Game state loaded.");
            } else {
                Gdx.app.log("EasyLevel", "No saved state found or failed to load.");
            }
        }

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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // When the bird is launched, enable gravity
        world.setLaunched(true);
        chances--;

        if (chances <= 0) {
            gameOver = true; // Set game over flag when chances are exhausted
        } else {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    newBird();
                }
            }, 3); // Provide the player with a new bird after 3 seconds
        }
        return true;
    }

    private void newBird() {
        Timer.instance().clear();
        world.bodies.remove(bird.b2body);
        bird = new Bird(world, 0);
        physics.setBird(bird);
        angryActor.setBird(bird);
        ControllerLogic.RECORDING = false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
}
