package com.pointless;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

/**
 * Created by vaibh on 7/12/2017.
 */

public class Boss {

    public static  int SPEED = 150;

    public static int getWIDTH() {
        return WIDTH;
    }

    public static  int WIDTH = 150;
    public static  int HEIGHT = 150;
    private static Texture texture;
    public final Circle BossCircle;
    public float BossHealth = 310;
    public static final int radius = 75 ;
    public static  int TILE_WIDTH=150;
    public static  int TILE_HEIGHT=150;
    public static final float FRAME_DURATION = .25F;
    public int flag = 0 ;
    private Animation animation;
    private float animationTimer = 0;
    int max = 4 ;
    int min = 0 ;
    int type = 0 ;
    public float x, y;

    public boolean remove = false;

    public Boss (float y) {

        type = min + (int)(Math.random() * max);
        if(type == 1)
            texture = new Texture("boss.png");

        else if(type == 2) {

            texture = new Texture("boss2.png");
        }

        else if(type == 3) {
            texture = new Texture("boss3.png");
        }

        else  {
            texture = new Texture("boss.png");
        }

        TextureRegion[][] textures = new TextureRegion(texture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(FRAME_DURATION, textures[0][0],textures[0][1],textures[0][2],textures[0][3]);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        this.x = GameScreen.WORLD_WIDTH - 150  ;
        this.y = y;
        BossCircle = new Circle(x , y , radius);

    }
    private void reset () {

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
        animationTimer += deltaTime/2;
        check();

        if(flag == 0 )
            y -= SPEED*deltaTime ;

        if(flag == 1)
            y += SPEED*deltaTime ;

        if (BossHealth < 0) {
           remove = true;
        }
        updateCollisionCircle();

    }

    private void updateCollisionCircle() {
        BossCircle.setY(y);
    }



    public void render (SpriteBatch sb) {
        TextureRegion bossSlice = (TextureRegion) animation.getKeyFrame(animationTimer);
        float textureX = BossCircle.x - TILE_WIDTH / 2;
        float textureY = BossCircle.y - TILE_HEIGHT / 2;
        sb.draw(bossSlice, textureX, textureY, WIDTH, HEIGHT);
    }


    public void drawDebug(ShapeRenderer shapeRenderer) {

        shapeRenderer.circle(BossCircle.x, BossCircle.y, radius);
    }

    public float getX () {
        return x;
    }

    public float getY () {
        return y;
    }
}


