package sudoku;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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
		System.out.println("Found "+results.size()+" solutions.");
		if (results.size() > 0) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					setField(i, j, results.get(0)[i][j]);
				}
			}	
		}
	}
	
	public void save(String fileName) {
		File file = new File(fileName+".su");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(innerSquareSize+";"+innerSquareSize+"\n");
			for (int i = 0; i < getBoardSize(); i++) {
				for (int j = 0; j < getBoardSize(); j++) {
					if (board[i][j].value <= 0) {
						writer.write(".");
					}
					else {
						writer.write(""+board[i][j].value);
					}
					if (j < getBoardSize()-1) {
						writer.write(";");
					}
				}
				if (i < getBoardSize()-1) {
					writer.write("\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(String fileName) {
		File file = new File(fileName+".su");
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(";|\\n");
			innerSquareSize = scanner.nextInt();
			board = new Field[getBoardSize()][getBoardSize()];
			scanner.nextInt();
			for (int i = 0; i < getBoardSize(); i++) {
				for (int j = 0; j < getBoardSize(); j++) {
					String next = scanner.next();
					if (next.equals(".")) {
						board[i][j] = new Field(0, true);
					}
					else {
						board[i][j] = new Field(Integer.parseInt(next), true);
					}
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
