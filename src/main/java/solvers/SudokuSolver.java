package solvers;

import java.util.ArrayList;
import java.util.List;

import sudoku.Field;
import MVC.Model;

/*
	All the solvers in the sudoku solver package extend
	the SudokuSolver class. This class is the foundation
	of the other solvers and has some methods that are
	empty because they should have their own implementation
	in the classes that extends SudokuSolver.
*/

public abstract class SudokuSolver {
	protected int[][] board;
	protected int innerSquareSize, solutionsFound;
	public int recursiveCalls, guesses;
	public int difficulty = 0;
	protected List<int[][]> solutions = new ArrayList<>();

	
	public SudokuSolver(Field[][] board, int innerSquareSize) {
		setBoard(board, innerSquareSize);
	}
	
	public SudokuSolver(int[][] board, int innerSquareSize) {
		this.innerSquareSize = innerSquareSize;
		this.board = copyOf(board);
	}
	
	//Finds all solutions.
	public List<int[][]> solve() {
		return solve(Integer.MAX_VALUE);
	}
	
	//Finds at most maxSolutions different solutions.
	public List<int[][]> solve(int maxSolutions) {
		return null;
	}
	
	public int[] makeOneMove() {
		return null;
	}

	//Initializes the board the with respective value of n and k.
	public void setBoard(Field[][] board, int innerSquareSize) {
		this.innerSquareSize = innerSquareSize;
		this.board = new int[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				this.board[i][j] = board[i][j].value;
			}
		}
	}
	
	public boolean isSolvable() {
		return solve(1).size() > 0;
	}
	
	public boolean hasUniqueSolution() {
		return solve(2).size() == 1;
	}
	
	protected boolean canBePlaced(int x, int y, int value) {
		return Model.canBePlaced(toFields(board), innerSquareSize, x, y, value);
	}

	//Takes the sudoku board and makes field out of this.
	protected Field[][] toFields(int[][] matrix) {
		Field[][] fields = new Field[matrix.length][matrix[0].length];
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				fields[i][j] = new Field(matrix[i][j], true);
			}
		}
		return fields;
	}
	//Checks validity by seeing if values can be
	//inserted in non-empty fields.
	protected boolean isValidSudoku() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] != 0 && !canBePlaced(i, j, board[i][j])) {
					return false;
				}
			}
		}
		return true;
	}
	
	protected boolean sudokuSolved() {
		return Model.sudokuSolved(board, innerSquareSize);
	}

	protected void print(int[][] board) {
		for (int[] ints : board) {
			for (int j = 0; j < board.length; j++) {
				System.out.print(ints[j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	//Copies all elements in an array into another.
	protected int[][] copyOf(int[][] array) {
		int[][] result = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, result[i], 0, array[0].length);
		}
		return result;
	}

	//This resets values for when starting over a recursively solving instance
	protected void reset() {
		solutions = new ArrayList<>();
		solutionsFound = 0;
		recursiveCalls = 0;
		difficulty = 0;
		guesses = 0;
	}
	
	protected boolean kEqualsN() {
		return innerSquareSize * innerSquareSize == board.length;
	}
	
	protected int getNumInnerSquares() {
		return board.length / innerSquareSize;
	}
	
	protected int getMaxValue() {
		return innerSquareSize * innerSquareSize;
	}
	
	public static int[] getDifficultyRange() {
		return new int[] {1, 8};
	}
	//Returns the range that the given difficulty has.
	public static int[] getDifficultyRange(String difficulty) {
		switch(difficulty) {
			case "Unsolvable":
				return new int[] {0, 0};
			case "Easy":
				return new int[] {1, 2};
			case "Medium":
				return new int[] {3, 4};
			case "Hard":
				return new int[] {5, 7};
			case "Extreme":
				return new int[] {8, 8};
			default:
				return null;
		}
	}
	
	public static String[] getDifficultyStrings() {
		return new String[] {"Easy", "Medium", "Hard", "Extreme"};
	}
	//Determines Difficulty string by checking value of difficulty
	public static String getDifficultyString(int difficulty) {
		if (difficulty <= 0) {
			return "Unsolvable";
		}
		else if (difficulty <= 2) {
			return "Easy";
		}
		else if (difficulty <= 4) {
			return "Medium";
		}
		else if (difficulty <= 7) {
			return "Hard";
		}
		else if (difficulty <= 8) {
			return "Extreme";
		}
		return "";
	}

}
