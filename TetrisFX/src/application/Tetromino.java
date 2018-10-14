package application;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tetromino {
	
	private Random rand = new Random();
	private Point[] coords;
	private Point[] currentPos;
	private Point startingPoint;
	private Color color;
	
	private Point[][] shapes = {{new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(-1, 1)},	//0. S-Shape
			  {new Point(0, 0), new Point(-1, 0), new Point(0, 1), new Point(1, 1)},	//1. Z-Shape
			  {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(1, 1)},	//2. L-Shape
			  {new Point(0, 0), new Point(-1, 0), new Point(-1, 1), new Point(1, 0)},	//3. J-Shape
			  {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(2, 0)},	//4. I-Shape
			  {new Point(0, 0), new Point(1, 0), new Point(0, 1), new Point(1, 1)},	//5. Square-Shape
			  {new Point(0, 0), new Point(-1, 0), new Point(1, 0), new Point(0, 1)}};	//6. T-Shape
	
	public Tetromino() {
		int selector = rand.nextInt(7);
		this.startingPoint = new Point(5, 0);
		this.coords = shapes[selector];
		this.color = setColor(selector);
		setCurrentPos();
	}
	
	public Tetromino(Tetromino other) {
		this.startingPoint = other.startingPoint;
		this.coords = other.coords;
		this.currentPos = other.currentPos;
		this.color = other.color;
	}
	
	public Point[] getCurrentPos() {
		return currentPos;
	}
	
	public Point getCurrentPosIndex(int index) {
		return currentPos[index];
	}
	
	public int getCurrentPosLength() {
		return currentPos.length;
	}
	
	public Point[] getCoords() {
		return coords;
	}
	
	public Color getColor() {
		return color;
	}
	
	private void setCurrentPos() {
		currentPos = new Point[coords.length];
		for(int i = 0; i < coords.length; ++i) {
			int x = startingPoint.getX() + coords[i].getX();
			int y = startingPoint.getY() + coords[i].getY();
			currentPos[i] = new Point(x, y);
		}
	}
	
	public void setCurrentPos(Point[] newPos) {
		currentPos = newPos;
	}
	
	public void moveDown() {
		for(int i = 0; i < currentPos.length; ++i) {
			currentPos[i].addY(1);
		}
	}
	
	public boolean canMoveDown(int bounds, Rectangle[][] board) {
		for(int i = 0; i < currentPos.length; ++i) {
			if(currentPos[i].getY() > bounds) {
				return false;
			}
			
			if(board[currentPos[i].getY() + 1][currentPos[i].getX()] != null) {
				return false;
			}
		}
		return true;
	}
	
	public void moveLeft() {
		for(int i = 0; i < currentPos.length; ++i) {
			currentPos[i].addX(-1);
		}
	}
	
	public boolean canMoveLeft(Rectangle[][] board) {
		for(int i = 0; i < currentPos.length; ++i) {
			if(currentPos[i].getX() <= 0) {
				return false;
			}
			if(board[currentPos[i].getY()][currentPos[i].getX() - 1] != null) {
				return false;
			}
		}
		
		return true;
	}
	
	public void moveRight() {
		for(int i = 0; i < currentPos.length; ++i) {
			currentPos[i].addX(1);
		}
	}
	
	public boolean canMoveRight(int bound, Rectangle[][] board) {
		for(int i = 0; i < currentPos.length; ++i) {
			if(currentPos[i].getX() >= bound) {
				return false;
			}
			if(board[currentPos[i].getY()][currentPos[i].getX() + 1] != null) {
				return false;
			}
			
		}
		
		return true;
	}
	
	public void rotate() {
		if(this.coords == shapes[5]) {
			return;
		}
		else {
			Point[] rotatedCoordinates = new Point[currentPos.length];
			for(int i = 0; i < currentPos.length; ++i) {
				Point translatedCoordinate = new Point(currentPos[i].getX() - currentPos[0].getX(), currentPos[i].getY() - currentPos[0].getY());
				int x = 0;
				int y = 0;
				y = translatedCoordinate.getY() * -1;
				translatedCoordinate.setY(y);
				
				rotatedCoordinates[i] = translatedCoordinate;
				
				x = (int)Math.round(translatedCoordinate.getY() * + 1);
				y = (int)Math.round(translatedCoordinate.getX() * -1);
				rotatedCoordinates[i].setX(x);
				rotatedCoordinates[i].setY(y);
				
				y = translatedCoordinate.getY() * -1;
				rotatedCoordinates[i].setY(y);
				
				x = rotatedCoordinates[i].getX() + currentPos[0].getX();
				y = rotatedCoordinates[i].getY() + currentPos[0].getY();
				rotatedCoordinates[i].setX(x);
				rotatedCoordinates[i].setY(y);
			}
			this.currentPos = rotatedCoordinates;
		}
		
	}
	
	public boolean canRotate(Rectangle[][] board) {
		Tetromino testTetromino = new Tetromino(this);
		testTetromino.rotate();
		for(int i = 0; i < testTetromino.getCurrentPosLength(); ++i) {
			int x = testTetromino.getCurrentPosIndex(i).getX();
			int y = testTetromino.getCurrentPosIndex(i).getY();
			
			//Out of bounds horizontally
			if(x < 0 || x > board[i].length - 1) {
				return false;
			}
			
			//Out of bounds vertically
			if(y < 0 || y > board.length - 2) {
				return false;
			}
			
			//Other block in the way
			if(board[y][x] != null) {
				return false;
			}
		}
		
		return true;
	}
	
	public Color setColor(int value) {
		switch(value) {
		case 0 : 
			return Color.BLUE;
		case 1 :
			return Color.YELLOW;
		case 2 :
			return Color.LIGHTGREEN;
		case 3 :
			return Color.PURPLE;
		case 4:
			return Color.LIGHTBLUE;
		case 5:
			return Color.RED;
		case 6:
			return Color.ORANGE;
		default :
			return null;
		}
	}

}
