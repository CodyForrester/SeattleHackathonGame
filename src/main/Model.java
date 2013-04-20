package main;


import game.LoopingPlatform;
import game.Platform;
import game.Player;
import game.Timed;
import game.VerletIntegrator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import render.ColorSprite2D;
import render.Drawable;
import render.GridSprite2D;
import render.RectTextureSprite2D;
import util.MersenneTwister;
import util.TextureFactory;
import util.Vector;
import util.Positioned;
import util.SpatialAlgorithm;

public class Model implements Runnable{
	private static final double TIME_STEP = 0.01;
	
	public N64Controller player1;
	public N64Controller player2;
	
	public static final Random random = new MersenneTwister();
	
	
	private boolean gameRunning = true;
	
	protected List<Drawable> drawableObjects;
	protected List<Timed>    timedObjects;
	
	public Model() {
		drawableObjects = Collections.synchronizedList(new ArrayList<Drawable>());
		timedObjects = Collections.synchronizedList(new ArrayList<Timed>());
		GridSprite2D grid = new GridSprite2D(new Vector(-200, -200), new Vector(400, 400), 20, 20, 1);
		drawableObjects.add(grid);
		player1 = new N64Controller();
		player2 = new N64Controller();
	}
	
	public void run() {
		VerletIntegrator physics = new VerletIntegrator();
		Player p = new Player(new Vector(0,0));
		Platform plat = new Platform(new Vector(-50, -200), new Vector(100,10));
		LoopingPlatform plat2 = new LoopingPlatform(new Vector(0, -20), new Vector(10,10), new Vector(0,10), 10);
		drawableObjects.add(p);
		drawableObjects.add(plat);
		drawableObjects.add(plat2);
		
		physics.movingObjects.add(p);
		physics.staticObjects.add(plat);
		physics.staticObjects.add(plat2);
		
		timedObjects.add(physics);
		//timedObjects.add(p);
		timedObjects.add(plat2);
		
		while (gameRunning){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			for (Timed timed : timedObjects){
				timed.step(TIME_STEP);
			}
		}
		
		/*
		 * Clean up
		 */
	}
	
	public void endGame(){
		gameRunning = false;
	}
}
