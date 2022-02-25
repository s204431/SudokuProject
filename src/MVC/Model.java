package MVC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import solvers.*;
import sudoku.Field;
import Generators.*;


public class Model {
	private View view;
	public Field[][] board;
	public int innerSquareSize; //Width/height an inner square.
	public enum Mode {play, create, solver};
	public Mode mode = Mode.play;

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
	public static boolean sudokuSolved(int[][] board) {
		Field[][] toBeSolved = new Field[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				toBeSolved[i][j] = new Field(board[i][j], true);
			}
		}
		return sudokuSolved(toBeSolved);
	}
	
	//Checks if the current sudoku is solved. Takes a 2D field array.
	public static boolean sudokuSolved(Field[][] board) {
		int boardSize = board.length;
		int innerSquareSize = (int)Math.sqrt(boardSize);
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
		SudokuSolver solver = new EfficientSolver(board);
		List<int[][]> results = solver.solve(maxSolutions);
		System.out.println("Found "+results.size()+" solutions. Took "+solver.recursiveCalls+" recursive calls and "+solver.guesses+" guesses. Difficulty: "+solver.difficulty+".");
		if (results.size() > 0) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					board[i][j].value = results.get(0)[i][j];
				}
			}
		}
		if (sudokuSolved(board)) {
			System.out.println("Solved!");
		}
		view.repaint();
	}
	
	public void save(String fileName) {
		File file = new File("savedsudokus/"+fileName+".su");
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
		File file = new File("savedsudokus/"+fileName+".su");
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(Pattern.compile("[\\r\\n;]+"));
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
						board[i][j] = new Field(Integer.parseInt(next), mode != Mode.play);
					}
				}
			}
			scanner.close();
			view.clickedPosition = new int[] {0, 0};
			view.resetBoardPosition();
			view.repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean canBePlaced(Field[][] board, int x, int y, int value) {
		for (int i = 0; i < board.length; i++) {
			if ((i != y && board[x][i].value == value) || (i != x && board[i][y].value == value)) {
				return false;
			}
		}
		int innerSquareSize = (int) Math.sqrt(board.length);
		for (int i = x/innerSquareSize*innerSquareSize; i < x/innerSquareSize*innerSquareSize+innerSquareSize; i++) {
			for (int j = y/innerSquareSize*innerSquareSize; j < y/innerSquareSize*innerSquareSize+innerSquareSize; j++) {
				if ((i != x || j != y) && board[i][j].value == value) {
					return false;
				}
			}
		}
		return true;
	}

	public void generateSudoku(int difficulty) {
		int[][] matrix = new SudokuGenerator().generateSudoku(getBoardSize(), difficulty, (int)(getBoardSize()*getBoardSize()*0.4));
		for(int i = 0; i < getBoardSize(); i++) {
			for(int j = 0; j < getBoardSize(); j++){
				board[i][j].value = matrix[i][j];
			}
		}
		view.repaint();
	}
	
	public void giveHint() {
		int[] move = new EfficientSolver(board).makeOneMove();
		if (move != null) {
			setField(move[0], move[1], move[2]);	
		}
	}
	
	public Field[][] getBoard() {
		return board;
	}
	
	private static long start;
	
	public static void Stopwatch() {
		start = System.currentTimeMillis();
	}
	
    public static double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }
	
}

