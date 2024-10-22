package org.angry.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.angry.controller.AngryBirds;

public class LevelsScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private ImageButton backButton;
    private Table table;
    private BitmapFont white, black;
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Texture backMenu; // Background texture for LevelScreen
    private Sprite spriteBg;
    private Viewport viewport;
    private AngryBirds game;

    public LevelsScreen(AngryBirds game) {
        this.game = game;
    }

    @Override
    public void show() {
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport);
        batch = new SpriteBatch();

        // Load background texture
        backMenu = new Texture("angrybirds/backmenu.jpg"); // Use a suitable background image
        spriteBg = new Sprite(backMenu);
        spriteBg.setPosition(0, 0);
        spriteBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(stage);
        atlas = new TextureAtlas("ui/button.pack");
        white = new BitmapFont(Gdx.files.internal("font/w.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("font/b.fnt"), false);
        skin = new Skin(atlas);

        table = new Table(skin);
        table.setFillParent(true); // Ensures the table resizes with the stage
        table.pad(50); // Add padding around the entire table to avoid touching edges

        Drawable buttonDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("blue_button.png")));

        // Create level buttons (use TextButton as before)
        TextButton.TextButtonStyle ts = new TextButton.TextButtonStyle();
        ts.up = buttonDrawable;
        ts.down = buttonDrawable;
        ts.pressedOffsetX = 1;
        ts.pressedOffsetY = -1;
        ts.font = white;

        TextButton easyButton = new TextButton("Easy", ts);
        TextButton mediumButton = new TextButton("Medium", ts);
        TextButton hardButton = new TextButton("Hard", ts);

        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Easy level selected");
                ((Game) Gdx.app.getApplicationListener()).setScreen(new EasyLevel(game));
            }
        });

        mediumButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Medium level selected");
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MediumLevel(game));
            }
        });

        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Hard level selected");
                ((Game) Gdx.app.getApplicationListener()).setScreen(new HardLevel(game));
            }
        });

        // Arrange buttons in the table
        float buttonWidth = Gdx.graphics.getWidth() * 0.25f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.3f;
        float padding = Gdx.graphics.getWidth() * 0.03f; // Adjust padding for even spacing

        table.add(easyButton).width(buttonWidth).height(buttonHeight).pad(padding);
        table.add(mediumButton).width(buttonWidth).height(buttonHeight).pad(padding);
        table.add(hardButton).width(buttonWidth).height(buttonHeight).pad(padding);
        table.row(); // Move to the next row

        stage.addActor(table);

        // Load the back button image
        Texture backButtonTexture = new Texture(Gdx.files.internal("back_button.png")); // Your back button image
        Drawable backButtonDrawable = new TextureRegionDrawable(backButtonTexture);
        backButton = new ImageButton(backButtonDrawable);

        // Add back button to the bottom-left corner
        Table backButtonTable = new Table();
        backButtonTable.setFillParent(true);
        backButtonTable.bottom().left();
        backButtonTable.add(backButton).pad(20); // Adding padding for positioning
        stage.addActor(backButtonTable);

        // Set click listener for the back button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        spriteBg.draw(batch); // Draw background
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.setScreenSize(width, height);
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
        atlas.dispose();
        white.dispose();
        black.dispose();
        skin.dispose();
    }
}
