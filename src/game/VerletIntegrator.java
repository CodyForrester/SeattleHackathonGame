package game;

import java.util.ArrayList;
import java.util.List;

import main.Model;
import util.Vector;

import util.PhysicsObject;
import util.RectanglePositioned;

public class VerletIntegrator implements Timed{
	//TODO eliminate these constants
	static final double PLAYER_MAX_X_SPEED = .5;
	public List<PhysicsObject> movingObjects;
	public List<RectanglePositioned> staticObjects;
	private List<PhysicsObject> movingToDelete;
	
	private Model model;
	private List<PhysicsObject> movingToAdd;
	
	public void setModel(Model m){
		model = m;
	}
	public VerletIntegrator(){
		movingObjects = new ArrayList<PhysicsObject>();
		staticObjects = new ArrayList<RectanglePositioned>();
		movingToDelete = new ArrayList<PhysicsObject>();
		movingToAdd = new ArrayList<PhysicsObject>();
	}
	
	public void deleteMovingObject(PhysicsObject p){
		movingToDelete.add(p);
	}
	
	public void addMovingObject(PhysicsObject p){
		movingToAdd.add(p);
	}

	public void step(double timeStep){
		for( PhysicsObject o : movingObjects ){
			//o.getPosition().addInPlace(new Vector(0,-.001));
			integrateVerlet(o, timeStep);
		}
		satisfyConstraints();
	}

	public static final int ITERATION_COUNT = 3;

	public void satisfyConstraints(){
		for( int iterations = 0; iterations < ITERATION_COUNT; iterations++ ){
			for( PhysicsObject a : movingObjects ){
				Vector v = a.getPosition();
				Vector s = a.getSize();
				a.setIsOnWall(false);
				a.setIsOnGround(false);
				if( v.x <= model.MIN_X ){
					v.x = model.MIN_X;
					a.setIsOnWall(true);
					a.setIsLeftWall(true);
				}
				if( v.x >= model.MAX_X - s.x ){
					v.x = model.MAX_X - s.x;
					a.setIsOnWall(true);
					a.setIsLeftWall(false);
				}
				if( v.y <= model.MIN_Y ){
					v.y = model.MIN_Y;
					a.setIsOnGround(true);
				}
				if( v.y >= model.MAX_Y - s.y ){
					v.y = model.MAX_Y - s.y;
				}
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
				if( a.removeMe() ){
					movingToDelete.add(a);
					System.out.println(a);
				}
			}
		}
		for( PhysicsObject a : movingToDelete ){
			movingObjects.remove(a);
		}
		movingToDelete.clear();
		for( PhysicsObject a : movingToAdd ){
			movingObjects.add(a);
		}
		movingToAdd.clear();
	}

	private void integrateVerlet(PhysicsObject o, double timeStep){
		Vector position = o.getPosition();
		Vector tempPosition = position.minus(new Vector());
		Vector oldPosition = o.getOldPosition();
		Vector a = acceleration(o, timeStep);
		
		//b.x += 0.99*x-0.99*oldX+ax*dt*dt;
		Vector difference = position
				.minus(oldPosition)
				.plus(a.scale(timeStep*timeStep));
		if( o instanceof Player ){
			if( difference.x > PLAYER_MAX_X_SPEED ){
				difference.x = PLAYER_MAX_X_SPEED;
			} else if ( difference.x < -PLAYER_MAX_X_SPEED ){
				difference.x = -PLAYER_MAX_X_SPEED;
			}
		}
		position.addInPlace(difference);
		o.setOldPosition(tempPosition);
	}

	private Vector acceleration(PhysicsObject o, double timeStep){
		return o.getAcceleration();
	}
}
