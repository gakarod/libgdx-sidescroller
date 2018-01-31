package com.pointless;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.Random;

import static com.pointless.GameScreen.State.PAUSE;
import static com.pointless.GameScreen.State.RESUME;

/**
 * Created by vaibh on 7/4/2017.
 */

public class GameScreen extends ScreenAdapter  {


    public  static float WORLD_WIDTH = 480;
    public  static float WORLD_HEIGHT = 320;
    private static final float GAP_BETWEEN_FLOWERS = 200F;
    public static final float MIN_ASTEROID_SPAWN_TIME = 0.4f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.8f;

    Random random;
    Random rand = new Random();

    private ShapeRenderer shapeRenderer;
    private StretchViewport viewPort;
    public Camera camera;
    private SpriteBatch sb;
    private Flappy flappy;
    Sound sound ;
    Sound blast ;

    float asteroidSpawnTimer;
    float asteroidlocation;
    float ylocation;
    float y2location;

    AdHandler handler ;
    boolean toggle = false  ;


    private int score = 0;
    private Array<Flower> flowers = new Array<Flower>();
    float shootTimer;
    float Timer;

    float health = 1;//0 = dead, 1 = full health
    float life = 3;
    public boolean flag = false;

    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;
    long startTime = System.currentTimeMillis();

    private Texture bg;
    private Texture flowerBottom;
    private Texture flappyTexture;
    private Texture blank;
    private Texture heart;
    Game gamenow;
    private AdHandler adhandler ;

    Vector3 touchPoint = new Vector3();
    Rectangle cBounds = new Rectangle(WORLD_WIDTH - 75, WORLD_HEIGHT-33 ,46,42);
    public static final float SHOOT_WAIT_TIME = 0.2f;
    public static final float WAIT_TIME = 1.0f;

    public ArrayList<Bullet> bullets;
    public ArrayList<Asteroid> asteroids;
    public ArrayList<Explosion> explosions;
    public ArrayList<Bomb> bombs;
    private Boss boss;
    private Sprite pause;

    public enum State
    {
        PAUSE,
        RUN,
        RESUME,
        STOPPED
    }

    private State state = State.RUN;

