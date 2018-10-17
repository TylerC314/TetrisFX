package application;
	
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	int windowWidth = 200;
	int windowHeight = windowWidth * 11 / 5;
	static int rows = 22;
	static int columns = 10;
	int blockWidth = windowWidth / columns;
	int blockHeight = windowHeight / rows;
	Rectangle[][] gameBoard = new Rectangle[rows][columns];
	Tetromino currentShape;
	Group root = new Group();
	Group gameWindow = new Group();
	Group movingShape = new Group();
	BoardChecker checker = new BoardChecker(blockWidth, blockHeight);
	boolean isGameOver = false;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("TetrisFX");
		

		root.getChildren().add(gameWindow);
		root.getChildren().add(movingShape);
		
		currentShape = new Tetromino();
		drawShape(currentShape);
		
		AnimationTimer timer = new AnimationTimer() {

			long lastUpdate = 0;
			long gameSpeed = 1000_000_000;
			@Override
			public void handle(long now) {
				if(now - lastUpdate >= gameSpeed) {
					if(currentShape.canMoveDown(rows - 2, gameBoard)) {
						removeShape(currentShape);
						currentShape.moveDown();
						drawShape(currentShape);
					}
					else {
						gameWindow.getChildren().clear();
						movingShape.getChildren().clear();

						addToBoard(currentShape);
						checker.checkBoard(gameBoard);
						drawBoard(gameBoard, gameWindow);

						currentShape = new Tetromino();
						drawShape(currentShape);
						gameSpeed = getGameSpeed(checker.getLinesCleared());
					}

					lastUpdate = now;
				}				
			}};
			
			timer.start();
		
		Scene scene = new Scene(root, windowWidth, windowHeight);
		scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->{
			if(key.getCode() == KeyCode.A && currentShape.canMoveLeft(gameBoard)) {
				removeShape(currentShape);
				currentShape.moveLeft();
				drawShape(currentShape);
			}
			
			if(key.getCode() == KeyCode.D && currentShape.canMoveRight(columns - 1, gameBoard)) {
				removeShape(currentShape);
				currentShape.moveRight();
				drawShape(currentShape);
			}
			
			if(key.getCode() == KeyCode.S && currentShape.canMoveDown(rows - 2, gameBoard)) {
				removeShape(currentShape);
				currentShape.moveDown();
				drawShape(currentShape);
			}
			
			if(key.getCode() == KeyCode.W && currentShape.canRotate(gameBoard)) {
				removeShape(currentShape);
				currentShape.rotate();
				drawShape(currentShape);
				
			}
		});
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public long getGameSpeed(int linesCleared) {
		switch(linesCleared) {
		case 0 :
			return 1000_000_000;
		case 10:
			return 750_000_000;
		case 20:
			return 500_000_000;
		case 30:
			return 250_000_000;
		default:
			return 250_000_000;
		}
	}
	
	public void printBoard() {
		for(int i = 0; i < gameBoard.length; ++i) {
			for(int j = 0; j < gameBoard[i].length; ++j) {
				if(gameBoard[i][j] != null) {
					System.out.print(1);
				}
				else {
					System.out.print(0);
				}
			}
			System.out.println();
		}
		System.out.println("----------");
	}
	
	public void drawShape(Tetromino shape){
		for(int i = 0; i < shape.getCurrentPosLength(); ++i) {
			Rectangle square = new Rectangle(shape.getCurrentPosIndex(i).getX() * blockWidth,
											 shape.getCurrentPosIndex(i).getY() * blockHeight,
											 blockWidth, blockHeight);
			
			square.setFill(shape.getColor());
			square.setStroke(Color.BLACK);
			movingShape.getChildren().add(square);
		}
	}
	
	public void removeShape(Tetromino shape) {
		movingShape.getChildren().clear();
	}
	
	public void addToBoard(Tetromino shape) {
		for(int i = 0; i < shape.getCurrentPosLength(); ++i) {
			int column = shape.getCurrentPosIndex(i).getX();
			int row = shape.getCurrentPosIndex(i).getY();
			Rectangle square = new Rectangle(column * blockWidth, row * blockHeight, blockWidth, blockHeight);
			square.setFill(shape.getColor());
			square.setStroke(Color.BLACK);
			gameBoard[row][column] = square;
		}
	}
	
	public void setShapeOnBoard(Tetromino shape) {
		for(int i = 0 ; i < shape.getCurrentPosLength(); ++i) {
			int column = shape.getCurrentPosIndex(i).getX();
			int row = shape.getCurrentPosIndex(i).getY();
			Rectangle square = new Rectangle(column * blockWidth, row * blockHeight, blockWidth, blockHeight);
			square.setFill(shape.getColor());
			square.setStroke(Color.BLACK);
			gameBoard[row][column] = square;
			gameWindow.getChildren().add(gameBoard[row][column]);
		}
	}
	
	public void drawBoard(Rectangle[][] board, Group group) {
		for(int row = 0; row < board.length; row++) {
			for(int column = 0; column < board[row].length; column++) {
				if(board[row][column] != null) {
//					Rectangle square = new Rectangle(column * blockWidth, row * blockHeight, blockWidth, blockHeight);
//					square.setFill(board[row][column].getFill());
//					square.setStroke(board[row][column].getStroke());
//					board[row][column] = square;
					group.getChildren().add(board[row][column]);
				}
			}
		}
	}
}