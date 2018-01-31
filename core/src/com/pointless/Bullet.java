package com.pointless;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by vaibh on 7/4/2017.
 */

public class Bullet {public static final int SPEED = 370;
    public static final int WIDTH = 3;
    public static Texture texture;
    public final Circle BulletCircle;

    float x, y;

    public boolean remove = false;

    public Bullet (float x , float y) {
        this.x = x;
        this.y = y;
        BulletCircle = new Circle(x , y , WIDTH);
        if (texture == null)
            texture = new Texture("bullet.png");
    }

    private void updateCollisionCircle() {
        BulletCircle.setX(x);
    }

    public void update (float deltaTime) {
        x += SPEED * deltaTime;
        if (x > GameScreen.WORLD_WIDTH)
            remove = true;
        updateCollisionCircle();

    }



    public void draw (SpriteBatch sb) {
        sb.draw(texture, x, y);
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

