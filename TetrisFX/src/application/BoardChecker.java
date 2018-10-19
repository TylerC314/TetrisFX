package application;

import javafx.scene.shape.Rectangle;

public class BoardChecker {
	
	private int rowCounter = 0;
	private int firstRow = 0;
	private int blockWidth = 0;
	private int blockHeight = 0;
	private int linesCleared = 0;
	
	public BoardChecker(int blockWidth, int blockHeight) {
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;
	}
	
	public void checkBoard(Rectangle[][] board) {
		for(int i = 0; i < board.length; ++i) {
			if(rowIsFull(board, i)) {
				clearRow(board, i);
				if(linesCleared <= 30) {
					++linesCleared;
				}
				else {
					continue;
				}

				++rowCounter;
				if(firstRow == 0) {
					firstRow = i;
				}
			}
		}
		
		for(int i = firstRow - 1; i > 0; --i) {
			transferRow(board, i + rowCounter, i);
		}
		rowCounter = 0;
		firstRow = 0;
	}
	
	public boolean rowIsFull(Rectangle[][] board, int row) {
		for(int i = 0; i < board[row].length; ++i) {
			if(board[row][i] == null) {
				return false;
			}
		}
		return true;
	}
	
	public void transferRow(Rectangle[][] board, int row1, int row2) {
		for(int column = 0; column < board[row1].length; ++column) {
			if(board[row2][column] != null) {
				Rectangle block = board[row2][column];
				Rectangle replacementBlock = new Rectangle(column * blockWidth + 200, row1 * blockHeight, blockWidth, blockHeight);
				replacementBlock.setFill(block.getFill());
				replacementBlock.setStroke(block.getStroke());
				board[row1][column] = replacementBlock;
			}
			else {
				board[row1][column] = board[row2][column];
			}


		}
	}
	
	public void clearRow(Rectangle[][] board, int row) {
		for(int i = 0; i < board[row].length; ++i) {
			board[row][i] = null;
		}
	}
	
	public int getLinesCleared() {
		return linesCleared;
	}
	
	public void setLinesCleared(int lines) {
		linesCleared = lines;
	}
}