    ArrayList<Asteroid> asteroidsToRemove = new ArrayList<Asteroid>();
    ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
    ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
    ArrayList<Bomb> bombsToRemove = new ArrayList<Bomb>();

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewPort.update(width, height);
    }

    public GameScreen(Game game ,AdHandler adhandler ) {
        this.gamenow = game;
        this.adhandler = adhandler ;
    }

    @Override
    public void show() {
        super.show();
        bitmapFont = new BitmapFont();
        glyphLayout = new GlyphLayout();
        bullets = new ArrayList<Bullet>();
        asteroids = new ArrayList<Asteroid>();
        explosions = new ArrayList<Explosion>();
        bombs = new ArrayList<Bomb>();
        random = new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewPort = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        sb = new SpriteBatch();

        sound = Gdx.audio.newSound(Gdx.files.internal("boom.wav")) ;
        blast = Gdx.audio.newSound(Gdx.files.internal("big.wav")) ;
        bg = new Texture(Gdx.files.internal("bg.png"));
        flowerBottom = new Texture(Gdx.files.internal("meteor.png"));
        flappyTexture = new Texture(Gdx.files.internal("ship.png"));
        heart = new Texture(Gdx.files.internal("heart.png"));
        blank = new Texture("blank.png");
        flappy = new Flappy(flappyTexture);
        flappy.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        boss = new Boss(WORLD_HEIGHT/2);
    //    pause = new Sprite(new Texture("pause.png"));
     //   pause.setBounds(WORLD_WIDTH - 75, WORLD_HEIGHT-33 ,46,42);
        Gdx.input.setInputProcessor(new InputAdapter() {


        });
        //   TextureRegion myTextureRegion = new TextureRegion(pause);
        //   TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        //   ImageButton pause_btnDialog = new ImageButton(myTexRegionDrawable); //Set the button up

    }

    @Override
    public void pause() {
        super.pause();
        this.state = PAUSE;
    }

    @Override
    public void resume() {
        super.resume();
        this.state = RESUME;

    }

    @Override
    public void dispose() {
        super.dispose();
    }


    @Override
    public void render(float delta) {


                super.render(delta);
                update(delta, startTime);
                clearScreen();
                draw(startTime);
            //    drawDebug();



    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        flappy.drawDebug(shapeRenderer);

        for (Flower flower : flowers) {
            flower.drawDebug(shapeRenderer);
        }

        for (Asteroid asteroid : asteroids) {
            asteroid.drawDebug(shapeRenderer);
        }

     //   boss.drawDebug(shapeRenderer);
        shapeRenderer.rect(cBounds.getX(),cBounds.getY(),cBounds.getWidth(),cBounds.getHeight());
        shapeRenderer.end();
    }

    private void update(float delta, long timer) {

         if(toggle){
             updateBoss(delta);
             updateBombs(delta);
         }
        updateFlappy(delta);
        Bullethit();
        updateBullets(delta);
        updateFlowers(delta);
        buildAsteroids(delta);
        updateAsteroids(delta);
        updateExplosions(delta);
        updateScore();
        flag = false;

        if (checkForCollision())
            restart();
    }

    private void draw(long time) {
        long elapsedtime = System.currentTimeMillis() - time;
        sb.setProjectionMatrix(camera.projection);
        sb.setTransformMatrix(camera.view);
        sb.begin();
        sb.draw(bg, 0, 0);
        drawFlowers();
        drawBullets();

        drawAsteroids();
        drawExplosions();
        Health();
     //   sb.draw(pause,WORLD_WIDTH-75 , WORLD_HEIGHT-75);
        sb.setColor(Color.WHITE);
        flappy.draw(sb);
        if (elapsedtime / 1000 >= 15) {
            boss.render(sb);
            toggle = true ;
            drawBombs();
        }
        drawScore();
        sb.end();
    }


    private void updateFlappy(float delta) {
        flappy.update(delta);
        shootTimer += delta ;
        if (shootTimer >= SHOOT_WAIT_TIME) {
            shootTimer = 0;
            bullets.add(new Bullet(flappy.x, flappy.y));
        }

        for (int i = 0; i < 5; i++) {
            if (!Gdx.input.isTouched(i)) continue;

            touchPoint.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
            if(cBounds.contains(touchPoint.x,touchPoint.y)) {
              state = PAUSE ;
            }
            flappy.flyUp();

        }

        blockFlappeeLeavingTheWorld();

    }


    private void updateFlowers(float delta) {
        for (Flower flower : flowers) {
            flower.update(delta);
            if (flag) {
                flower.MAX_SPEED_PER_SECOND += 150;
            }
        }
        checkIfNewFlowerIsNeeded();
        removeFlowersIfPassed();

        if (checkForCollision()) {
            life-- ;
        }
    }

    private void updateBoss(float delta) {
        if (boss.remove) {
            next();
        }
        Timer += delta ;
        if (Timer >= WAIT_TIME) {
            Timer = 0;
            bombs.add(new Bomb(boss.x - boss.getWIDTH()/2, boss.y));
        }
        boss.update(delta);
    }

    private void updateBullets(float delta) {

        for (Bullet bullet : bullets) {
            bullet.update(delta);
            if (bullet.remove)
                bulletsToRemove.add(bullet);
        }
        bullets.removeAll(bulletsToRemove);
    }

    private void updateBombs(float delta){

        for (Bomb bomb : bombs) {
            bomb.update(delta);
            if (bomb.remove)
                bombsToRemove.add(bomb);
        }
        bombs.removeAll(bombsToRemove);
    }

    private void updateScore() {
        if (flowers.size > 0) {
            Flower flower = flowers.first();
            if (flower.getX() < flappy.getX() && !flower.isPointClaimed()) {
                flower.markPointClaimed();
                score++;
            }
        }
    }


    private void drawScore() {
        String scoreAsString = Integer.toString(score);
        glyphLayout.setText(bitmapFont, scoreAsString);
        bitmapFont.draw(sb, scoreAsString, (viewPort.getWorldWidth() - glyphLayout.width) / 2, (4 * viewPort.getWorldHeight() / 5) - glyphLayout.height / 2);
    }


    private void drawFlowers() {
        for (Flower flower : flowers) {
            flower.draw(sb);
        }
    }

    private void drawBullets() {
        for (Bullet bullet : bullets) {
            bullet.draw(sb);
        }
    }

    private void drawBombs() {
        for (Bomb bomb : bombs) {
            bomb.draw(sb);
        }
    }

    private void drawExplosions() {

        for (Explosion explosion : explosions) {
            explosion.render(sb);
        }
    }

    private void buildAsteroids(float delta) {
        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0) {
            ylocation = flappy.y + 50;
            y2location = flappy.y - 50;
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroidlocation = rand.nextFloat() * (ylocation - y2location) + y2location;
        //    asteroids.add(new Asteroid(random.nextInt((int) (GameScreen.WORLD_HEIGHT - 32))));
            asteroids.add(new Asteroid(asteroidlocation));
            asteroids.add(new Asteroid(flappy.y));
        }
    }

    private void drawAsteroids() {
        for (Asteroid asteroid : asteroids) {
            asteroid.render(sb);
        }
    }

    private void updateAsteroids(float delta) {

        for (Asteroid asteroid : asteroids) {
            if (flag) {
                asteroid.SPEED += 50;
            }
            asteroid.update(delta);
            if (asteroid.remove)
                asteroidsToRemove.add(asteroid);
        }
        asteroids.removeAll(asteroidsToRemove);
    }

    private void updateExplosions(float delta) {

        for (Explosion explosion : explosions) {
            explosion.update(delta);
            if (explosion.remove)
                explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);
    }


    private void restart() {

        flappy.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
        flowers.clear();

    }


    private void next() {
        toggle = false ;
        blast.play(0.75f);
        startTime = System.currentTimeMillis();
        boss.BossHealth = 310;
        boss.remove = false;
        flowers.clear();
        flag = true;

    }

    private boolean checkForCollision() {

        for (Flower flower : flowers) {
            if (flower.isFlappyColliding(flappy)) {
                return true;
            }
        }
        for (Asteroid asteroid : asteroids) {
            if (asteroid.isFlappyColliding(flappy)) {
                asteroidsToRemove.add(asteroid);
                health -= 0.1;
            }
        }
  if(toggle) {
      for (Bullet bullet : bullets) {
          if (Intersector.overlaps(bullet.BulletCircle, boss.BossCircle)) {
              bulletsToRemove.add(bullet);
              sound.play() ;
              explosions.add(new Explosion(boss.getX(), boss.getY()));
              boss.BossHealth -= 10;
          }
      }
      for (Bomb bomb : bombs) {
          if (bomb.isFlappyColliding(flappy)) {
              bombsToRemove.add(bomb);
              explosions.add(new Explosion(flappy.getX(), flappy.getY()));
              health -= 0.2 ;
          }
      }
  }
        return false;
    }

    private void Bullethit() {

        for (Bullet bullet : bullets) {
            for (Asteroid asteroid : asteroids) {
                for (Flower flower : flowers) {
                    if (Intersector.overlaps(flower.botCollisionCircle, bullet.BulletCircle)) {
                        bulletsToRemove.add(bullet);
                    }
                }
                if (Intersector.overlaps(asteroid.AsteroidCircle, bullet.BulletCircle)) {
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                    score += 1;
                }
            }

        }

    }

    private void Health() {
        if (life == 0)
            gamenow.setScreen(new GameOver(gamenow ,score , adhandler));
        else if (life == 3) {
            sb.draw(heart, 20, WORLD_HEIGHT - 60);
            sb.draw(heart, 60, WORLD_HEIGHT - 60);
            sb.draw(heart, 100, WORLD_HEIGHT - 60);
        } else if (life == 2) {
            sb.draw(heart, 20, WORLD_HEIGHT - 60);
            sb.draw(heart, 60, WORLD_HEIGHT - 60);
        } else if (life == 1) {
            sb.draw(heart, 20, WORLD_HEIGHT - 60);
        }

        if (health > 0.7f)
            sb.setColor(Color.GREEN);
        else if (health > 0.4f)
            sb.setColor(Color.ORANGE);
        else if (health > 0)
            sb.setColor(Color.RED);
        else {
            life--;
            health = 1;
        }

        sb.draw(blank, 0, 0, GameScreen.WORLD_WIDTH * health, 5);

    }

    private void blockFlappeeLeavingTheWorld() {
        flappy.setPosition(flappy.getX(), MathUtils.clamp(flappy.getY(), 0, WORLD_HEIGHT));
    }


    private void checkIfNewFlowerIsNeeded() {
        if (flowers.size == 0) {
            createNewFlower();
        } else {
            Flower flower = flowers.peek();
            if (flower.getX() < WORLD_WIDTH - GAP_BETWEEN_FLOWERS) {
                createNewFlower();
            }
        }
    }


    private void createNewFlower() {
        Flower newFlower = new Flower(flowerBottom);
        newFlower.setPosition(WORLD_WIDTH + Flower.WIDTH);
        flowers.add(newFlower);
    }


    private void removeFlowersIfPassed() {
        if (flowers.size > 0) {
            Flower firstFlower = flowers.first();
            if (firstFlower.getX() < -Flower.WIDTH) {
                flowers.removeValue(firstFlower, true);
            }
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}

