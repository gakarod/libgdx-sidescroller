package com.pointless;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * Created by vaibh on 7/4/2017.
 */

public class Flappy {public static final float COLLISION_RADIUS = 24f;
    private static final float DIVE_ACCEL = 0.30F;
    private static final float FLY_ACCEL = 3F;
    public static final int TILE_WIDTH=64;
    public static final int TILE_HEIGHT=43;
    Texture ship ;


    private Animation animation;
    private float animationTimer = 0;
    private static Circle collisionCircle;

    public float x = 0;
    public float y = 0;
    private float ySpeed = 0;


    public Flappy(Texture texture){

         ship = texture ;
         collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public static Circle getCollisionCircle() {return collisionCircle;}
    public float getY() { return y;}
    public float getX() {return x;}

    public void update(float delta) {

        animationTimer += delta;
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp() {
        ySpeed = FLY_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }

    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void draw(SpriteBatch sb) {

        float textureX = collisionCircle.x - TILE_WIDTH / 2;
        float textureY = collisionCircle.y - TILE_HEIGHT / 2;
        sb.draw(ship, textureX, textureY);
    }

    public void drawDebug(ShapeRenderer sr){
        sr.circle(x, y, COLLISION_RADIUS);
    }

}
