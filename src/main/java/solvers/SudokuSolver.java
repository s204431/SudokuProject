package solvers;

import java.util.ArrayList;
import java.util.List;

import sudoku.Field;
import MVC.Model;

/*
	This is an abstract super class that all of
	the solvers extend from. It contains functionalities
	that solvers have in common.
*/

public abstract class SudokuSolver {
	protected int[][] board;
	protected int innerSquareSize, solutionsFound;
	public int recursiveCalls, guesses;
	public int difficulty = 0;
	protected List<int[][]> solutions = new ArrayList<>();

	//Constructor taking a field 2D array.
	public SudokuSolver(Field[][] board, int innerSquareSize) {
		setBoard(board, innerSquareSize);
	}
	
	//Constructor taking an int 2D array.
	public SudokuSolver(int[][] board, int innerSquareSize) {
		this.innerSquareSize = innerSquareSize;
		this.board = copyOf(board);
	}
	
	//Finds all solutions.
	public List<int[][]> solve() {
		return solve(Integer.MAX_VALUE);
	}
	
	//Finds at most maxSolutions different solutions. Should be extended by subclasses.
	public List<int[][]> solve(int maxSolutions) {
		return null;
	}

	//Changes the current board of the solver.
	public void setBoard(Field[][] board, int innerSquareSize) {
		this.innerSquareSize = innerSquareSize;
		this.board = new int[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				this.board[i][j] = board[i][j].value;
			}
		}
	}
	
	//Checks if the sudoku is solvable.
	public boolean isSolvable() {
		return solve(1).size() > 0;
	}
	
	//Checks if the sudoku has a unique solution.
	public boolean hasUniqueSolution() {
		return solve(2).size() == 1;
	}
	
	//Checks if a value can legally be placed at position (x, y).
	protected boolean canBePlaced(int x, int y, int value) {
		return Model.canBePlaced(toFields(board), innerSquareSize, x, y, value);
	}

	//Turns a 2D int array representation of a sudoku into a 2D field array representation.
	protected Field[][] toFields(int[][] matrix) {
		Field[][] fields = new Field[matrix.length][matrix[0].length];
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				fields[i][j] = new Field(matrix[i][j], true);
			}
		}
		return fields;
	}
	
	//Checks if a sudoku is valid by checking if there are any illegal values in the sudoku.
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
	
	//Checks if the sudoku is solved.
	protected boolean sudokuSolved() {
		return Model.sudokuSolved(board, innerSquareSize);
	}

	//Prints the sudoku to the console. Used for debugging.
	protected void print(int[][] board) {
		for (int[] ints : board) {
			for (int j = 0; j < board.length; j++) {
				System.out.print(ints[j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	//Makes a copy of a sudoku.
	protected int[][] copyOf(int[][] array) {
		int[][] result = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, result[i], 0, array[0].length);
		}
		return result;
	}

	//Resets the solver.
	protected void reset() {
		solutions = new ArrayList<>();
		solutionsFound = 0;
		recursiveCalls = 0;
		difficulty = 0;
		guesses = 0;
	}
	
	//Checks if k = n for the sudoku.
	protected boolean kEqualsN() {
		return innerSquareSize * innerSquareSize == board.length;
	}
	
	//Returns k.
	protected int getNumInnerSquares() {
		return board.length / innerSquareSize;
	}
	
	//Returns the maximum value that can be placed in fields.
	protected int getMaxValue() {
		return innerSquareSize * innerSquareSize;
	}
	
	//Static method that returns the minimum and maximum possible difficulty.
	public static int[] getDifficultyRange() {
		return new int[] {1, 8};
	}
	
	//Returns the range of difficulties that covers a specific string difficulty.
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
	
	//Returns all possible difficulty strings.
	public static String[] getDifficultyStrings() {
		return new String[] {"Easy", "Medium", "Hard", "Extreme"};
	}
	
	//Returns which string difficulty the given number difficulty corresponds to.
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
