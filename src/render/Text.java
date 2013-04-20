package render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Text {
	
	// prevent instantiation
	private Text() {}

	public static enum HorizontalAlignment {LEFT, CENTER, RIGHT};
	public static enum VerticalAlignment {TOP, BOTTOM};

	public static enum Size {SMALL, MEDIUM, LARGE}

	private static final char[] characters = new char[] {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
		'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
		'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '!', '?',
		'+', '-', '=', ',', '<', '>', '/', '\'', '*', '\"', ':', '@'
	};
	
	private static final int BASE_WIDTH = 7, BASE_HEIGHT = 12;

	private static Sprite[][] sprites;

	public static void  loadSprites() {
		sprites = new Sprite[3][characters.length];
		for (int size = 0; size < 3; size++) {
			try {
				BufferedImage source = ImageIO.read(new File("resources/text/text_" + size + ".png"));
				int width = BASE_WIDTH * (size + 1);
				int height = BASE_HEIGHT * (size + 1);
				for (int i = 0; i < characters.length; i++)
					sprites[size][i] = new Sprite(source.getSubimage(i * width, 0, width, height));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	private static Sprite getSprite(char character, Size size) {
		for (int i = 0; i < characters.length; i++)
			if (characters[i] == character)
				return sprites[size.ordinal()][i];
		return sprites[size.ordinal()][64];
	}

	private static void drawString(String string, int x, int y, Size size) {
		char[] charArray = string.toCharArray();
		for (int i = 0; i < charArray.length; i++)
			if (charArray[i] != ' ')
				getSprite(charArray[i], size).draw(x + i * BASE_WIDTH * (size.ordinal() + 1), y);
	}
	
	public static void drawString(String string, int x, int y, Size size, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
		if (horizontalAlignment == HorizontalAlignment.CENTER)
			x -= string.length() * BASE_WIDTH * (size.ordinal() + 1) / 2;
		else if (horizontalAlignment == HorizontalAlignment.RIGHT)
			x -= string.length() * BASE_WIDTH * (size.ordinal() + 1);
		if (verticalAlignment == VerticalAlignment.BOTTOM)
			y -= BASE_HEIGHT * (size.ordinal() + 1);
		drawString(string, x, y, size);
	}

}
