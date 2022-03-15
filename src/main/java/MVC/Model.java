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
	protected View view;
	public Field[][] board;
	public int innerSquareSize; //Width/height an inner square (n).
	public int numInnerSquares; //Number of inner squares in one side of the sudoku (k).
	public enum Mode {play, create, solver};
	public Mode mode = Mode.play;

	public Model(int numInnerSquares, int innerSquareSize, Mode mode) {
		this.mode = mode;
		this.innerSquareSize = innerSquareSize;
		this.numInnerSquares = numInnerSquares;
		board = new Field[getBoardSize()][getBoardSize()];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = new Field();
			}
		}
		Stopwatch();
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public int getBoardSize() {
		return innerSquareSize*numInnerSquares;
	}
	
	public void setField(int x, int y, Field field) {
		board[x][y] = field;
		if (sudokuSolved(board, innerSquareSize)) {
			System.out.println("Solved!");
		}
		view.repaint();
	}
	
	public void setField(int x, int y, int value) {
		board[x][y].value = value;
		if (sudokuSolved(board, innerSquareSize)) {
			System.out.println("Solved!");
		}
		view.repaint();
	}
	
	//Checks if the current sudoku is solved. Takes a 2D integer array.
	public static boolean sudokuSolved(int[][] board, int innerSquareSize) {
		Field[][] toBeSolved = new Field[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				toBeSolved[i][j] = new Field(board[i][j], true);
			}
		}
		return sudokuSolved(toBeSolved, innerSquareSize);
	}
	
	//Checks if the current sudoku is solved. Takes a 2D field array.
	public static boolean sudokuSolved(Field[][] board, int innerSquareSize) {
		int boardSize = board.length;
		int numInnerSquares = boardSize/innerSquareSize;
		int maxValue = innerSquareSize*innerSquareSize;
		boolean[][] foundColumn = new boolean[boardSize][maxValue];
		boolean[][] foundRow = new boolean[boardSize][maxValue];
		boolean[][] foundInnerSquare = new boolean[numInnerSquares*numInnerSquares][maxValue];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j].value < 1 || board[i][j].value > maxValue) {
					return false;
				}
				else {
					if (foundRow[i][board[i][j].value-1] || foundColumn[j][board[i][j].value-1] || foundInnerSquare[(i/innerSquareSize)*numInnerSquares+(j/innerSquareSize)][board[i][j].value-1]) {
						return false;
					}
					foundRow[i][board[i][j].value-1] = true;
					foundColumn[j][board[i][j].value-1] = true;
					foundInnerSquare[(i/innerSquareSize)*numInnerSquares+(j/innerSquareSize)][board[i][j].value-1] = true;
				}
			}
		}
		return true;
	}
	
	public void solve() {
		solve(Integer.MAX_VALUE);
	}
	
	public void solve(int maxSolutions) {
		SudokuSolver solver = new EfficientSolver(board, innerSquareSize);
		List<int[][]> results = solver.solve(maxSolutions);
		System.out.println("Found "+results.size()+" solutions. Took "+solver.recursiveCalls+" recursive calls and "+solver.guesses+" guesses. Difficulty: "+solver.difficulty+".");
		if (results.size() > 0) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					board[i][j].value = results.get(0)[i][j];
				}
			}
		}
		if (sudokuSolved(board, innerSquareSize)) {
			System.out.println("Solved!");
		}
		view.repaint();
	}
	
	public void save(String fileName) {
		File file = new File("savedsudokus/"+fileName+".su");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(numInnerSquares+";"+innerSquareSize+"\n");
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
	
	public static Object[] load(String fileName, Mode mode) {
		File file = new File("savedsudokus/"+fileName+".su");
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(Pattern.compile("[\\r\\n;]+"));
			int numInnerSquares = scanner.nextInt();
			int innerSquareSize = scanner.nextInt();
			int boardSize = numInnerSquares*innerSquareSize;
			Field[][] board = new Field[boardSize][boardSize];
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
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
			return new Object[] {board, innerSquareSize, numInnerSquares};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void loadAndUpdate(String fileName) {
		Object[] result = load(fileName, mode);
		board = (Field[][]) result[0];
		innerSquareSize = (int) result[1];
		numInnerSquares = (int) result[2];
		view.clickedPosition = new int[] {0, 0};
		view.resetBoardPosition();
		view.repaint();
	}
	
	public static boolean canBePlaced(Field[][] board, int innerSquareSize, int x, int y, int value) {
		for (int i = 0; i < board.length; i++) {
			if ((i != y && board[x][i].value == value) || (i != x && board[i][y].value == value)) {
				return false;
			}
		}
		for (int i = x/innerSquareSize*innerSquareSize; i < x/innerSquareSize*innerSquareSize+innerSquareSize; i++) {
			for (int j = y/innerSquareSize*innerSquareSize; j < y/innerSquareSize*innerSquareSize+innerSquareSize; j++) {
				if ((i != x || j != y) && board[i][j].value == value) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void generateSudoku(int minDifficulty, int maxDifficulty, double minMissingFieldsPercent) {
		generateSudoku(minDifficulty, maxDifficulty, minMissingFieldsPercent, innerSquareSize, numInnerSquares);
	}

	public void generateSudoku(int minDifficulty, int maxDifficulty, double minMissingFieldsPercent, int innerSquareSize, int numInnerSquares) {
		this.innerSquareSize = innerSquareSize;
		this.numInnerSquares = numInnerSquares;
		int[][] matrix = new SudokuGenerator().generateSudoku(innerSquareSize, numInnerSquares, minDifficulty, maxDifficulty, (int)(getBoardSize()*getBoardSize()*minMissingFieldsPercent));
		board = new Field[getBoardSize()][getBoardSize()];
		for(int i = 0; i < getBoardSize(); i++) {
			for(int j = 0; j < getBoardSize(); j++){
				board[i][j] = new Field(matrix[i][j], true);
			}
		}
		view.resetBoardPosition();
		view.repaint();
	}
	
	public void giveHint() {
		int[] move = new EfficientSolver(board, innerSquareSize).makeOneMove();
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
	
    public static int elapsedTime() {
        long now = System.currentTimeMillis();
        int Time = (int) ((now - start) / 1000.0);
        return Time;
    }
    
    public int getMaxNumber() {
    	return innerSquareSize*innerSquareSize;
    }
	
}
