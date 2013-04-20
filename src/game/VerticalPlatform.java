package game;

import util.PhysicsObject;
import util.Vector;

public class VerticalPlatform extends Platform {
	
	public VerticalPlatform(Vector position, Vector size){
		super(position, size);
	}
	
	public Vector collide(PhysicsObject a){
		Vector aPosition = a.getPosition();
		Vector aOldPosition = a.getOldPosition();
		Vector aSize = a.getSize();
		if( (this.position.y + this.size.y >= aPosition.y) && (this.position.y + this.size.y - 2 <= aOldPosition.y) && (aPosition.x + aSize.x > this.position.x && aPosition.x < this.position.x + this.size.x ) ){
			a.setIsOnGround(true);
			if( a.getAcceleration().x == 0 ) {aOldPosition.addInPlace(new Vector((aPosition.x - aOldPosition.x)*.0025, 0)); }
			Vector target = new Vector(aPosition.x, this.position.y + this.size.y);
			aOldPosition.y = target.y;
			return target;
		} else if( (this.position.y <= aPosition.y + aSize.y) && (this.position.y >= aOldPosition.y + aSize.y) && (aPosition.x + aSize.x > this.position.x && aPosition.x < this.position.x + this.size.x ) ){
			Vector target = new Vector(aPosition.x, this.position.y -aSize.y );
			aOldPosition.y = target.y;
			return target;
		} else if( (this.position.x + this.size.x >= aPosition.x) && (this.position.x + this.size.x <= aOldPosition.x) && (aPosition.y + aSize.y > this.position.y && aPosition.y < this.position.y + this.size.y) ){
			a.setIsOnWall(true);
			a.setIsLeftWall(true);
			return new Vector(this.position.x + this.size.x, aPosition.y);
		} else if( (this.position.x <= aPosition.x + aSize.x) && (this.position.x >= aOldPosition.x) && (aPosition.y + aSize.y > this.position.y && aPosition.y < this.position.y + this.size.y) ){
			a.setIsOnWall(true);
			a.setIsLeftWall(false);
			return new Vector(this.position.x - aSize.x, aPosition.y);
		}  else {
			return null;
		}
	}
/*
	public Vector collide(PhysicsObject a){
		Vector aPosition = a.getPosition();
		Vector aOldPosition = a.getOldPosition();
		Vector aSize = a.getSize();
		//System.out.println(aPosition);
		//if( this.y >= doA.oy + doA.height && (doA.y + doA.height >= this.y) && (doA.x < this.x + this.width && doA.x + doA.width > this.x) )
		if( (this.position.y + Math.max(this.size.x, aSize.x) >= aPosition.y) && (this.position.y + this.size.y <= aOldPosition.y) && (aPosition.x + Math.max(this.size.x, aSize.x) > this.position.x && aPosition.x < this.position.x + Math.max(this.size.x, aSize.x)) ){
			a.setIsOnGround(true);
			System.out.println("Collided top.");
			Vector target = new Vector(aPosition.x, this.position.y + this.size.y);
			aOldPosition.setInPlace(target);
			return target;
		} else if( (this.position.x + this.size.x >= aPosition.x) && (this.position.x + this.size.x <= aOldPosition.x) && (aPosition.y + aSize.y > this.position.y && aPosition.y < this.position.y + this.size.y) ){
			a.setIsOnWall(true);
			a.setIsLeftWall(true);
			return new Vector(this.position.x + this.size.x, aPosition.y);
		} else if( (this.position.x <= aPosition.x + aSize.x) && (this.position.x >= aOldPosition.x) && (aPosition.y + aSize.y > this.position.y && aPosition.y < this.position.y + this.size.y) ){
			a.setIsOnWall(true);
			a.setIsLeftWall(false);
			return new Vector(this.position.x - aSize.x, aPosition.y);
		}  else {
			return null;
		}
	}*/
}
