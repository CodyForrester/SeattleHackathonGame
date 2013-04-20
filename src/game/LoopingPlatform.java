package game;

import org.newdawn.slick.opengl.Texture;

import util.Vector;

public class LoopingPlatform extends Platform implements Timed {

	private Vector motion;
	private double period;
	private double timePassed;
	
	public LoopingPlatform(Texture texture, Vector position, Vector size, Vector motion, double period) {
		super(position, size);
		this.motion = motion;
		this.period = period;
	}

	@Override
	public void step(double timeStep) {
		position.addInPlace(motion.scale(timeStep));
		timePassed += timeStep;
		if (timePassed > period) {
			motion = motion.scale(-1);
			position.addInPlace(motion.scale(timePassed - period));
			timePassed = 0;
		}
	}

}
