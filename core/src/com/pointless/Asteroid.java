package com.pointless;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.RandomXS128;

import java.util.Random;

/**
 * Created by vaibh on 7/4/2017.
 */

public class Asteroid {

    public int SPEED = 250;
    public int WIDTH = 32;
    public int HEIGHT = 32;
    public static final int radius = 15;
    public  int TILE_WIDTH=32;
    public  int TILE_HEIGHT=32 ;
    private static Texture texture;
    public final Circle AsteroidCircle;
    private Animation animation;
    private float animationTimer = 0;
    public static final float FRAME_DURATION = .25F;
    public int flag = 0;
    int max = 4 ;
    int min = 0 ;
    int type = 0 ;

    float x, y;

    public boolean remove = false;

    public Asteroid (float y ) {

        type = min + (int)(Math.random() * max);
            if(type == 1)
                texture = new Texture("asteroid.png");

            else if(type == 2) {
                TILE_HEIGHT = 16;
                TILE_WIDTH = 16;
                WIDTH = 16 ;
                HEIGHT = 16 ;
                texture = new Texture("asteroid2.png");
            }

            else if(type == 3) {
                TILE_HEIGHT = 32;
                TILE_WIDTH = 16;
                WIDTH = 16 ;
                HEIGHT = 32 ;
                texture = new Texture("asteroid3.png");
            }

            else  {
            texture = new Texture("asteroid.png");
        }


        TextureRegion[][] textures = new TextureRegion(texture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, textures[0][0],textures[0][1]);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        this.x = GameScreen.WORLD_WIDTH;
        this.y = y;

        AsteroidCircle = new Circle(x , y , radius);
    }

    private void check() {
        if (y >= GameScreen.WORLD_HEIGHT-HEIGHT) {
            y = GameScreen.WORLD_HEIGHT-HEIGHT ;
            flag = 0;
        }
        else if ( y <= 0 + HEIGHT/4 ) {
            y = 0+HEIGHT/4;
            flag = 1;
        }
    }

    public void update (float deltaTime) {
        animationTimer += deltaTime;
        x -= SPEED * deltaTime;

        if(type == 2) {
            if( GameScreen.WORLD_WIDTH >= x && x >= 3*(GameScreen.WORLD_WIDTH)/4) {
                y -= SPEED * deltaTime;
            }
            else if( (3*GameScreen.WORLD_WIDTH)/4 >= x && x >= GameScreen.WORLD_WIDTH/2){
                y += SPEED * deltaTime;
            }
            else if( GameScreen.WORLD_WIDTH/2 >= x && x >= GameScreen.WORLD_WIDTH/4) {
                y -= SPEED * deltaTime;
            }
            else if( (GameScreen.WORLD_WIDTH)/4 >= x && x >= -GameScreen.WORLD_WIDTH){
                y += SPEED * deltaTime;
            }
        }

        if(type == 3) {
            check();
            if (flag == 0) {
                y -= SPEED * deltaTime;
            }
            else if (flag == 1) {
                y += SPEED * deltaTime;
            }
        }


        if (x < - GameScreen.WORLD_WIDTH)
            remove = true;

        updateCollisionCircle();

    }

    private void updateCollisionCircle() {
        AsteroidCircle.setX(x);
        AsteroidCircle.setY(y);
    }

    public boolean isFlappyColliding(Flappy flappee) {
        Circle flappyCollisionCircle = flappee.getCollisionCircle();
        return
                Intersector.overlaps(flappyCollisionCircle, AsteroidCircle) ;

    }


    public void render (SpriteBatch sb) {
        TextureRegion asteroidSlice = (TextureRegion) animation.getKeyFrame(animationTimer);
        float textureX = AsteroidCircle.x - TILE_WIDTH / 2;
        float textureY = AsteroidCircle.y - TILE_HEIGHT / 2;
        sb.draw(asteroidSlice, textureX, textureY, WIDTH, HEIGHT);
    }


  public void drawDebug(ShapeRenderer shapeRenderer) {

      shapeRenderer.circle(AsteroidCircle.x, AsteroidCircle.y, radius);
  }

    public float getX () {
        return x;
    }

    public float getY () {
        return y;
    }
}
