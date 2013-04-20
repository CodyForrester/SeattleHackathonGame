package main;


import game.LoopingPlatform;
import game.TreasureChest;
import game.Platform;
import game.Player;
import game.Timed;
import game.VerletIntegrator;
import game.VerticalPlatform;

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
		
		//Player Sprites
		Player.player1Punch = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatPunch.png", "PNG");
		Player.player2Punch = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocPunch.png", "PNG");
		Player.player1Stand = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatStanding.png", "PNG");
		Player.player2Stand = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocStanding.png", "PNG");
		Player.player1Jump = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatJump.png", "PNG");
		Player.player2Jump = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocJump.png", "PNG");
		Player.player1WallJump = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatWallJump.png", "PNG");
		Player.player2WallJump = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocWallJump.png", "PNG");
		Player.player1Gun = new RectTextureSprite2D(new Vector(), new Vector(25, 31), 0, "assets/textures/CatGun.png", "PNG");
		Player.player2Gun = new RectTextureSprite2D(new Vector(), new Vector(25, 31), 0, "assets/textures/CrocGun.png", "PNG");
		Player.player1GunDown = new RectTextureSprite2D(new Vector(), new Vector(25, 31), 0, "assets/textures/CatGunDown.png", "PNG");
		Player.player2GunDown = new RectTextureSprite2D(new Vector(), new Vector(25, 31), 0, "assets/textures/CrocGunDown.png", "PNG");
		Player.crownSprite = new RectTextureSprite2D(new Vector(), new Vector(15, 11), 0, "assets/textures/crown.png", "PNG");
		
		Player.player1Jet0 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatJetpack0.png", "PNG");
		Player.player1Jet1 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatJetpack1.png", "PNG");
		Player.player1Jet2 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatJetpack2.png", "PNG");
		Player.player1Jet3 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatJetpack3.png", "PNG");
		Player.player2Jet0 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocJetpack0.png", "PNG");
		Player.player2Jet1 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocJetpack1.png", "PNG");
		Player.player2Jet2 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocJetpack2.png", "PNG");
		Player.player2Jet3 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocJetpack3.png", "PNG");
		
		Player.player1Walk0 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatWalking0.png", "PNG");
		Player.player1Walk1 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatWalking1.png", "PNG");
		Player.player1Walk2 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CatWalking2.png", "PNG");
		Player.player2Walk0 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocWalk0.png", "PNG");
		Player.player2Walk1 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocWalk1.png", "PNG");
		Player.player2Walk2 = new RectTextureSprite2D(new Vector(), new Vector(22, 31), 0, "assets/textures/CrocWalk2.png", "PNG");
		
		
		AudioPlayer.start();
		physics = new VerletIntegrator();
		physics.setModel(this);
		player1 = new Player(new Vector(0,0));
		player2 = new Player(new Vector(80,0));
		Platform plat = new Platform(new Vector(MIN_X, MIN_Y), new Vector(MAX_X - MIN_X,10));
		
		Platform plat1 = new Platform(new Vector(-444,-222), new Vector(350,3));
		physics.staticObjects.add(plat1);
		Platform plat2 = new Platform(new Vector(-81,-181), new Vector(218,3));
		physics.staticObjects.add(plat2);
		Platform plat3 = new Platform(new Vector(120,-233), new Vector(213,3));
		physics.staticObjects.add(plat3);
		Platform plat4 = new Platform(new Vector(310,-149), new Vector(140,3));
		physics.staticObjects.add(plat4);
		Platform plat5 = new Platform(new Vector(-350,-113), new Vector(140,3));
		physics.staticObjects.add(plat5);
		Platform plat6 = new Platform(new Vector(-450,-22), new Vector(140,3));
		physics.staticObjects.add(plat6);
		Platform plat7 = new Platform(new Vector(153,-49), new Vector(176,3));
		physics.staticObjects.add(plat7);
		Platform plat8 = new Platform(new Vector(-172,-52), new Vector(135,3));
		physics.staticObjects.add(plat8);
		Platform plat9 = new Platform(new Vector(-261,19), new Vector(227,3));
		physics.staticObjects.add(plat9);
		Platform plat10 = new Platform(new Vector(40,60), new Vector(410,3));
		physics.staticObjects.add(plat10);
		
		VerticalPlatform plat11 = new VerticalPlatform(new Vector(3,-300), new Vector(5,120));
		physics.staticObjects.add(plat11);
		VerticalPlatform plat12 = new VerticalPlatform(new Vector(-213,-222), new Vector(5,110));
		physics.staticObjects.add(plat12);
		VerticalPlatform plat13 = new VerticalPlatform(new Vector(-111,-50), new Vector(5,72));
		physics.staticObjects.add(plat13);
		VerticalPlatform plat14 = new VerticalPlatform(new Vector(268,-230), new Vector(5,130));
		physics.staticObjects.add(plat14);
		VerticalPlatform plat15 = new VerticalPlatform(new Vector(248,-50), new Vector(5,205));
		physics.staticObjects.add(plat15);		
		VerticalPlatform plat16 = new VerticalPlatform(new Vector(37,-180), new Vector(5,130));
		physics.staticObjects.add(plat16);
		VerticalPlatform plat17 = new VerticalPlatform(new Vector(215,-130), new Vector(5,80));
		physics.staticObjects.add(plat17);
		
		drawableObjects.add(plat11);
		drawableObjects.add(plat12);
		drawableObjects.add(plat13);
		drawableObjects.add(plat14);
		drawableObjects.add(plat15);
		drawableObjects.add(plat16);
		drawableObjects.add(plat17);
		//LoopingPlatform plat2 = new LoopingPlatform(new Vector(0, -20), new Vector(100,10), new Vector(0,10), 10);
		
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
		
		TreasureChest chest = new TreasureChest("assault", new Vector(0, -190));
		TreasureChest.chestSpriteClosed  = new RectTextureSprite2D(new Vector(), new Vector(), 0, "assets/textures/ChestClosed.png", "PNG");
		TreasureChest.chestSpriteOpen  = new RectTextureSprite2D(new Vector(), new Vector(), 0, "assets/textures/ChestOpen.png", "PNG");
		
		drawableObjects.add(chest);
		physics.staticObjects.add(chest);
		
		physics.staticObjects.add(plat);
		physics.staticObjects.add(plat2);
		
		timedObjects.add(physics);
		
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
