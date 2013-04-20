package main;

public class N64Controller{
	private double x;
	private double y;
	private boolean button5;
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public boolean getButton5(){
		return button5;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public void setButton5(boolean v){
		button5 = v;
	}
}
