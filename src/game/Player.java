package game;
import util.PhysicsObject;
import util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import render.ColorSprite2D;
import render.RectTextureSprite2D;

import java.awt.*;

import main.Model;
import main.N64Controller;

public class Player implements util.PhysicsObject, render.Drawable, game.Timed {
	private Vector currentPosition;
	private Vector oldPosition;
	private String currentItem;
	private N64Controller controller;
	private boolean isOnGround;
	private boolean isOnWall;
	private Vector playerSize;
	private Model model;
	private double walkCycle;
	private int timer;
	private boolean isLeftWall;
	private int lastFired;
	private int directionFacing;
	private boolean facingDown;
	public RectTextureSprite2D currentSprite;
	private int killCount;
	public int jetpackFuel;
	private int ammo;
	
	//Sprites:
	public static RectTextureSprite2D crownSprite;
	
	public static RectTextureSprite2D player1Punch;
	public static RectTextureSprite2D player2Punch;
	public static RectTextureSprite2D player1Stand;
	public static RectTextureSprite2D player2Stand;
	public static RectTextureSprite2D player1Jump;
	public static RectTextureSprite2D player2Jump;
	public static RectTextureSprite2D player1WallJump;
	public static RectTextureSprite2D player2WallJump;
	public static RectTextureSprite2D player1Gun;
	public static RectTextureSprite2D player2Gun;
	public static RectTextureSprite2D player1GunDown;
	public static RectTextureSprite2D player2GunDown;

	public static RectTextureSprite2D player1Walk0;
	public static RectTextureSprite2D player1Walk1;
	public static RectTextureSprite2D player1Walk2;
	public static RectTextureSprite2D player2Walk0;
	public static RectTextureSprite2D player2Walk1;
	public static RectTextureSprite2D player2Walk2;
	
	public static RectTextureSprite2D player1Jet0;
	public static RectTextureSprite2D player1Jet1;
	public static RectTextureSprite2D player1Jet2;
	public static RectTextureSprite2D player1Jet3;
	public static RectTextureSprite2D player2Jet0;
	public static RectTextureSprite2D player2Jet1;
	public static RectTextureSprite2D player2Jet2;
	public static RectTextureSprite2D player2Jet3;
	
	public void setModel(Model m){
		model = m;
	}
	//private int killCount;
	//private List<String> items;
	//private int mapWidth;
	//private int mapHeight;
	
	//passed a valid starting position vector
	//creates player with no kills and no items, at given vector
	public Player(Vector starting){
		//killCount = 0;
		//items = new ArrayList<String>();
		oldPosition = starting;
		currentPosition = starting;
		playerSize = new Vector(22, 31);
		timer = 0;
		directionFacing = 1;
		ammo = 2;
	}
	

	//returns player's old position
	public Vector getOldPosition(){
		return oldPosition;
	}
	
	//sets old position
	public void setOldPosition(Vector position){
		oldPosition = position;
	}
	
	//returns player's current position
	public Vector getPosition(){
		return currentPosition;
	}
	
	public Vector getAcceleration(){
		Vector accel = new Vector(0,-12);
		
		if( isOnGround ){
			accel.x = controller.getX()*3;
		} else if ( controller.getButtonJump() && jetpackFuel > 0 ){
			accel.x = controller.getX()*12;
		} else {
			accel.x = controller.getX()*1;
		}
		if( controller.getButtonJump() && jetpackFuel > 0 ){
			accel.y = 10;
		}
		return accel;
	}
	
	public void draw(){
		if( getKillCount() > model.player1.getKillCount() || getKillCount() > model.player2.getKillCount() ){
			crownSprite.setPosition(currentPosition.plus(new Vector(1,34)));
			crownSprite.draw();
		}
		currentSprite.setPosition(currentPosition);
		currentSprite.draw(directionFacing==1);
	}
	
