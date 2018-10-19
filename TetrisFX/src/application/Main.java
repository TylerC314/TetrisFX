package application;
	
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	int windowWidth = 400;
//	int windowHeight = windowWidth * 11 / 5;
	int windowHeight = 440;
	static int rows = 22;
	static int columns = 10;
	int blockWidth = (windowWidth - 200) / columns;
	int blockHeight = windowHeight / rows;
	Rectangle[][] gameBoard = new Rectangle[rows][columns];
	Tetromino currentShape;
	Group root = new Group();
	Group gameWindow = new Group();
	Group movingShape = new Group();
	BoardChecker checker = new BoardChecker(blockWidth, blockHeight);
	boolean isGameOver = false;
	AnimationTimer timer;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("TetrisFX");
		
		root.getChildren().add(gameWindow);
		root.getChildren().add(movingShape);
		
		Line screenSplitter = new Line(windowWidth / 2, 0, windowWidth / 2, windowHeight);
		root.getChildren().add(screenSplitter);
		
		currentShape = new Tetromino();
		drawShape(currentShape);
		
		timer = new AnimationTimer() {

			long lastUpdate = 0;
			long gameSpeed = getGameSpeed(checker.getLinesCleared());
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
		if(linesCleared >= 10) {
			return 750_000_000;
		}
		else if(linesCleared >= 20) {
			return 500_000_000;
		}
		else if(linesCleared >= 30){
			return 250_000_000;
		}
		else {
			return 1000_000_000;
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
			Rectangle square = new Rectangle(shape.getCurrentPosIndex(i).getX() * blockWidth + 200,
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
			Rectangle square = new Rectangle(column * blockWidth + 200, row * blockHeight, blockWidth, blockHeight);
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
					group.getChildren().add(board[row][column]);
				}
			}
		}
	}
}