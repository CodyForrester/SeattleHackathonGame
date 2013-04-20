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
		//System.out.println(aPosition);
		//if( this.y >= doA.oy + doA.height && (doA.y + doA.height >= this.y) && (doA.x < this.x + this.width && doA.x + doA.width > this.x) )
		if( (this.position.x + this.size.x >= aPosition.x) && (this.position.x + this.size.x <= aOldPosition.x) && (aPosition.y + aSize.y > this.position.y && aPosition.y < this.position.y + this.size.y) ){
			a.setIsOnWall(true);
			a.setIsLeftWall(true);
			return new Vector(this.position.x + this.size.x, aPosition.y);
		} else if( (this.position.x <= aPosition.x + aSize.x) && (this.position.x >= aOldPosition.x) && (aPosition.y + aSize.y > this.position.y && aPosition.y < this.position.y + this.size.y) ){
			a.setIsOnWall(true);
			a.setIsLeftWall(false);
			return new Vector(this.position.x - aSize.x, aPosition.y);

		} else {
			return null;
		}
	}
}
