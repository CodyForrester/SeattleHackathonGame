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
		satisfyConstraints();
	}

	public static final int ITERATION_COUNT = 3;

	public void satisfyConstraints(){
		for( int iterations = 0; iterations < ITERATION_COUNT; iterations++ ){
			for( PhysicsObject a : movingObjects ){
				for( PhysicsObject b : movingObjects ){
					if( !a.equals(b) ){
						Vector coords = b.collide(a);
						if( coords != null ){
							a.getPosition().setInPlace(coords);
						}
					}
				}
				for( RectanglePositioned b : staticObjects ){
					Vector coords = b.collide(a);
					if( coords != null ){
						a.getPosition().setInPlace(coords);
					}
				}
			}
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
