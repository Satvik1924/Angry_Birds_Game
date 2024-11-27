package org.angry.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.angry.Model.World;

public class GameController implements Disposable {
    public Stage stage;
    private Viewport viewport;
    public Label pig, state;
    World world;

    public void setState(String text) {
        this.state.setText(text);
    }

    public GameController(World world, SpriteBatch sb) {

        viewport = new FitViewport(AngryBirds.V_WIDTH, AngryBirds.V_HIEGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Table table = new Table();

        table.setFillParent(true);

        state = new Label("Game State", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.top();
        table.add(pig).expandX().padTop(10).top();
        stage.addActor(table);

        state.setPosition(1200, 0);
        stage.addActor(state);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
