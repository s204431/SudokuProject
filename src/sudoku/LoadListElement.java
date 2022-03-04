package sudoku;

public class LoadListElement {
	public String name;
	public int difficulty;
	public int size;
	public Field[][] board;
	public int innerSquareSize;
	public int numInnerSquares;
	
	public LoadListElement(String name, int difficulty, int size, Field[][] board, int innerSquareSize, int numInnerSquares) {
		this.name = name;
		this.difficulty = difficulty;
		this.size = size;
		this.board = board;
		this.innerSquareSize = innerSquareSize;
		this.numInnerSquares = numInnerSquares;
	}
}