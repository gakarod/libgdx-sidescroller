package com.pointless;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class FlappyImpact extends Game {

    AdHandler adhandler ;
	public FlappyImpact(AdHandler adhandler) {
		this.adhandler = adhandler;
	}
	@Override
	public void create () {
		setScreen(new StartScreen(this , adhandler));
	}


}
