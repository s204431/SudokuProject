package sudoku;

import java.util.List;

import solvers.*;

public class Model {
	private View view;
	public Field[][] board;
	public int innerSquareSize; //Width/height an inner square.
	
	public Model(int innerSquareSize) {
		this.innerSquareSize = innerSquareSize;
		board = new Field[getBoardSize()][getBoardSize()];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = new Field();
			}
		}
		//board[5][6].value = 2;
		//board[3][8].value = 3;
		//board[5][6].interactable = false;
		//System.out.println(new BacktrackingSolver(board).canBePlaced(4, 6, 3));
		//System.out.println(board[3][8].value);
		//System.out.println(new BacktrackingSolver(board).solve());
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public int getBoardSize() {
		return innerSquareSize*innerSquareSize;
	}
	
	public void setField(int x, int y, Field field) {
		board[x][y] = field;
		if (sudokuSolved(board)) {
			System.out.println("Solved!");
		}
		view.repaint();
	}
	
	public void setField(int x, int y, int value) {
		board[x][y].value = value;
		if (sudokuSolved(board)) {
			System.out.println("Solved!");
		}
		view.repaint();
	}
	
	//Checks if the current sudoku is solved. Takes a 2D integer array.
	public boolean sudokuSolved(int[][] board) {
		Field[][] toBeSolved = new Field[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				toBeSolved[i][j] = new Field(board[i][j], true);
			}
		}
		return sudokuSolved(toBeSolved);
	}
	
	//Checks if the current sudoku is solved. Takes a 2D field array.
	public boolean sudokuSolved(Field[][] board) {
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
	
	public void solve() {
		solve(Integer.MAX_VALUE);
	}
	
	public void solve(int maxSolutions) {
		List<int[][]> results = new BacktrackingSolver(board, this).solve(maxSolutions);
		System.out.println("Found " + results.size() + " solutions.");
		if (results.size() > 0) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					setField(i, j, results.get(0)[i][j]);
				}
			}
		}
	}
	public int getInnerSquareSize(){
		return innerSquareSize;
	}
}
