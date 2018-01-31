package com.pointless;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by vaibh on 7/4/2017.
 */

public class Flower {
    public float MAX_SPEED_PER_SECOND = 100F;
    private static final float COLLISION_CIRCLE_RADIUS = 33f;

    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;


    private static final float HEIGHT_OFFSET = 400f;

    private float x = 0;
    private float y = 0;
    private boolean pointClaimed = false;
    private float textureX;
    private float textureY;


    public final Circle botCollisionCircle;

    private final Texture botTexture;


    public Flower(Texture bottom){

        botTexture=bottom;
        y = MathUtils.random(HEIGHT_OFFSET);
        if (y >= GameScreen.WORLD_HEIGHT-66) {
            y = GameScreen.WORLD_HEIGHT-66 ;

        }
        else if ( y <= 0 + COLLISION_CIRCLE_RADIUS/4 ) {
            y = 0 + COLLISION_CIRCLE_RADIUS / 4;
        }
        botCollisionCircle = new Circle(x  , y , COLLISION_CIRCLE_RADIUS);
    }



    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }


    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();

    }

    public float getX() {
        return x;
    }

    public boolean isPointClaimed() {
        return pointClaimed;
    }

    public void markPointClaimed() {
        pointClaimed = true;
    }

    private void updateCollisionCircle() {
        botCollisionCircle.setX(x );
    }


    public boolean isFlappyColliding(Flappy flappee) {
        Circle flappyCollisionCircle = flappee.getCollisionCircle();
        return

                Intersector.overlaps(flappyCollisionCircle, botCollisionCircle) ;

    }

    public void draw(SpriteBatch batch) {
        drawBottomFlower(batch);

    }

    private void drawBottomFlower(SpriteBatch batch) {

        textureX = x - botTexture.getWidth()/2;
        textureY = y - botTexture.getHeight()/2 ;
        batch.draw(botTexture, textureX, textureY);
    }


    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(botCollisionCircle.x, botCollisionCircle.y, botCollisionCircle.radius);
    }
}
