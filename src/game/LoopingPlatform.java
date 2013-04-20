package game;

import org.newdawn.slick.opengl.Texture;

import util.PhysicsObject;
import util.Vector;

public class LoopingPlatform extends Platform implements Timed {

	private Vector motion;
	private double period;
	private double timePassed;
	private Vector oldPosition;
	
	public LoopingPlatform(Vector position, Vector size, Vector motion, double period) {
		super(position, size);
		this.motion = motion;
		this.period = period;
		this.oldPosition = position;
	}

	@Override
	public void step(double timeStep) {
		oldPosition = position.clone();
		position.addInPlace(motion.scale(timeStep));
		timePassed += timeStep;
		if (timePassed > period) {
			motion = motion.scale(-1);
			position.addInPlace(motion.scale(timePassed - period));
			timePassed = 0;
		}
	}
	
	public Vector collide(PhysicsObject a){
		Vector aPosition = a.getPosition();
		Vector aOldPosition = a.getOldPosition();
		Vector aSize = a.getSize();
		//System.out.println(aPosition);
		//if( this.y >= doA.oy + doA.height && (doA.y + doA.height >= this.y) && (doA.x < this.x + this.width && doA.x + doA.width > this.x) )
		if( (this.position.y + this.size.y >= aPosition.y) && (this.oldPosition.y + this.size.y <= aOldPosition.y) && (aPosition.x + aSize.x > this.position.x && aPosition.x < this.position.x + this.size.x) ){
			a.setIsOnGround(true);
			if( a.getAcceleration().x == 0 ) {aOldPosition.addInPlace(new Vector((aPosition.x - aOldPosition.x)*.0025, 0)); }
			return new Vector(aPosition.x, this.position.y + this.size.y);
		} else {
			//System.out.println("none");
			return null;
		}
	}

}
