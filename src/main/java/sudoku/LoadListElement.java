package sudoku;

/*
 This class represents an element in the list in the load menu.
*/

public class LoadListElement {
	public String name;
	public int difficulty, size, innerSquareSize, numInnerSquares;
	public Field[][] board;
	
	//Constructor taking the necessary information.
	public LoadListElement(String name, int difficulty, int size, Field[][] board, int innerSquareSize, int numInnerSquares) {
		this.name = name;
		this.difficulty = difficulty;
		this.size = size;
		this.board = board;
		this.innerSquareSize = innerSquareSize;
		this.numInnerSquares = numInnerSquares;
	}
}