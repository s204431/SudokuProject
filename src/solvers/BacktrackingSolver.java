package solvers;

import java.util.List;
import java.util.ArrayList;

import sudoku.Field;
import MVC.Model;

public class BacktrackingSolver extends SudokuSolver {
	
	public BacktrackingSolver(Field[][] board) {
		super(board);
	}
	
	public BacktrackingSolver(int[][] board) {
		super(board);
	}

	public List<int[][]> solve(int maxSolutions) {
		reset();
		if (isValidSudoku()) {
			solveRecursive(0, 0, maxSolutions);
		}
		if (recursiveCalls > 500000) {
			System.out.println("Solver took too long to find solutions.");
		}
		return solutions;
	}
	
	private void solveRecursive(int x, int y, int maxSolutions) {
		if (solutionsFound >= maxSolutions) {
			return;
		}
		recursiveCalls++;
		if (recursiveCalls > 500000) {
			return;
		}
		if (board[x][y] > 0) {
			if (x == board.length-1 && y == board.length-1) {
				if (sudokuSolved()) {
					solutions.add(copyOf(board));
					solutionsFound++;
				}
			}
			else if (y == board.length-1) {
				solveRecursive(x+1, 0, maxSolutions);
			}
			else {
				solveRecursive(x, y+1, maxSolutions);
			}
			return;
		}
		for (int v = 1; v <= board.length; v++) {
			if (solutionsFound >= maxSolutions) {
				return;
			}
			if (canBePlaced(x, y, v)) {
				board[x][y] = v;
				if (x == board.length-1 && y == board.length-1) {
					if (sudokuSolved()) {
						solutions.add(copyOf(board));
						solutionsFound++;
					}
				}
				else if (y == board.length-1) {
					solveRecursive(x+1, 0, maxSolutions);
				}
				else {
					solveRecursive(x, y+1, maxSolutions);
				}
			}
		}
		board[x][y] = 0;
	}
	
}
