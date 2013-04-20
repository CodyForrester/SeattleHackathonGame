package game;
import util.PhysicsObject;
import util.Vector;
import render.ColorSprite2D;
import java.awt.*;

import main.Model;
import main.N64Controller;

public class Projectile implements util.PhysicsObject, render.Drawable, Timed {
	private String type;
	private Vector oldPosition;
	private Vector currentPosition;
	private Vector size;
	private ColorSprite2D sprite = new ColorSprite2D(new Vector(), new Vector (5, 5), 0, Color.GREEN);
	private boolean isOnWall;
	private boolean isOnGround;
	private int directionFacing;
	public Player myTarget;
	
	//pre: passed a legal String for type, and a starting position
	public Projectile(String type, Vector starting){
		this.type = type;
		size = new Vector(15, 2);
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
		return isOnGround;
	}
	
	public void step(double v){
	}
	
	public void setIsOnGround(boolean ground){
		isOnGround = ground;
	}
	
	public boolean isOnWall(){
		return isOnWall;
	}
	
	public void setIsLeftWall(boolean value){
		//nothing
	}
	
	public void setIsOnWall(boolean value){
		isOnWall = value;
	}
	
	public boolean removeMe(){
		return isOnWall || isOnGround;
	}
	
	public Vector getSize(){
		return size;
	}
	
	public Vector collide(PhysicsObject a){
		Vector aPosition = a.getPosition();
		Vector aOldPosition = a.getOldPosition();
		Vector aSize = a.getSize();
		if (a == myTarget && (aPosition.x + aSize.x > this.currentPosition.x && aPosition.x < this.currentPosition.x + this.size.x) && (aPosition.y + aSize.y > this.currentPosition.y && aPosition.y < this.currentPosition.y + this.size.y)){
			//projectile hit a player, do something here
			((Player)a).removeKill();
			Vector newPosition = new Vector((Model.random.nextInt(2)*2-1)*500, -400);
			a.getOldPosition().setInPlace(newPosition);
			return newPosition;
		}
		return null;
	}
	
	public Vector getAcceleration(){
		return new Vector(); //projectiles should move at constant speed
	}
	
	public void setDirection(int direction){
		directionFacing = direction;
	}
}