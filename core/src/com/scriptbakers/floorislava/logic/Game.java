package com.scriptbakers.floorislava.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.scriptbakers.floorislava.Constants;
import com.scriptbakers.floorislava.logic.gameentities.Lava;
import com.scriptbakers.floorislava.logic.gameentities.furniture.Furniture;
import com.scriptbakers.floorislava.logic.gameentities.Player;

import java.util.ArrayList;

import static com.scriptbakers.floorislava.Constants.*;
import static com.scriptbakers.floorislava.Constants.GameState.*;

/**
 * Created by bernardo on 04-11-2016.
 */

public class Game {
    public final World world;
    public final Player player;
    private ArrayList<Lava> lavaPatches;


    private int noUpdates;
    private FurnitureSpawner furnitureSpawner;
    private GameState gameState;
    private ArrayList<Furniture> furnitures;
    public Game() {
        world = new World(Constants.INITIAL_GRAVITY, true);
        player = new Player(world, PLAYER_INITIAL_X, PLAYER_INITIAL_Y, PLAYER_WIDTH, PLAYER_HEIGHT);
        furnitureSpawner = new FurnitureSpawner(world);
        lavaPatches = new ArrayList<Lava>();
        furnitures =  new ArrayList<Furniture>();

        noUpdates = 0;
        gameState = PAUSED;

        world.setContactListener(new GameContactListener());
        createWalls();
    }

    private void createWalls() {
        //Left wall creation
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);
        body.setGravityScale(0);

        EdgeShape shape = new EdgeShape();
        shape.set(0, 0, 0, WORLD_HEIGHT);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = -1;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_WALL;
        fixtureDef.filter.maskBits = MASK_WALL;
        body.createFixture(fixtureDef);

        //Right wall creation
        bodyDef = new BodyDef();
        bodyDef.position.set(WORLD_WIDTH+1, 0);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        body = world.createBody(bodyDef);
        body.setGravityScale(0);

        shape = new EdgeShape();
        shape.set(0, 0, 0, WORLD_HEIGHT);

        fixtureDef = new FixtureDef();
        fixtureDef.density = -1;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY_WALL;
        fixtureDef.filter.maskBits = MASK_WALL;
        body.createFixture(fixtureDef);
    }

    public void update(float delta) {
        if(gameState != RUNNING)
            return;

        world.step(delta, 6, 2);
        player.update(delta);

        if(!player.isAlive())
            gameState=OVER;

        noUpdates++;

        for (int i = furnitures.size() - 1; i >= 0; i--) {
            Furniture furniture = furnitures.get(i);
            if(furniture.getPosition().y - furniture.getDimensions().y< 0)
                furnitures.remove(i);
        }

        for (int i = lavaPatches.size() - 1; i >= 0; i--) {
            Lava lava = lavaPatches.get(i);
            if (lava.getPosition().y + lava.getLength() / 2 < 0)
                lavaPatches.remove(i);
        }

        if(noUpdates % (60/LAVA_GENERATION_PER_SECOND) == 0) {
            lavaPatches.add(new Lava(world, (float) Math.random() * LAVA_PATCH_MAX_LENGTH));

            for(int i = 0; i < Math.random()*3+1; i++) {
                furnitures.add(furnitureSpawner.generateObstacle(lavaPatches.get(lavaPatches.size()-1).getPosition().y));
            }
        }
    }

    public void run() {
        gameState = RUNNING;
    }

    public void pause() {
        gameState = PAUSED;
    }

    public GameState getGameState(){
        return gameState;
    }

    public ArrayList<Furniture> getFurnitures(){
        return this.furnitures;
    }

    public Vector2 getPlayerPosition() {
        return player.getPosition();
    }

    public ArrayList<Lava> getLavaPatches() {
        return lavaPatches;
    }
}
