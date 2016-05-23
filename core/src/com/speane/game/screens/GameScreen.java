package com.speane.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.speane.game.TankGame;
import com.speane.game.entities.Bullet;
import com.speane.game.entities.Tank;
import com.speane.game.entities.moving.Direction;
import com.speane.game.entities.network.transfers.CreatePlayer;
import com.speane.game.entities.network.transfers.LevelUp;
import com.speane.game.entities.network.transfers.SendMessage;
import com.speane.game.entities.network.userinfo.UserInfo;
import com.speane.game.help.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.speane.game.help.Config.DESKTOP_SCREEN_HEIGHT;
import static com.speane.game.help.Config.DESKTOP_SCREEN_WIDTH;
import static com.speane.game.help.TextureManager.TANK_TEXTURE;

/**
 * Created by Speane on 08.03.2016.
 */
public class GameScreen extends ScreenAdapter {
    private Music backgroundMusic;

    private boolean gameOver = false;
    private int score;
    private int nextLevelScore;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Tank player;
    private SpriteBatch batch;
    private Renderer renderer;
    private Map<Integer, Tank> enemies;
    private NetworkManager networkManager;
    private InputHandler inputHandler;
    private CollisionDetector collisionDetector;

    private HashMap<Integer, String> playerNames;
    private TankGame game;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private float levelWidth;
    private float levelHeight;
    private UserInfo userInfo;

    private Queue<String> chatMessages;

    private Stage stage;
    private TextField chatTextField;
    private TextButton sendButton;

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public Tank getPlayer() {
        return player;
    }

    public Map<Integer, Tank> getEnemies() {
        return enemies;
    }

    public GameScreen(TankGame game, UserInfo userInfo) {
        this.game = game;
        this.userInfo = userInfo;
        playerNames = new HashMap<>();
        chatMessages = new Queue<>();
    }

