package org.angry.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.angry.controller.AngryBirds;
import org.angry.view.ILevelScreen;

public class Paused implements Screen {
    private Game game;
    private Screen previousScreen;
    private SpriteBatch batch;
    private Sprite spriteBg;
    private Stage stage;
    private Skin skin;
    private Texture buttonTexture;
    private TextButton resumeButton, returnButton, restartButton; // Added restart button
    private Viewport viewport;
    private Table table;

    public Paused(Game game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen; // Store the current level/game screen
        batch = new SpriteBatch();
        Texture backMenu = new Texture("angrybirds/backmenu.jpg"); // Load the background image
        spriteBg = new Sprite(backMenu);
        spriteBg.setPosition(0, 0); // Position it at (0,0)
        spriteBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Stretch to fill the screen

        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);

        // Load button skin and texture
        skin = new Skin();
        buttonTexture = new Texture(Gdx.files.internal("blue_button.png"));
        TextureRegionDrawable buttonUpDrawable = new TextureRegionDrawable(buttonTexture); // Normal state
        TextureRegionDrawable buttonDownDrawable = new TextureRegionDrawable(buttonTexture); // Pressed state

        // Button style
        TextButton.TextButtonStyle ts = new TextButton.TextButtonStyle();
        ts.up = buttonUpDrawable;
        ts.down = buttonDownDrawable;
        ts.font = new BitmapFont(Gdx.files.internal("font/w.fnt"), false); // Use same font as main menu

        // Resume button
        resumeButton = new TextButton("Resume", ts);
        resumeButton.pad(10, 100, 10, 100); // Padding for button size
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (previousScreen instanceof ILevelScreen) {
                    ((ILevelScreen) previousScreen).setPaused(false); // Unpause the level
                }
                game.setScreen(previousScreen); // Resume the previous screen
            }
        });

        // Return button
        returnButton = new TextButton("Return", ts);
        returnButton.pad(10, 100, 10, 100); // Padding for button size
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenu((AngryBirds) game)); // Exit game or return to main menu
            }
        });

        // Restart button
        restartButton = new TextButton("Restart", ts); // Initialize restart button
        restartButton.pad(10, 100, 10, 100); // Padding for button size
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(previousScreen); // Restart the previous screen
            }
        });

        // Table to arrange buttons
        table = new Table();
        table.setFillParent(true);  // Automatically fill the parent container (center it)
        table.center(); // Align the table to the center of the screen
        table.add(resumeButton).space(20).row(); // Add resume button and add space
        table.add(restartButton).space(20).row(); // Add restart button and add space
        table.add(returnButton).space(20).row(); // Add return button and add space

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage); // Enable input processing for the stage
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Set background to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        spriteBg.draw(batch); // Draw the background image
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        skin.dispose();
        buttonTexture.dispose();
        stage.dispose();
    }
}
