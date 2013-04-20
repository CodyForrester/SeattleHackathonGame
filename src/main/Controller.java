package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Controllers;

import util.Vector;
import util.Vector3D;

public class Controller implements Runnable {
	
	private enum N64Controller{
		CUp (0),
		CRight (1),
		CDown (2),
		CLeft (3),
		B (4),
		A (5),
		L (6),
		R (7),
		Z (8),
		Start (9);
		
		int value;
		N64Controller(int value){
			this.value = value;
		}
		
		public int getID(){
			return value;
		}
	}
	
	private boolean gameRunning = true;
	private Model model;

	private Vector cameraPosition;
	private double cameraDistance;
	
	private org.lwjgl.input.Controller Player1;
	private org.lwjgl.input.Controller Player2;
	
	private float FOV;

	public Controller() {
		cameraPosition = new Vector(0, 0);
		cameraDistance = 100;
		FOV = 45.0f;
		try {
			Controllers.create();
			Player1 = Controllers.getController(0);
			Player2 = Controllers.getController(1);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	

	public void run() {
		while (!Keyboard.isCreated());
		
		while (gameRunning) {
			/*
			 * Camera Controls
			 */
			cameraDistance += Mouse.getDWheel() * cameraDistance / 1000;
			cameraDistance += (Player1.isButtonPressed(N64Controller.CUp.getID())?-.0005:0) * cameraDistance / 1000;
			cameraDistance += (Player1.isButtonPressed(N64Controller.CDown.getID())?.0005:0) * cameraDistance / 1000;
			cameraDistance += (Player2.isButtonPressed(N64Controller.CUp.getID())?-.0005:0) * cameraDistance / 1000;
			cameraDistance += (Player2.isButtonPressed(N64Controller.CDown.getID())?.0005:0) * cameraDistance / 1000;
			if (cameraDistance < 20)
				cameraDistance = 20;
			if (cameraDistance > 50000)
				cameraDistance = 50000;
			
			double cameraSpeed = cameraDistance / 10000000.0;
			
			cameraPosition.addInPlace(new Vector(Player1.getXAxisValue()*cameraSpeed, -Player1.getYAxisValue()*cameraSpeed));
			cameraPosition.addInPlace(new Vector(Player2.getXAxisValue()*cameraSpeed, -Player2.getYAxisValue()*cameraSpeed));
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				cameraPosition.addInPlace(new Vector(-cameraSpeed, 0));
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				cameraPosition.addInPlace(new Vector(cameraSpeed, 0));
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				cameraPosition.addInPlace(new Vector(0, -cameraSpeed));
			if (Keyboard.isKeyDown(Keyboard.KEY_UP))
				cameraPosition.addInPlace(new Vector(0, cameraSpeed));
		}

		/*
		 * Clean up
		 */
	}
	
	private Vector pickPointOnScreen(Vector screenPixel) {
		Vector3D cameraForwards = new Vector3D(0, 0, -1);
		Vector3D cameraRight = new Vector3D(1, 0, 0);
		Vector3D cameraUp = new Vector3D(0, 1, 0);

		double screenX = screenPixel.x - Display.getWidth() / 2;
		double screenY = screenPixel.y - Display.getHeight() / 2;

		Vector3D screenVector = cameraForwards
				.scale((float) (Display.getHeight() / (2 * Math.tan(FOV / 360 * Math.PI))))
				.add(cameraRight.scale((float) screenX))
				.add(cameraUp.scale((float) screenY));

		double distScale = cameraDistance / screenVector.getZ();
		screenVector = screenVector.scale(-(float) distScale);
		return cameraPosition.plus(new Vector(screenVector.getX(),
				screenVector.getY()));
	}

	public void endGame() {
		gameRunning = false;
	}

	public Vector getCameraPosition() {
		return cameraPosition;
	}

	public double getCameraDistance() {
		return cameraDistance;
	}
	
	public float getFOV() {
		return FOV;
	}
}
