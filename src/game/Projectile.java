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
	
	//pre: passed a legal String for type, and a starting position
	public Projectile(String type, Vector starting){
		this.type = type;
		size = new Vector(5, 5);
		oldPosition = starting;
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
	
	public Vector getSize(){
		return size;
	}
	
	public Vector collide(PhysicsObject a){
		return null;
	}
	
	public Vector getAcceleration(){
		return new Vector(); //projectiles should move at constant speed
	}
}