	public void step(double timestep){
		if( jetpackFuel < 0 ) jetpackFuel = 0;
		if( isOnWall && isOnGround ) isOnWall = false;
		getDirectionFacing();
		lastFired--;
		if( controller.getTrigger() ){
			System.out.println(ammo);
			if( ammo > 0 ){
				ammo--;
				Projectile p = new Projectile("bullet", currentPosition.plus(new Vector(playerSize.x /2 + (facingDown?0:directionFacing*7.5), playerSize.y / 2)));
				if( facingDown ){
					p.setOldPosition(p.getPosition().minus(new Vector(0,-model.physics.PLAYER_MAX_X_SPEED*3)));
					p.getSize().x = 2;
					p.getSize().y = 15;
				} else {
					lastFired = 100;
					p.setOldPosition(p.getPosition().minus(new Vector(directionFacing*model.physics.PLAYER_MAX_X_SPEED*3, 0)));
				}
				p.myTarget = (this==model.player1)?model.player2:model.player1;
				p.setDirection(directionFacing);
				model.physics.movingObjects.add(p);
				model.thingsToAdd.add(p);
			} else {
				//TODO: Punch
			}
		}
		if (isOnGround()) {
			if( jetpackFuel < 400 ) jetpackFuel++;
			
			if( controller.getButtonJetpack() ){
				this.oldPosition.y -= .2;
				timer = 200;
			}
		} else if( isOnWall && false ) {
			//this.directionFacing = isLeftWall?1:-1;
			//oldPosition.addInPlace(new Vector(0, (currentPosition.y - oldPosition.y)*.01)); 
			/*if( controller.getButtonJetpack() && false ){
				this.oldPosition.y = currentPosition.y - .1;
				this.oldPosition.x += .05 * -directionFacing;
				timer = 200;
			}*/
		}
		else {
			if( timer > 0 && controller.getButtonJetpack() ){
				this.oldPosition.y -= .000025*timer;
			} else {
				timer = 0;
			}
			timer--;
		}
		if ( controller.getButtonJump() ) {
			jetpackFuel--;
			if( this == model.player1 ){
				int r = Model.random.nextInt(4);
				if( r == 0 ){
					currentSprite = player1Jet0;
				} else if(r == 1){
					currentSprite = player1Jet1;
				} else if(r == 2){
					currentSprite = player1Jet2;
				} else {
					currentSprite = player1Jet3;
				}
			} else {
				int r = Model.random.nextInt(4);
				if( r == 0 ){
					currentSprite = player2Jet0;
				} else if(r == 1){
					currentSprite = player2Jet1;
				} else if(r == 2){
					currentSprite = player2Jet2;
				} else {
					currentSprite = player2Jet3;
				}
			}
		} else if ( lastFired > 0 ) {
			System.out.println("didFire");
			if( this == model.player1 ){
				currentSprite = player1Gun;
			} else {
				currentSprite = player2Gun;
			}
		} else if( isOnGround ){
			double speed = Math.abs(this.currentPosition.x - this.oldPosition.x);
			if( speed > 0 ){
				walkCycle += speed/16;
				int w = ((int)walkCycle)%3;
				if( this == model.player1 ){
					if( w==0 ){
						currentSprite = player1Walk0;
					} else if( w==1 ){
						currentSprite = player1Walk1;
					} else {
						currentSprite = player1Walk2;
					}
				} else {
					if( w==0 ){
						currentSprite = player2Walk0;
					} else if( w==1 ){
						currentSprite = player2Walk1;
					} else {
						currentSprite = player2Walk2;
					}
				}
			}
		}  else if( facingDown ) {
			if( this == model.player1 ){
				currentSprite = player1GunDown;
			} else {
				currentSprite = player2GunDown;
			}
		} else if( isOnWall && false ){
			if( this == model.player1 ){
				currentSprite = player1WallJump;
			} else {
				currentSprite = player2WallJump;
			}
		} else {
			if( this == model.player1 ){
				currentSprite = player1Jump;
			} else {
				currentSprite = player2Jump;
			}
		}
	}
	
	public int getAmmo(){
		return ammo;
	}
	
	public void setAmmo(int value){
		ammo = value;
	}
	
	public void setController(N64Controller controller){
		this.controller = controller;
	}
	//return the player's size in the form (width, height)
	public Vector getSize(){
		return playerSize;
	}
	
	//return whether the player is on the ground
	public boolean isOnGround(){
		return isOnGround;
	}
	
	//sets whether the player is on the ground
	public void setIsOnGround(boolean ground){
		isOnGround = ground;
		if( ground ) isOnWall = false;
	}
	
	public boolean isOnWall(){
		return isOnWall;
	}
	
	public void setIsOnWall(boolean value){
		isOnWall = value;
	}
	
	public boolean removeMe(){
		return false;
	}
	
	//-1 = left
	//0 = neutral
	//1 = right
	public int getDirectionFacing(){
		if( controller.getY() == 1 && !isOnGround ){
			facingDown = true;
		} else if( controller.getY() == 1 ) {
			this.currentPosition.y--;
			this.oldPosition.y--;
		} else {
			facingDown = false;
		}
		if (controller.getX() < 0 && directionFacing != -1){
			directionFacing = -1;
			return -1;
		}
		else if (controller.getX() > 0 && directionFacing != 1){
			directionFacing = 1;
			return 1;
		}
		return directionFacing;
	}
	
	public Vector collide(PhysicsObject a){
		return null;
	}
	

	public void setIsLeftWall(boolean value){
		isLeftWall = value;
	}

//returns current number of kills this player has
public int getKillCount(){
	return killCount;
}

//adds 1 to the player's score
public void addKill(){
	killCount++;
}

//removes 1 from the player's score
//can be used if the player suicides
public void removeKill(){
	killCount--;
}
}
