package main;

import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import render.Drawable;
import render.RectTextureSprite2D;
import render.Text;
import util.PhysicsObject;
import util.Vector;

public class View implements Runnable {
	private static final String TITLE = "MultiPlayerGame v. 1";

	private Model model;
	private Controller controller;

	private int windowWidth, windowHeight;

	public enum ScreenState {TITLE, CONTROLS, PLAYER1WIN, PLAYER2WIN, GAME};

	public ScreenState screenState = ScreenState.TITLE;

	private static final RectTextureSprite2D
	title = new RectTextureSprite2D(new Vector(0, 0), new Vector(0, 0), 0, "assets/textures/title.png", "PNG"),
	controls = new RectTextureSprite2D(new Vector(0, 0), new Vector(0, 0), 0, "assets/textures/controls.png", "PNG"),
	player1win = new RectTextureSprite2D(new Vector(0, 0), new Vector(0, 0), 0, "assets/textures/player1win.png", "PNG"),
	player2win = new RectTextureSprite2D(new Vector(0, 0), new Vector(0, 0), 0, "assets/textures/player2win.png", "PNG");

	public View(int width, int height) {
		windowWidth = width;
		windowHeight = height;
	}

	public View(){
		DisplayMode d = Display.getDesktopDisplayMode();
		windowWidth = d.getWidth();
		windowHeight = d.getHeight();
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void run() {
		/*
		 * Create a display
		 */

		try {

			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.setFullscreen(true);
			Display.setVSyncEnabled(true);
			Display.setTitle(TITLE);
			Display.create();
		} catch (LWJGLException e) {
			System.err.println("Error: Failed to create display");
			e.printStackTrace();
			System.exit(0);
		}
		
		// MASSIVE HACK, PLEASE KEEP MOVING
		Text.loadSprites();

		/*
		 * Initialize LWJGL and OpenGL
		 */
		{
			GL11.glViewport(0, 0, windowWidth, windowHeight);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		}

		try {
			int seconds = 90;
			while ( !(Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_Q))) {
				// Don't eat everything!!!
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (screenState == ScreenState.GAME) {

					setCamera();

					/*
					 * Draw Code
					 */
					synchronized (model.drawableObjects) {
						for (Drawable drawable : model.drawableObjects)
							drawable.draw();
					}

					setUICamera();

					int padding = 10;
					int jetpackWidth = 50;
					int jetpackHeight = 200;

					// PLAYER 1 JETPACK
					int player1jetpackHeight = (int) (model.player1.jetpackFuel / 400.0 * jetpackHeight);

					GL11.glTranslatef(padding, padding, 0);

					GL11.glColor3f(1f, 1f, 1f);

					GL11.glBegin(GL11.GL_QUADS);
					{
						GL11.glNormal3d(0, 0, 1);
						GL11.glVertex2d(0, 0);
						GL11.glVertex2d(jetpackWidth, 0);
						GL11.glVertex2d(jetpackWidth, jetpackHeight);
						GL11.glVertex2d(0, jetpackHeight);
					}
					GL11.glEnd();

					GL11.glColor3f(128/255f, 0/255f, 128/255f);

					GL11.glBegin(GL11.GL_QUADS);
					{
						GL11.glNormal3d(0, 0, 1);
						GL11.glVertex2d(0, jetpackHeight - player1jetpackHeight);
						GL11.glVertex2d(jetpackWidth, jetpackHeight - player1jetpackHeight);
						GL11.glVertex2d(jetpackWidth, jetpackHeight);
						GL11.glVertex2d(0, jetpackHeight);
					}
					GL11.glEnd();

					GL11.glTranslatef(-padding, -padding, 0);

					// PLAYER 2 JETPACK
					int player2jetpackHeight = (int) (model.player2.jetpackFuel / 400.0 * jetpackHeight);

					GL11.glTranslatef(windowWidth - jetpackWidth - padding, padding, 0);

					GL11.glColor3f(1f, 1f, 1f);

					GL11.glBegin(GL11.GL_QUADS);
					{
						GL11.glNormal3d(0, 0, 1);
						GL11.glVertex2d(0, 0);
						GL11.glVertex2d(jetpackWidth, 0);
						GL11.glVertex2d(jetpackWidth, jetpackHeight);
						GL11.glVertex2d(0, jetpackHeight);
					}
					GL11.glEnd();

					GL11.glColor3f(128/255f, 0/255f, 128/255f);

					GL11.glBegin(GL11.GL_QUADS);
					{
						GL11.glNormal3d(0, 0, 1);
						GL11.glVertex2d(0, jetpackHeight - player2jetpackHeight);
						GL11.glVertex2d(jetpackWidth, jetpackHeight - player2jetpackHeight);
						GL11.glVertex2d(jetpackWidth, jetpackHeight);
						GL11.glVertex2d(0, jetpackHeight);
					}
					GL11.glEnd();

					GL11.glTranslatef(-(windowWidth - jetpackWidth - padding), -padding, 0);
					
					// BULLET COUNT
					GL11.glEnable(GL11.GL_TEXTURE_2D);
					
					GL11.glColor3f(1f, 1f, 1f);
						
					Text.drawString("Bullets: " + model.player1.getAmmo(), 100, 10, Text.Size.LARGE, Text.HorizontalAlignment.LEFT, Text.VerticalAlignment.TOP);
					Text.drawString("Bullets: " + model.player2.getAmmo(), windowWidth - 100, 10, Text.Size.LARGE, Text.HorizontalAlignment.RIGHT, Text.VerticalAlignment.TOP);
					seconds = +(int)Math.floor(90-(System.currentTimeMillis() - controller.startTime)/1000);
					if( seconds == 0 ){
						if( model.player1.getKillCount() > model.player2.getKillCount() ){
							screenState = ScreenState.PLAYER1WIN;
						} else if( model.player1.getKillCount() < model.player2.getKillCount() ){
							screenState = ScreenState.PLAYER2WIN;
						} else {
							model.player1.setAmmo(99);
							model.player2.setAmmo(99);
							controller.startTime += 60000;
						}
					}
					Text.drawString("Time: "+seconds, windowWidth/2, 10, Text.Size.LARGE, Text.HorizontalAlignment.CENTER, Text.VerticalAlignment.TOP);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
				} else {
					setDylanCompatibleUICamera();
					if (screenState == ScreenState.TITLE) {
						title.setDimension(new Vector(windowWidth, windowHeight));
						title.draw();
					}  else if (screenState == ScreenState.CONTROLS) {
						controls.setDimension(new Vector(windowWidth, windowHeight));
						controls.draw();
					}  else if (screenState == ScreenState.PLAYER1WIN) {
						player1win.setDimension(new Vector(windowWidth, windowHeight));
						player1win.draw();
					}  else if (screenState == ScreenState.PLAYER2WIN) {
						player2win.setDimension(new Vector(windowWidth, windowHeight));
						player2win.draw();
					}
				}
				/*
				 * End draw code
				 */
				Display.update();
			}
		} catch (Exception e) {
			System.err.println("Uncaught exception in View main loop: " + e.getMessage()
					+ "\nCleaning up and exiting.");
			e.printStackTrace();
		}

		/*
		 * Clean up
		 */

		model.endGame();
		controller.endGame();
		Display.destroy();
	}

	/**
	 * Initialize the camera for the next frame of OpenGL.
	 */
	private void setCamera() {
		Vector viewTranslation = controller.getCameraPosition();

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		float whRatio = (float) windowWidth / (float) windowHeight;
		GLU.gluPerspective(controller.getFOV(), whRatio, 1, 100000);
		GLU.gluLookAt((float) viewTranslation.x, (float) viewTranslation.y,
				(float) controller.getCameraDistance(), (float) viewTranslation.x,
				(float) viewTranslation.y, 0, 0, 1, 0);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
	}

	private void setUICamera() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, windowWidth, windowHeight, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, windowWidth, windowHeight);
	}
	
	private void setDylanCompatibleUICamera() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, windowWidth, 0, windowHeight, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, windowWidth, windowHeight);
	}
}
