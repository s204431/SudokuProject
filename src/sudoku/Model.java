package sudoku;

public class Model {
	private View view;
	public Field[][] board;
	public int innerSquareSize; //Width/height an inner square.
	public int boardX = 0; //x coordinate for top left corner.
	public int boardY = 0; //y coordinate for top left corner.
	
	public Model(int innerSquareSize) {
		this.innerSquareSize = innerSquareSize;
		board = new Field[getBoardSize()][getBoardSize()];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = new Field();
			}
		}
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public int getBoardSize() {
		return innerSquareSize*innerSquareSize;
	}
	
	public void setField(int x, int y, Field field) {
		board[x][y] = field;
		view.repaint();
	}
	
	public void setField(int x, int y, int value) {
		board[x][y].value = value;
		view.repaint();
	}
	
	public boolean sudokuSolved() {
		int boardSize = getBoardSize();
		boolean[][] foundColumn = new boolean[boardSize][boardSize];
		boolean[][] foundRow = new boolean[boardSize][boardSize];
		boolean[][] foundInnerSquare = new boolean[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j].value < 1 || board[i][j].value > boardSize) {
					return false;
				}
				else {
					foundRow[i][board[i][j].value-1] = true;
					foundColumn[j][board[i][j].value-1] = true;
					foundInnerSquare[(i/innerSquareSize)*innerSquareSize+(j/innerSquareSize)][board[i][j].value-1] = true;
				}
			}
		}
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (!foundRow[i][j] || !foundColumn[i][j] || !foundInnerSquare[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
}
