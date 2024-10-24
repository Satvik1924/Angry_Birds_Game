package org.angry.view;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.angry.controller.AngryBirds;

public class splash implements Screen {
    private Sprite splash;
    private SpriteBatch batch;
    private TweenManager tweenManager;
    private AngryBirds game;
    private BitmapFont font;
    private float loadingProgress = 0;
    private ShapeRenderer shapeRenderer; // For drawing the loading bar

    public splash(AngryBirds game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Texture splashTexture = new Texture(Gdx.files.internal("angrybirds/splash.png"));
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 0.5f).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));  // After splash, go to MainMenu
            }
        }).start(tweenManager);

        shapeRenderer = new ShapeRenderer(); // Initialize ShapeRenderer
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        // Simulating loading progress (increase by 20% per second)
        loadingProgress += delta * 0.2f;
        if (loadingProgress > 1) loadingProgress = 1;  // Ensure progress doesn't exceed 100%

        batch.begin();
        splash.draw(batch);  // Draw splash image

        // Draw the loading text
        font.draw(batch, "Loading: " + (int) (loadingProgress * 100) + "%", Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 4f - 30);
        batch.end();

        // Draw loading bar
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1); // White color for the loading bar
        shapeRenderer.rect(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f - 50,
            Gdx.graphics.getWidth() / 2f * loadingProgress, 20); // Loading bar dimensions
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
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
        batch.dispose();
        splash.getTexture().dispose();
        shapeRenderer.dispose(); // Dispose ShapeRenderer
    }
}
