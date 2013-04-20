package game;

import util.Vector;
import render.ColorSprite2D;
import java.awt.*;
import util.PhysicsObject;

public class TreasureChest implements render.Drawable, util.RectanglePositioned {
	private boolean open;
	private String item;
	private Vector currentPosition;
	private static ColorSprite2D chestsprite = new ColorSprite2D(new Vector(), new Vector (10, 10), 0, Color.ORANGE);
	private Vector size;
	
	//creates a treasure chest that contains the specified item
	//chests are closed by default
	public TreasureChest(String item, Vector position){
		this.item = item;
		currentPosition = position;
		open = false;
		size = new Vector(10, 10);
	}
	
	public void draw(){
		if (open){
			size.y = 15;
		}
		chestsprite.setPosition(currentPosition);
		chestsprite.setDimension(size);
		chestsprite.draw();
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
		Vector aPosition = a.getPosition();
		Vector aOldPosition = a.getOldPosition();
		Vector aSize = a.getSize();
		if (a instanceof Player && (aPosition.x + aSize.x > this.currentPosition.x && aPosition.x < this.currentPosition.x + this.size.x) && (aPosition.y + aSize.y > this.currentPosition.y && aPosition.y < this.currentPosition.y + this.size.y)){
			open = true;
			return a.getPosition();
		}
		return null;
	}
	
	public boolean isOpen(){
		return open;
	}

}
