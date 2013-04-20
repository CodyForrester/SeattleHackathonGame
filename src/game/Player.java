package game;
import java.util.*;

public class Player implements util.PhysicsObject {
	private int killCount;
	private List<String> items;
	private Vector position;
	private String currentItem;
	
	//pre: passed information of how big the map is
	//creates player with no kills and no items, at a random location on the map
	public Player(int width, int height){
		killCount = 0;
		items = new ArrayList<String>();
		respawn();
	}
	
	//pre: player has just been killed, or is spawning for the first time
	//post: removes all items from the player's inventory
	//      and respawns player at a new random location in the map
	public void respawn(){
		items.clear();
		Random r = new Random();
		position =  new Vector(r.nextInt(width), r.nextInt(height));
	}
	
	//returns player's current position
	public Vector getPosition(){
		return position;
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
	
	//returns the player model, subject to change because
	//I don't currently know how to return a model
	public String toString(){
		return "PLAYER";
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
}
