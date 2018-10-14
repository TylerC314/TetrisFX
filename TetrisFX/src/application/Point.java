package application;

public class Point {
	
	private int x;
	private int y;
	
	Point(int x, int y){
		setX(x);
		setY(y);
	}
	
	public int getX(){
		return x;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void addX(int value){
		this.x += value;
	}
	
	public void addY(int value){
		this.y += value;
	}
	
	public String toString() {
		return "X: " + x + " Y: " + y;
	}
}
