package game;
import util.PhysicsObject;
import util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import render.ColorSprite2D;
import java.awt.*;
import main.N64Controller;

public class Player implements util.PhysicsObject, render.Drawable, game.Timed {
	private Vector currentPosition;
	private Vector oldPosition;
	private String currentItem;
	private static ColorSprite2D playersprite = new ColorSprite2D(new Vector(), new Vector (20, 32), 0, Color.BLUE);
	private N64Controller controller;
	private boolean isOnGround;
	private Vector playerSize;
	
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
		return new Vector(controller.getX()*5, controller.getButton5()?10:0);
	}
	
	public void draw(){
		playersprite.setPosition(currentPosition);
		playersprite.setDimension(playerSize);
		playersprite.draw();
	}
	
	public void step(double timestep){
		if (isOnGround())
			this.playerSize.x = 15;
		else
			this.playerSize.x = 20;
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