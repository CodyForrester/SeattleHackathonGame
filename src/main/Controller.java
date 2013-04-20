package main;

import main.View.ScreenState;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Controllers;

import audio.AudioPlayer;

import util.Vector;
import util.Vector3D;

public class Controller implements Runnable {
	public View view;
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
	public long startTime;
	private boolean gameRunning = true;
	private Model model;

	private Vector cameraPosition;
	private double cameraDistance;

	public org.lwjgl.input.Controller Player1;
	public org.lwjgl.input.Controller Player2;

	private float FOV;

	public Controller() {
		cameraPosition = new Vector(0, 0);
		cameraDistance = 1000;
		FOV = 45.0f;
		try {
			Controllers.create();
			Player1 = Controllers.getController(0);
			Player2 = Controllers.getController(1);
			Controllers.poll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setModel(Model model) {
		this.model = model;
	}



	public void run() {
		while (!Keyboard.isCreated());
		AudioPlayer.play(AudioPlayer.MUSIC);
		boolean goodValues = false;
		while(!goodValues){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			goodValues = 	Player1.getXAxisValue() != -1 &&
					Player2.getXAxisValue() != -1 &&
					Player1.getYAxisValue() != -1 &&
					Player2.getYAxisValue() != -1;
		}
		System.out.println("Here.");
		while( view.screenState == ScreenState.TITLE ){
			while(Controllers.next()){
				System.out.println("Here here.");
				view.screenState = ScreenState.GAME;
			}
		}
		startTime = System.currentTimeMillis();
		while (gameRunning) {

			while (Controllers.next()) {
				if (Controllers.isEventButton()) {
					if (Controllers.getEventSource() == Player1) {
						if (Controllers.getEventControlIndex() == 0 && Player1.isButtonPressed(0)) {
							// p1 shoot
							AudioPlayer.play(AudioPlayer.SHOOT);
						} else if (Controllers.getEventControlIndex() == 8 && Player1.isButtonPressed(8)) {
							//p1 trigger
							model.player1Controller.trigger(true);
							if( model.player1.getAmmo() == 0 ){
								AudioPlayer.play(AudioPlayer.PUNCH);
							} else {
								AudioPlayer.play(AudioPlayer.SHOOT);
							}
						}
					} else if (Controllers.getEventSource() == Player2) {
						if (Controllers.getEventControlIndex() == 0 && Player2.isButtonPressed(0)) {
							// p2 shoot
							AudioPlayer.play(AudioPlayer.SHOOT);
						} else if (Controllers.getEventControlIndex() == 8 && Player2.isButtonPressed(8)) {
							//p2 trigger
							model.player2Controller.trigger(true);
							if( model.player2.getAmmo() == 0 ){
								AudioPlayer.play(AudioPlayer.PUNCH);
							} else {
								AudioPlayer.play(AudioPlayer.SHOOT);
							}
						}
					}
				}
			}

			/*
			 * Camera Controls
			 */
			model.player1Controller.setX(Player1.getXAxisValue());
			model.player1Controller.setY(Player1.getYAxisValue());
			model.player1Controller.setButtonJump(Player1.isButtonPressed(N64Controller.B.getID()));
			model.player1Controller.setButtonJetpack(Player1.isButtonPressed(N64Controller.A.getID()));
			model.player1Controller.setR(Player1.isButtonPressed(N64Controller.R.getID()));

			model.player2Controller.setX(Player2.getXAxisValue());
			model.player2Controller.setY(Player2.getYAxisValue());
			model.player2Controller.setButtonJump(Player2.isButtonPressed(N64Controller.B.getID()));
			model.player2Controller.setButtonJetpack(Player2.isButtonPressed(N64Controller.A.getID()));
			model.player2Controller.setR(Player2.isButtonPressed(N64Controller.R.getID()));

			//cameraDistance += Mouse.getDWheel() * cameraDistance / 1000;
			//cameraDistance += (Player1.isButtonPressed(N64Controller.CUp.getID())?-.0005:0) * cameraDistance / 1000;
			//cameraDistance += (Player1.isButtonPressed(N64Controller.CDown.getID())?.0005:0) * cameraDistance / 1000;
			//cameraDistance += (Player2.isButtonPressed(N64Controller.CUp.getID())?-.0005:0) * cameraDistance / 1000;
			//cameraDistance += (Player2.isButtonPressed(N64Controller.CDown.getID())?.0005:0) * cameraDistance / 1000;
			Vector p1 = model.player1.getPosition();
			Vector p2 = model.player2.getPosition();
			Vector s1 = model.player1.getSize();
			Vector s2 = model.player2.getSize();
			Vector bottomLeft = new Vector(Math.min(p1.x, p2.x) - 50, Math.min(p1.y, p2.y) - 50);
			Vector topRight = new Vector(Math.max(p1.x+s1.x, p2.x+s2.x) + 50, Math.max(p1.y+s1.y, p2.y+s2.y) + 50);
			topRight.subtractInPlace(bottomLeft);
			Vector cameraGoal = bottomLeft.plus(topRight.scale(0.5));
			cameraPosition.addInPlace(cameraGoal.minus(cameraPosition).scale(.0000001));
			double goal = topRight.mag() * 1.2;
			
			cameraDistance += (goal-cameraDistance)/10000000;
			if (cameraDistance < 300)
				cameraDistance = 300;
			if (cameraDistance > 2500)
				cameraDistance = 2500;

			double cameraSpeed = cameraDistance / 10000000.0;

			//cameraPosition.addInPlace(new Vector(Player1.getXAxisValue()*cameraSpeed, -Player1.getYAxisValue()*cameraSpeed));
			//cameraPosition.addInPlace(new Vector(Player2.getXAxisValue()*cameraSpeed, -Player2.getYAxisValue()*cameraSpeed));
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
