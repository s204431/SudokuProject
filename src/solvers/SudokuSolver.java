package solvers;

import java.util.Arrays;
import java.util.List;

import sudoku.Field;
import sudoku.Model;

public class SudokuSolver {
	
	protected int[][] board;
	protected Model model;
	
	public SudokuSolver(Field[][] board, Model model) {
		this.board = new int[board.length][board[0].length];
		this.model = model;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				this.board[i][j] = board[i][j].value;
			}
		}
	}
	
	public List<int[][]> solve() {
		return null;
	}
	
	public boolean canBePlaced(int x, int y, int value) {
		for (int i = 0; i < board.length; i++) {
			if (board[x][i] == value || board[i][y] == value) {
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
