package sudoku;

public class Model {
	private View view;
	public Field[][] board;
	public int innerSquareSize; //Width/height an inner square.
	public int boardX; //x coordinate for top left corner.
	public int boardY; //y coordinate for top left corner.
	
	public Model(int innerSquareSize) {
		this.innerSquareSize = innerSquareSize;
		board = new Field[getBoardSize()][getBoardSize()];
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public int getBoardSize() {
		return innerSquareSize*innerSquareSize;
	}
	
	public void setField(int x, int y, Field field) {
		board[x][y] = field;
	}
}
