package util;

public interface RectanglePositioned extends Positioned{
	public Vector getSize();
	public Vector collide(PhysicsObject a);
}
