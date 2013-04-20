package game;
import util.PhysicsObject;
import util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import render.ColorSprite2D;
import java.awt.*;

import main.Model;
import main.N64Controller;

public class Player implements util.PhysicsObject, render.Drawable, game.Timed {
	private Vector currentPosition;
	private Vector oldPosition;
	private String currentItem;
	private static ColorSprite2D playersprite = new ColorSprite2D(new Vector(), new Vector (20, 32), 0, Color.BLUE);
	private N64Controller controller;
	private boolean isOnGround;
	private boolean isOnWall;
	private Vector playerSize;
	private Model model;
	private int timer;
	
	
	public void setModel(Model m){
		model = m;
	}
	//private int killCount;
	//private List<String> items;
	//private int mapWidth;
	//private int mapHeight;
	
	//passed a valid starting position vector
	//creates player with no kills and no items, at given vector
	public Player(Vector starting){
		//killCount = 0;
		//items = new ArrayList<String>();
		oldPosition = starting;
		currentPosition = starting;
		playerSize = new Vector(20, 32);
		timer = 0;
	}
	

	//returns player's old position
	public Vector getOldPosition(){
		return oldPosition;
	}
	
	//sets old position
	public void setOldPosition(Vector position){
		oldPosition = position;
	}
	
	//returns player's current position
	public Vector getPosition(){
		return currentPosition;
	}
	
	public Vector getAcceleration(){
		Vector accel = new Vector(0,-15);
		
		if( isOnGround ){
			accel.x = controller.getX()*5;
		} else if ( controller.getButton5() ){
			accel.x = controller.getX()*15;
		} else {
			accel.x = controller.getX()*2.5;
		}
		if( controller.getButton5() ){
			accel.y = 10;
		}
		return accel;
	}
	
	public void draw(){
		playersprite.setPosition(currentPosition);
		playersprite.setDimension(playerSize);
		playersprite.draw();
	}
	
	public void step(double timestep){
		if( controller.getTrigger() ){
			System.out.println("Fire!");
			Projectile p = new Projectile("bullet", currentPosition.plus(new Vector(0, playerSize.y / 2)));
			model.physics.movingObjects.add(p);
			model.drawableObjects.add(p);
		}
		if (isOnGround()) {
			if( controller.getButton4() ){
				this.oldPosition.y -= .2;
				timer = 200;
			}
		} else {
			if( timer > 0 && controller.getButton4() ){
				this.oldPosition.y -= .000025*timer;
			} else {
				timer = 0;
			}
			timer--;
		}
	}
	
	public void setController(N64Controller controller){
		this.controller = controller;
	}
	//return the player's size in the form (width, height)
	public Vector getSize(){
		return playerSize;
	}
	
	//return whether the player is on the ground
	public boolean isOnGround(){
		return isOnGround;
	}
	
	//sets whether the player is on the ground
	public void setIsOnGround(boolean ground){
		isOnGround = ground;
	}
	
	public boolean isOnWall(){
		return isOnWall;
	}
	
	public void setIsOnWall(boolean value){
		isOnWall = value;
	}
	
	public boolean removeMe(){
		return false;
	}
	public Vector collide(PhysicsObject a){
		return null;
	}
}


/*	//pre: player has just been killed
//post: removes all items from the player's inventory
//      and respawns player at a new random location in the map
public void respawn(){
	items.clear();
	Random r = new Random();
	position =  new Vector(r.nextInt(mapWidth), r.nextInt(mapHeight));
}


//pre: passed a string containing item name
//post: adds item to player's arsenal
//     or does nothing if player already has weapon
public void addItem(String item){
	if (!items.contains(item)){
		items.add(item);
	}
}

//pre: passed a String of what item the user wants to switch to
//post: switches to that weapon
public void switchItem(String item){
	if (!items.contains(item)){
		throw new IllegalArgumentException("Player does not have that item");
	}
	currentItem = item;
}

//returns what weapon the player is currently holding
public String currentItem(){
	return currentItem;
}

//returns current number of kills this player has
public int getKillCount(){
	return killCount;
}

//adds 1 to the player's score
public void addKill(){
	killCount++;
}

//removes 1 from the player's score
//can be used if the player suicides
public void removeKill(){
	killCount--;
}
*/