    public void addScore(int deltaScore) {
        score += deltaScore;
        nextLevelScore += deltaScore;
        if (nextLevelScore >= Config.LEVEL_UP_SCORE) {
            int levelsToUp = nextLevelScore / Config.LEVEL_UP_SCORE;
            LevelUp levelUp = new LevelUp();
            levelUp.healthPoints = levelsToUp * Config.HEALTH_POINTS_FOR_LEVEL_UP;
            levelUp.level = levelsToUp;
            player.levelUp(levelsToUp);
            player.addHealthPoints(Config.HEALTH_POINTS_FOR_LEVEL_UP);
            networkManager.sendEvent(levelUp);
            nextLevelScore = nextLevelScore % Config.LEVEL_UP_SCORE;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {

        tiledMap = game.getAssetManager().get("tiledmaps/tank_battles.tmx");
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)tiledMap.getLayers().get("background");
        levelWidth = tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();
        levelHeight = tiledMapTileLayer.getHeight() * tiledMapTileLayer.getTileHeight();
        loadResources();
        initEntities();
        initNetwork();
        initCamera();



        backgroundMusic = game.getAssetManager().get("sound/background.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.play();


        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        chatTextField = new TextField("", skin);
        chatTextField.setSize(200, 30);
        stage.addActor(chatTextField);

        sendButton = new TextButton("Send", skin);
        sendButton.setSize(50, 30);
        sendButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                stage.unfocusAll();
                String message = chatTextField.getText();
                chatMessages.addFirst(userInfo.name + ": " + message);
                if (chatMessages.size > 5) {
                    chatMessages.removeLast();
                }
                SendMessage sendMessage = new SendMessage();
                sendMessage.message = message;
                networkManager.sendEvent(sendMessage);
                chatTextField.setText("");
            }
        });
        chatTextField.setPosition(camera.position.x + DESKTOP_SCREEN_WIDTH / 2 - 250,
                camera.position.y - DESKTOP_SCREEN_HEIGHT / 2 + 100, Align.bottomLeft);
        sendButton.setPosition(camera.position.x + DESKTOP_SCREEN_WIDTH / 2 - 50,
                camera.position.y - DESKTOP_SCREEN_HEIGHT / 2 + 100, Align.bottomLeft);
        stage.addActor(sendButton);
        //
    }

    private void initCamera() {
        camera = new OrthographicCamera();

        batch = new SpriteBatch();
        renderer = new Renderer(batch);

        CreatePlayer createPlayer = new CreatePlayer();
        createPlayer.x = player.getX();
        createPlayer.y = player.getY();
        createPlayer.healthPoints = player.getHealthPoints();
        createPlayer.level = player.getLevel();
        createPlayer.rotation = player.getRotation();
        createPlayer.name = userInfo.name;
        networkManager.sendEvent(createPlayer);
        /*MoveTank moveTank = new MoveTank();
        moveTank.rotation = player.getRotation();
        moveTank.x = player.getX();
        moveTank.y = player.getY();
        networkManager.move(moveTank);*/
        inputHandler = new InputHandler(player, networkManager, tiledMap, this);
        collisionDetector = new CollisionDetector(this);

        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView(camera);
        /*float cameraX;
        float cameraY;
        if ((player.getX() + Config.DESKTOP_SCREEN_WIDTH / 2) > levelWidth) {
            cameraX = levelWidth - Config.DESKTOP_SCREEN_WIDTH / 2;
        }
        else if ((player.getX() - Config.DESKTOP_SCREEN_WIDTH / 2) < 0) {
            cameraX = Config.DESKTOP_SCREEN_WIDTH / 2;
        }
        else {
            cameraX = player.getX();
        }

        if ((player.getY() + Config.DESKTOP_SCREEN_HEIGHT / 2) > levelHeight) {
            cameraY = levelHeight - Config.DESKTOP_SCREEN_HEIGHT / 2;
        }
        else if ((player.getY() - Config.DESKTOP_SCREEN_HEIGHT / 2) < 0) {
            cameraY = Config.DESKTOP_SCREEN_HEIGHT / 2;
        }
        else {
            cameraY = player.getY();
        }*/
        updateCamera();

        viewport = new FitViewport(Config.DESKTOP_SCREEN_WIDTH, Config.DESKTOP_SCREEN_HEIGHT, camera);
        viewport.apply(true);

        /*camera.position.set(cameraX, cameraY, camera.position.z);
        camera.update();*/
    }

    private void initEntities() {
        initTanks();
    }

    private void initNetwork() {
        networkManager = new NetworkManager(this);
    }

    private void initTanks() {
        do {
            /*player = new Tank(TANK_TEXTURE, (MathUtils.random((int) levelWidth)),
                    MathUtils.random((int) levelHeight),
                    MathUtils.random(4) * 90);*/
            player = new Tank(TANK_TEXTURE, 1400, 1400, 90);
        } while (CollisionDetector.collidesWithLayer((TiledMapTileLayer) tiledMap.getLayers().get("indestructible"),
                player.getCollisionModel()) ||
                CollisionDetector.collidesWithLayer((TiledMapTileLayer) tiledMap.getLayers().get("impassable"),
                        player.getCollisionModel()));

        enemies = new HashMap<>();
    }

    private void loadResources() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        clearScreen();
        if (gameOver) {
            System.out.println("GAME OVER!!!");
            networkManager.close();
            game.setScreen(new GameOverScreen(game, userInfo, score));
        }
        if (stage.getKeyboardFocus() == null) {
            inputHandler.queryInput();
        }
        updateAllBullets();
        collisionDetector.checkCollisions();

        updateCamera();

        chatTextField.setPosition(camera.position.x + DESKTOP_SCREEN_WIDTH / 2 - 250,
                camera.position.y - DESKTOP_SCREEN_HEIGHT / 2 + 50, Align.bottomLeft);
        sendButton.setPosition(camera.position.x + DESKTOP_SCREEN_WIDTH / 2 - 50,
                camera.position.y - DESKTOP_SCREEN_HEIGHT / 2 + 50, Align.bottomLeft);

        stage.act();



        draw();
        stage.draw();
    }

    private void updateAllBullets() {
        updateTankBullets(player);
        for (Tank enemy : enemies.values()) {
            updateTankBullets(enemy);
        }
    }

    private void updateTankBullets(Tank tank) {
        Iterator<Bullet> iterator = tank.getBullets().iterator();
        Bullet bullet;
        while (iterator.hasNext()) {
            bullet = iterator.next();
            bullet.move(Direction.FORWARD);
            if (CollisionDetector.collidesWithLayer((TiledMapTileLayer) tiledMap.getLayers().get("indestructible"),
                    bullet.getCollisionModel())) {
                iterator.remove();
            }
        }
    }

    private void updateCamera() {
        float cameraX;
        float cameraY;
        if ((player.getX() + Config.DESKTOP_SCREEN_WIDTH / 2) > levelWidth) {
            cameraX = levelWidth - Config.DESKTOP_SCREEN_WIDTH / 2;
        }
        else if ((player.getX() - Config.DESKTOP_SCREEN_WIDTH / 2) < 0) {
            cameraX = Config.DESKTOP_SCREEN_WIDTH / 2;
        }
        else {
            cameraX = player.getX();
        }

        if ((player.getY() + Config.DESKTOP_SCREEN_HEIGHT / 2) > levelHeight) {
            cameraY = levelHeight - Config.DESKTOP_SCREEN_HEIGHT / 2;
        }
        else if ((player.getY() - Config.DESKTOP_SCREEN_HEIGHT / 2) < 0) {
            cameraY = Config.DESKTOP_SCREEN_HEIGHT / 2;
        }
        else {
            cameraY = player.getY();
        }

        /*viewport = new FitViewport(Config.DESKTOP_SCREEN_WIDTH, Config.DESKTOP_SCREEN_HEIGHT, camera);
        viewport.apply(true);*/

        camera.position.set(cameraX, cameraY, camera.position.z);
        camera.update();
        orthogonalTiledMapRenderer.setView(camera);
    }

    private boolean isOutOfScreen(float x, float y) {
        return ((x > Config.DESKTOP_SCREEN_WIDTH)
                || (x < 0)
                || (y > Config.DESKTOP_SCREEN_HEIGHT)
                || (y < 0));
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();

        //clearScreen();
        batch.begin();

        drawEnemies();
        drawTank(player, userInfo.name);
        renderer.drawText("Health: " + player.getHealthPoints(),
                (int) (camera.position.x - Config.DESKTOP_SCREEN_WIDTH / 2),
                (int) (camera.position.y + Config.DESKTOP_SCREEN_HEIGHT / 2));
        renderer.drawText("Score: " + score,
                (int) (camera.position.x - Config.DESKTOP_SCREEN_WIDTH / 2),
                (int) (camera.position.y + Config.DESKTOP_SCREEN_HEIGHT / 2 - 20));

        for (int i = 0; i < chatMessages.size; i++) {
            renderer.drawText(chatMessages.get(i), camera.position.x + Config.DESKTOP_SCREEN_WIDTH / 2 - 200,
                    camera.position.y - DESKTOP_SCREEN_HEIGHT / 2 + 100 + 20 * i);
        }
        batch.end();
    }

    private void drawTank(Tank tank, String playerName) {
        renderer.draw(tank);
        renderer.drawText("[" + tank.getLevel() + " lvl] " + tank.getHealthPoints() + "hp",
                tank.getX() - 10,
                (int) (tank.getY() + tank.getCollisionModel().getHeight() + 30));
        renderer.drawText(playerName,
                tank.getX() - 10,
                (int) (tank.getY() + tank.getCollisionModel().getHeight() + 50));
        for (Bullet bullet : tank.getBullets()) {
            renderer.draw(bullet);
        }
    }

    private void drawEnemies() {
        for (Integer key : enemies.keySet()) {
            drawTank(enemies.get(key), playerNames.get(key));
        }
        /*for (Tank enemy : enemies.values()) {
            drawTank(enemy, ENEMY_TANK_TEXTURE, playerNames.get(key));
        }*/
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public int getNextLevelScore() {
        return nextLevelScore;
    }

    public void setNextLevelScore(int nextLevelScore) {
        this.nextLevelScore = nextLevelScore;
    }

    public HashMap getPlayerNames() {
        return playerNames;
    }

    public Queue<String> getChatMessages() {
        return chatMessages;
    }
}
