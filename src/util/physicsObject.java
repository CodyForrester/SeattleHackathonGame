package util;

public interface PhysicsObject extends RectanglePositioned{
	public Vector getOldPosition();
	public Vector setOldPosition(Vector position);
}
