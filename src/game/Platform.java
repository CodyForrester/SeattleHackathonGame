package game;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import render.ColorSprite2D;
import render.Drawable;
import render.Sprite2D;

import util.PhysicsObject;
import util.RectanglePositioned;
import util.Vector;

public class Platform implements RectanglePositioned, Drawable {
	
	private static Sprite2D texture;
	protected Vector position;
	private Vector size;
	
	public Platform(Vector position, Vector size) {
		this.texture = new ColorSprite2D(position, size, 0, Color.RED);
		this.position = position;
		this.size = size;
	}
	
	@Override
	public Vector getPosition() {
		return position;
	}
	
	@Override
	public Vector getSize() {
		return size;
	} 
	
	public void draw() {
		texture.setDimension(size);
		texture.setPosition(position);
		texture.draw();
	}
	
	public Vector collide(PhysicsObject a){
		Vector aPosition = a.getPosition();
		Vector aOldPosition = a.getOldPosition();
		Vector aSize = a.getSize();
		//System.out.println(aPosition);
		//if( this.y >= doA.oy + doA.height && (doA.y + doA.height >= this.y) && (doA.x < this.x + this.width && doA.x + doA.width > this.x) )
		if( (this.position.y + this.size.y >= aPosition.y) && (aPosition.x + aSize.x > this.position.x && aPosition.x < this.position.x + this.size.x) ){
			System.out.println("collided");
			return new Vector(aPosition.x, this.position.y + this.size.y);
		} else {
			//System.out.println("none");
			return null;
		}
	}
	
}
