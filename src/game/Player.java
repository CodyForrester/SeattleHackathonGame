package game;
import util.PhysicsObject;
import util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import render.ColorSprite2D;

public class Player implements util.PhysicsObject, render.Drawable {
	private Vector currentPosition;
	private Vector oldPosition;
	private String currentItem;
	private int playerWidth;
	private int playerHeight;
	private ColorSprite2D playersprite;
	
	private int killCount;
	private List<String> items;
	//private int mapWidth;
	//private int mapHeight;
	
	//pre: passed information of how big the player is
	//creates player with no kills and no items, at (0, 0)
	public Player(int width, int height){
		killCount = 0;
		items = new ArrayList<String>();
		playerWidth = width;
		playerHeight = height;
		oldPosition = new Vector();
		currentPosition = new Vector();
		//playersprite = new ColorSprite2D(????);
	}
	

	//returns player's old position
	public Vector getOldPosition(){
		return oldPosition;
	}
	
	//sets old position
	public Vector setOldPosition(Vector position){
		return position;
	}
	
	//returns player's current position
	public Vector getPosition(){
		return currentPosition;
	}
	
	
	public void draw(){
		playersprite.draw();
	}
	
	//return the player's size in the form (width, height)
	public Vector getSize(){
		return new Vector(playerWidth, playerHeight);
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
}*/

