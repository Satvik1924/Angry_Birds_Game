package org.angry.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.angry.view.LevelsScreen;
import org.angry.view.MainMenu;
import org.angry.view.EasyLevel;
import org.angry.view.splash;

public class AngryBirds extends Game {

    public static boolean isOneDarged = false;
    public final static int V_WIDTH = 1280;
    public final static int V_HIEGHT = 560;
    public static MainMenu main = null;
    public static splash splash= null;
    public static EasyLevel play = null;
    public static LevelsScreen levelScreen = null;

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        main = new MainMenu(this);
        splash = new splash(this);
        play = new EasyLevel(this);
        levelScreen = new LevelsScreen(this);
        setScreen(splash);
    }

    @Override
    public void render() {
        super.render();
    }
}
