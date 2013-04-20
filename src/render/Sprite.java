package render;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

public class Sprite {

	private static final ColorModel
	glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {8,8,8,8}, true, false, ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE),
	glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] {8,8,8,0}, false, false, ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);

	private int textureID;
	private BufferedImage image;
	private int width, height;
	private float widthRatio, heightRatio;

	public Sprite(BufferedImage source) {
		textureID = defineTexture(source);
		image = source;
		width = image.getWidth();
		height = image.getHeight();
		widthRatio = (float)width/twoFold(width);
		heightRatio = (float)height/twoFold(height);
	}

	public Sprite(String filename) throws IOException {
		this(ImageIO.read(new File(filename)));
	}

	private static ByteBuffer convertToTextureData(BufferedImage image) {
		WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, twoFold(image.getWidth()), twoFold(image.getHeight()), image.getColorModel().hasAlpha() ? 4 : 3, null);
		BufferedImage texture = new BufferedImage(image.getColorModel().hasAlpha() ? glAlphaColorModel : glColorModel, raster, false, null);
		Graphics g = texture.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		byte[] data = ((DataBufferByte) texture.getRaster().getDataBuffer()).getData();
		ByteBuffer textureData = ByteBuffer.allocateDirect(data.length);
		textureData.order(ByteOrder.nativeOrder());
		textureData.put(data, 0, data.length);
		textureData.flip();
		return textureData;
	}

	private static int defineTexture(BufferedImage image) {
		ByteBuffer textureData = convertToTextureData(image);
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, twoFold(image.getWidth()), twoFold(image.getHeight()), 0, image.getColorModel().hasAlpha() ? GL11.GL_RGBA : GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, textureData);
		return textureID;
	}

	private static int twoFold(int fold) {
		int ret = 1;
		while (ret < fold) {
			ret *= 2;
		}
		return ret;
	}

	public void draw(int x, int y) {
		GL11.glPushMatrix();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		GL11.glTranslatef(x, y, 0);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);

			GL11.glTexCoord2f(0, heightRatio);
			GL11.glVertex2f(0, height);

			GL11.glTexCoord2f(widthRatio, heightRatio);
			GL11.glVertex2f(width, height);

			GL11.glTexCoord2f(widthRatio, 0);
			GL11.glVertex2f(width, 0);
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}
	
	// DUMB!
	public void drawBackwards(int x, int y) {
		GL11.glPushMatrix();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		GL11.glTranslatef(x, y, 0);

		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(widthRatio, 0);
			GL11.glVertex2f(0, 0);

			GL11.glTexCoord2f(widthRatio, heightRatio);
			GL11.glVertex2f(0, height);

			GL11.glTexCoord2f(0, heightRatio);
			GL11.glVertex2f(width, height);

			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(width, 0);
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}

	public void draw(int x, int y, Color color) {
		Color.setColor(color);
		draw(x, y);
	}

}
