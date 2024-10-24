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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.angry.controller.AngryBirds;

public class LevelCompleted implements Screen {
    private Stage stage;
    private Skin skin;
    private TextButton mainMenuButton, levelsButton;
    private Table table;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture backMenu;
    private Sprite spriteBg;
    private Texture congratulationsImage; // New variable for the congratulations image
    private Viewport viewport;
    private AngryBirds game;

    public LevelCompleted(AngryBirds game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();

        // Load background image
        backMenu = new Texture("angrybirds/backmenu.jpg");
        spriteBg = new Sprite(backMenu);
        spriteBg.setPosition(0, 0);
        spriteBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(stage);

        // Load button skin and font
        skin = new Skin();
        font = new BitmapFont(Gdx.files.internal("font/w.fnt"), false); // Use same font as paused screen

        // Load button texture
        Texture buttonTexture = new Texture(Gdx.files.internal("blue_button.png"));
        TextureRegionDrawable buttonUpDrawable = new TextureRegionDrawable(buttonTexture);
        TextureRegionDrawable buttonDownDrawable = new TextureRegionDrawable(buttonTexture);

        // Button style
        TextButton.TextButtonStyle ts = new TextButton.TextButtonStyle();
        ts.up = buttonUpDrawable;
        ts.down = buttonDownDrawable;
        ts.font = font;

        // Create buttons
        mainMenuButton = new TextButton("Main Menu", ts);
        mainMenuButton.pad(10, 100, 10, 100);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
            }
        });

        levelsButton = new TextButton("Levels", ts);
        levelsButton.pad(10, 100, 10, 100);
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelsScreen(game));
            }
        });

        // Load the congratulations image
        congratulationsImage = new Texture(Gdx.files.internal("congratulations.png"));

        // Arrange buttons in the table
        table = new Table();
        table.setFillParent(true);
        table.center();

        // Create an Image for the congratulations
        Image congratulationsImg = new Image(congratulationsImage);
        table.add(congratulationsImg).padBottom(20).row(); // Add the image above the buttons
        table.add(mainMenuButton).space(20).row();
        table.add(levelsButton).space(20);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        spriteBg.draw(batch);
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
        stage.dispose();
        font.dispose();
        skin.dispose();
        backMenu.dispose();
        congratulationsImage.dispose(); // Dispose the congratulations image
    }
}
