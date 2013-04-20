package game;
import util.PhysicsObject;
import util.Vector;
import render.ColorSprite2D;
import java.awt.*;

import main.N64Controller;

public class Projectile implements util.PhysicsObject, render.Drawable {
	private String type;
	private Vector oldPosition;
	private Vector currentPosition;
	private Vector size;
	private ColorSprite2D sprite = new ColorSprite2D(new Vector(), new Vector (5, 5), 0, Color.GREEN);
	private boolean isOnWall;
	
	//pre: passed a legal String for type, and a starting position
	public Projectile(String type, Vector starting){
		this.type = type;
		size = new Vector(5, 5);
		oldPosition = starting.minus(new Vector(5,0));
		currentPosition = starting;
	}
	
	public String getType(){
		return type;
	}
	
	public void draw(){
		sprite.setPosition(currentPosition);
		sprite.setDimension(size);
		sprite.draw();
	}
	
	public Vector getPosition(){
		return currentPosition;
	}
	
	public Vector getOldPosition(){
		return oldPosition;
	}
	
	public void setOldPosition(Vector position){
		oldPosition = position;
	}
	
	public boolean isOnGround(){
		return false;
	}
	
	public void setIsOnGround(boolean ground){
		//do nothing
	}
	
	public boolean isOnWall(){
		return isOnWall;
	}
	
	public void setIsOnWall(boolean value){
		isOnWall = value;
	}
	
	public boolean removeMe(){
		return isOnWall;
	}
	
	public Vector getSize(){
		return size;
	}
	
	public Vector collide(PhysicsObject a){
		Vector aPosition = a.getPosition();
		Vector aOldPosition = a.getOldPosition();
		Vector aSize = a.getSize();
		if (a instanceof Player && (aPosition.x + aSize.x > this.currentPosition.x && aPosition.x < this.currentPosition.x + this.size.x) && (aPosition.y + aSize.y > this.currentPosition.y && aPosition.y < this.currentPosition.y + this.size.y)){
			//projectile hit a player, do something here
			return a.getPosition();
		}
		return null;
	}
	
	public Vector getAcceleration(){
		return new Vector(); //projectiles should move at constant speed
	}
}