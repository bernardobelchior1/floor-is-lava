package com.scriptbakers.floorislava.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.scriptbakers.floorislava.FloorIsLava;
import com.scriptbakers.floorislava.logic.gameentities.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scriptbakers.floorislava.logic.gameentities.Player;
import com.scriptbakers.floorislava.Constants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scriptbakers.floorislava.hud.GameHud;


/**
 * Created by bernardo on 04-11-2016.
 */

public class GameScreen implements Screen {
    SpriteBatch batch;
    Game game;
    Box2DDebugRenderer debugRenderer;
    OrthographicCamera camera;
    FitViewport viewport;
    GameHud hud;

    public GameScreen(Game game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.hud = new GameHud(game, batch);

        debugRenderer = new Box2DDebugRenderer();

        camera = new OrthographicCamera(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        camera.position.set(Constants.WORLD_WIDTH/2, Constants.WORLD_HEIGHT/2, 0);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
        game.update(delta);

        debugRenderer.render(game.world, camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        camera = new OrthographicCamera(width, height);
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

    }
}