package solvers;

import java.util.List;

import sudoku.Field;
import sudoku.Model;

public abstract class SudokuSolver {
	
	protected int[][] board;
	protected Model model;
	protected int solutionsFound;
	
	public SudokuSolver(Field[][] board, Model model) {
		this.board = new int[board.length][board[0].length];
		this.model = model;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				this.board[i][j] = board[i][j].value;
			}
		}
	}
	
	public SudokuSolver(int[][] board, Model model) {
		this.board = new int[board.length][board[0].length];
		this.model = model;
	}
	
	//Finds all solutions.
	public List<int[][]> solve() {
		return solve(Integer.MAX_VALUE);
	}
	
	//Finds at most maxSolutions different solutions.
	public List<int[][]> solve(int maxSolutions) {
		return null;
	}
	
	public boolean isSolvable() {
		return solve(1).size() > 0;
	}
	
	public boolean hasUniqueSolution() {
		return solve(2).size() == 1;
	}
	
	protected boolean canBePlaced(int x, int y, int value) {
		for (int i = 0; i < board.length; i++) {
			if ((i != y && board[x][i] == value) || (i != x && board[i][y] == value)) {
				return false;
			}
		}
		int innerSquareSize = (int) Math.sqrt(board.length);
		for (int i = x/innerSquareSize*innerSquareSize; i < x/innerSquareSize*innerSquareSize+innerSquareSize; i++) {
			for (int j = y/innerSquareSize*innerSquareSize; j < y/innerSquareSize*innerSquareSize+innerSquareSize; j++) {
				if ((i != x || j != y) && board[i][j] == value) {
					return false;
				}
			}
		}
		return true;
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
	
	protected void print() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				System.out.print(" "+board[i][j]);
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

}
