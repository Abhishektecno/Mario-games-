package com.mygdx.abhigame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.Text;

import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class MyGdxGame10 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState =0;
	int pause =0;

	float gravity = 0.2f;
	float velocity = 0;
	int score = 0;
	int gameState = 0;

	BitmapFont font;
	int manY = 0;
	Texture coin;
	ArrayList<Integer> coinXs = new ArrayList<>();
	ArrayList<Integer> coinYs = new ArrayList<>();
	ArrayList<Rectangle> coinRectangle= new ArrayList<>();
	int coinCount = 0;

	ArrayList<Integer> bombXs = new ArrayList<>();
	ArrayList<Integer> bombYs = new ArrayList<>();
	ArrayList<Rectangle> bombRectangle = new ArrayList<>();

	int bombCount = 0;
	Rectangle manRectangle;
	Texture bomb;
	Random random;





	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		man = new Texture[4];
		man[0] = new Texture("mario.png");
		man[1] = new Texture("mario2.png");

		man[2] = new Texture("mario3.png");

		man[3] = new Texture("mario4.png");
		coin = new Texture("coins.png");
		bomb = new Texture("bom.png");
		random = new Random();
		manY = Gdx.graphics.getHeight()/2;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}
	public void makeCoins(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());
	}
	public void makeBomb(){
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int) height);
		bombXs.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gameState ==1){
			//game is live
			if(coinCount < 150){
				coinCount++;
			}else{
				coinCount = 0;
				makeCoins();
			}
			coinRectangle.clear();
			for(int i=0;i<coinXs.size();i++)
			{
				batch.draw(coin,coinXs.get(i),coinYs.get(i));
				coinXs.set(i,coinXs.get(i)-4);
				coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));

			}
			if(bombCount < 450){
				bombCount++;
			}else{
				bombCount = 0;
				makeBomb();
			}
			bombRectangle.clear();
			for(int i=0;i<bombXs.size();i++)
			{
				batch.draw(bomb,bombXs.get(i),bombYs.get(i));
				bombXs.set(i,bombXs.get(i)-4);
				bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));


			}


			if (Gdx.input.justTouched()){
				velocity -= 10;
			}

			if(pause<8){
				pause++;

			}else {
				pause = 0;

				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}
			velocity += gravity;
			manY -= velocity;
			if(manY <= 0 ){
				manY = 0;
			}
		}else if(gameState == 0 ){
			//waiting for player to start the game
			if ((Gdx.input.justTouched())){
				gameState = 1;
			}
		}else if(gameState ==2){
			//game over
			if(Gdx.input.justTouched()){
				gameState = 1;
				manY = Gdx.graphics.getHeight()/2;
				score=0;
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinCount = 0;
				coinRectangle.clear();
				bombXs.clear();
				bombYs.clear();
				bombCount = 0;
				bombRectangle.clear();


			}
		}



		batch.draw(man[manState],Gdx.graphics.getWidth()/2-man[manState].getWidth(),manY);
		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2-man[manState].getWidth(),manY,man[manState].getWidth(),man[manState].getHeight());

		for(int i=0;i<coinRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
//				Gdx.app.log("Coins!!","Collision");
				score++;
				coinRectangle.remove(i);
			    coinXs.remove(i);
				coinYs.remove(i);
				break;
			}
		}
		for(int i=0;i<bombRectangle.size();i++){
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				Gdx.app.log("Bombs!!","Collision");
                gameState =2;
			}
		}
		font.draw(batch,String.valueOf(score),500,200);


		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
