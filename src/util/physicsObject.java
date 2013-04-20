package util;

public interface PhysicsObject extends RectanglePositioned{
	public Vector getOldPosition();
	public void setOldPosition(Vector position);
	public Vector getAcceleration();
}
