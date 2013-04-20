package render;

import org.lwjgl.opengl.GL11;

public enum Color {

	RED(1, 0, 0),
	GREEN(0, 1, 0),
	BLUE(0, 0, 1),
	BLACK(0, 0, 0),
	WHITE(1, 1, 1);
	
	private float red, green, blue;
	
	private Color(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public static void setColor(Color color) {
		GL11.glColor3f(color.red, color.green, color.blue);
	}
	
}
