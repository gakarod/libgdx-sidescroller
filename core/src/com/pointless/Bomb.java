package com.pointless;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

/**
 * Created by vaibh on 8/4/2017.
 */

public class Bomb {
    public static final int SPEED = 320;
    public static final int WIDTH = 16;
    public static Texture texture;
    public final Circle BombCircle;

    float x, y;

    public boolean remove = false;

    public Bomb (float x , float y) {
        this.x = x;
        this.y = y;
        BombCircle = new Circle(x , y , 8);
        if (texture == null)
            texture = new Texture("bomb.png");
    }

    private void updateCollisionCircle() {
        BombCircle.setX(x);
    }

    public void update (float deltaTime) {
        x -= SPEED * deltaTime;
        if (x < 0)
            remove = true;
        updateCollisionCircle();

    }

    public boolean isFlappyColliding(Flappy flappee) {
        Circle flappyCollisionCircle = flappee.getCollisionCircle();
        return
                Intersector.overlaps(flappyCollisionCircle, BombCircle) ;

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
