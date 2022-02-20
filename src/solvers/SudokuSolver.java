package solvers;

import java.util.ArrayList;
import java.util.List;

import sudoku.Field;
import MVC.Model;

public abstract class SudokuSolver {
	
	protected int[][] board;
	protected int solutionsFound;
	public int recursiveCalls;
	protected List<int[][]> solutions = new ArrayList<>();
	
	public SudokuSolver(Field[][] board) {
		setBoard(board);
	}
	
	public SudokuSolver(int[][] board) {
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
	
	public void setBoard(Field[][] board) {
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
		return Model.canBePlaced(toFields(board), x, y, value);
	}
	
	protected Field[][] toFields(int[][] matrix) {
		Field[][] fields = new Field[matrix.length][matrix[0].length];
		for (int i = 0; i < fields.length; i++) {
			for (int j = 0; j < fields[i].length; j++) {
				fields[i][j] = new Field(matrix[i][j], true);
			}
		}
		return fields;
	}
	
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
		return Model.sudokuSolved(board);
	}
	
	protected void print() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				System.out.print(board[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	protected int[][] copyOf(int[][] array) {
		int[][] result = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				result[i][j] = array[i][j];
			}
		}
		return result;
	}
	
	protected void reset() {
		solutions = new ArrayList<>();
		solutionsFound = 0;
		recursiveCalls = 0;
	}

}
