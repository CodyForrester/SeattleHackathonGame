package main;


import game.LoopingPlatform;
import game.TreasureChest;
import game.Platform;
import game.Player;
import game.Timed;
import game.VerletIntegrator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import audio.AudioPlayer;

import render.ColorSprite2D;
import render.Drawable;
import render.RectTextureSprite2D;
import util.MersenneTwister;
import util.PhysicsObject;
import util.TextureFactory;
import util.Vector;
import util.Positioned;
import util.SpatialAlgorithm;

public class Model implements Runnable{
	private static final double TIME_STEP = 0.01;
	public final double MIN_X = -450;
	public final double MAX_X = 450;
	public final double MIN_Y = -300;
	public final double MAX_Y = 300;
	
	public N64Controller player1Controller;
	public N64Controller player2Controller;
	public Player player1;
	public Player player2;
	
	
	public static final Random random = new MersenneTwister();
	
	
	private boolean gameRunning = true;
	
	public List<Drawable> drawableObjects;
	public List<Timed>    timedObjects;
	public VerletIntegrator physics;
	public List<PhysicsObject> thingsToAdd;
	public List<PhysicsObject> thingsToRemove;
	
	public Model() {
		drawableObjects = Collections.synchronizedList(new ArrayList<Drawable>());
		timedObjects = Collections.synchronizedList(new ArrayList<Timed>());
		player1Controller = new N64Controller();
		player2Controller = new N64Controller();
		thingsToAdd = new ArrayList<PhysicsObject>();
		thingsToRemove = new ArrayList<PhysicsObject>();
	}
	
	public void run() {
		drawableObjects.add(new RectTextureSprite2D(new Vector(MIN_X, MIN_Y), new Vector(MAX_X-MIN_X, MAX_Y-MIN_Y), 0, "assets/textures/Stage1.png", "PNG"));
		AudioPlayer.start();
		physics = new VerletIntegrator();
		physics.setModel(this);
		player1 = new Player(new Vector(0,0));
		player2 = new Player(new Vector(80,0));
		Platform plat = new Platform(new Vector(MIN_X, MIN_Y), new Vector(MAX_X - MIN_X,10));
		
		LoopingPlatform plat2 = new LoopingPlatform(new Vector(0, -20), new Vector(100,10), new Vector(0,10), 10);
		
		drawableObjects.add(player1);
		player1.setController(player1Controller);
		player1.setModel(this);
		physics.movingObjects.add(player1);
		timedObjects.add(player1);
		
		drawableObjects.add(player2);
		player2.setController(player2Controller);
		player2.setModel(this);
		physics.movingObjects.add(player2);
		timedObjects.add(player2);
		
		drawableObjects.add(plat);
		drawableObjects.add(plat2);
		
		TreasureChest chest = new TreasureChest("assault", new Vector(0, -190));
		TreasureChest.chestSpriteClosed  = new RectTextureSprite2D(new Vector(), new Vector(), 0, "assets/textures/ChestClosed.png", "PNG");
		TreasureChest.chestSpriteOpen  = new RectTextureSprite2D(new Vector(), new Vector(), 0, "assets/textures/ChestOpen.png", "PNG");
		
		drawableObjects.add(chest);
		physics.staticObjects.add(chest);
		
		physics.staticObjects.add(plat);
		physics.staticObjects.add(plat2);
		
		timedObjects.add(physics);
		timedObjects.add(plat2);
		
		while (gameRunning){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Timed timed : timedObjects){
				timed.step(TIME_STEP);
				if( timed instanceof PhysicsObject ){
					if( ((PhysicsObject) timed).removeMe() ){
						thingsToRemove.add((PhysicsObject)timed);
					}
				}
			}
			for(PhysicsObject toRemove : thingsToRemove ){
				timedObjects.remove(toRemove);
				drawableObjects.remove(toRemove);
			}
			for(PhysicsObject toAdd : thingsToAdd ){
				timedObjects.add((Timed)toAdd);
				drawableObjects.add((Drawable)toAdd);
			}
			thingsToRemove.clear();
			thingsToAdd.clear();
		}
		
		/*
		 * Clean up
		 */
	}
	
	public void endGame(){
		gameRunning = false;
	}
}
