package main;

public class N64Controller{
	private double x;
	private double y;
	private boolean button5;
	private boolean button4;
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public boolean getButton5(){
		return button5;
	}
	
	public boolean getButton4(){
		return button4;
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
	
	public void setButton4(boolean v){
		button4 = v;
	}
}
