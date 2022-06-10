package sudoku;

/*
 This class is used to make an object of loaded '.su' files. When for example in LoadGameScreen.java
 we want to show all loadable elements in a box we use this class to generate those files and save them.
*/

public class LoadListElement {
	public String name;
	public int difficulty, size, innerSquareSize, numInnerSquares;
	public Field[][] board;
	
	public LoadListElement(String name, int difficulty, int size, Field[][] board, int innerSquareSize, int numInnerSquares) {
		this.name = name;
		this.difficulty = difficulty;
		this.size = size;
		this.board = board;
		this.innerSquareSize = innerSquareSize;
		this.numInnerSquares = numInnerSquares;
	}
}