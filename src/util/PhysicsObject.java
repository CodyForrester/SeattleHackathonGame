package util;

public interface PhysicsObject extends RectanglePositioned{
	public Vector getOldPosition();
	public void setOldPosition(Vector position);
	public Vector getAcceleration();
	public boolean isOnGround();
	public void setIsOnGround(boolean value);
	public boolean isOnWall();
	public void setIsOnWall(boolean value);
}
