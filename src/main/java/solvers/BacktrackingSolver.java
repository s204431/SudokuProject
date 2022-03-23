package solvers;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import sudoku.Field;
import MVC.Model;

public class BacktrackingSolver extends SudokuSolver {
	
	public BacktrackingSolver(Field[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	public BacktrackingSolver(int[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
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
			iterate(x, y, maxSolutions);
			return;
		}
		List<Integer> order = generateOrder();
		for (Integer integer : order) {
			if (solutionsFound >= maxSolutions) {
				return;
			}
			if (canBePlaced(x, y, integer)) {
				board[x][y] = integer;
				iterate(x, y, maxSolutions);
			}
		}
		board[x][y] = 0;
	}

	private void iterate(int x, int y, int maxSolutions) {
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

	protected List<Integer> generateOrder() {
		List<Integer> order = new ArrayList<>();
		for (int i = 1; i <= getMaxValue(); i++) {
			order.add(i);
		}
		return order;
	}
	
}
