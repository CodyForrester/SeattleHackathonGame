package game;

import java.util.ArrayList;
import java.util.List;
import util.Vector;

import util.PhysicsObject;
import util.RectanglePositioned;

public class VerletIntegrator implements Timed{
	//TODO eliminate these constants
	static final int PLAYER_SPEED = 1;
	public List<PhysicsObject> movingObjects;
	public List<RectanglePositioned> staticObjects;
	
	public VerletIntegrator(){
		movingObjects = new ArrayList<PhysicsObject>();
	}
	
 	public void step(double timeStep){
		for( PhysicsObject o : movingObjects ){
			integrateVerlet(o, timeStep);
		}
	}
 	
 	private void integrateVerlet(PhysicsObject o, double timeStep){
	  Vector position = o.getPosition();
	  Vector tempPosition = position.scale(1);
	  Vector oldPosition = o.getOldPosition();
	  Vector a = acceleration(o, timeStep);
	  position.addInPlace(position.scale(.97).minus(oldPosition.scale(.97).plus(a.scale(timeStep*timeStep))));
	  o.setOldPosition(tempPosition);
 	}
 	
 	private static final Vector gravity = new Vector(0,1);
 	private Vector acceleration(PhysicsObject o, double timeStep){
 		return gravity;
 	}
}
