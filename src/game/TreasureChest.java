package game;

import util.Vector;
import render.ColorSprite2D;
import java.awt.*;

public class TreasureChest implements render.Drawable, util.RectanglePositioned {
	private String item;
	private Vector currentPosition;
	private static ColorSprite2D chestsprite = new ColorSprite2D(new Vector(), new Vector (10, 10), 0, Color.ORANGE);
	
	//creates a treasure chest that contains the specified item
	public TreasureChest(String item, Vector position){
		this.item = item;
		currentPosition = position;
	}
	
	public void draw(){
		chestsprite.draw();
	}
	
	public Vector getSize(){
		return new Vector(10, 10);
	}
	
	public Vector getPosition(){
		return currentPosition;
	}
	
	public String getType(){
		return item;
	}

}
