package game;

import util.Vector;
import render.ColorSprite2D;
import render.RectTextureSprite2D;

import java.awt.*;

import main.Model;
import util.PhysicsObject;

public class TreasureChest implements render.Drawable, util.RectanglePositioned, Timed {
	private boolean open;
	private String item;
	private Vector currentPosition;
	public static RectTextureSprite2D chestSpriteClosed;
	public static RectTextureSprite2D chestSpriteOpen;
	private Vector size;
	
	//creates a treasure chest that contains the specified item
	//chests are closed by default
	public TreasureChest(String item, Vector position){
		this.item = item;
		currentPosition = position;
		open = false;
		size = new Vector(30, 30);
	}
	
	public void draw(){
		if (open){
			chestSpriteOpen.setPosition(currentPosition);
			chestSpriteOpen.setDimension(size);
			chestSpriteOpen.draw();
		} else {
			chestSpriteClosed.setPosition(currentPosition);
			chestSpriteClosed.setDimension(size);
			chestSpriteClosed.draw();
		}
	}
	
	public Vector getSize(){
		return size;
	}
	
	public Vector getPosition(){
		return currentPosition;
	}
	
	public String getType(){
		return item;
	}
	
	public Vector collide(PhysicsObject a){
		if( !open ){
			Vector aPosition = a.getPosition();
			Vector aOldPosition = a.getOldPosition();
			Vector aSize = a.getSize();
			if (a instanceof Player && (aPosition.x + aSize.x > this.currentPosition.x && aPosition.x < this.currentPosition.x + this.size.x) && (aPosition.y + aSize.y > this.currentPosition.y && aPosition.y < this.currentPosition.y + this.size.y)){
				open = true;
				((Player)a).setAmmo(((Player)a).getAmmo()+5);
				return a.getPosition();
			}
			return null;
		} else {
			return null;
		}
	}
	
	public boolean isOpen(){
		return open;
	}

	@Override
	public void step(double timeStep) {
		if( Model.random.nextInt(10000) == 13 ){
			open = false;
		}
		
	}

}
