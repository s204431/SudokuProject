package solvers;

import java.util.List;
import java.util.ArrayList;

import sudoku.Field;

/*
	This is the BackTracking Solver class that uses the SudokuSolver's
	functionality. It solves sudokus by using a backtracking technique.

	Responsible: Jens
*/

public class BacktrackingSolver extends SudokuSolver {
	
	//Constructor taking Field 2D array.
	public BacktrackingSolver(Field[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	//Constructor taking int 2D array.
	public BacktrackingSolver(int[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}

	//Find up to maxSolutions different solutions to a sudoku. Returns all of the solutions.
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

	//Recursively solves the sudoku.
	private void solveRecursive(int x, int y, int maxSolutions) {
		if (solutionsFound >= maxSolutions) {
			return;
		}
		recursiveCalls++;
		//Makes it stop at some point if it solves endlessly.
		if (recursiveCalls > 500000) {
			return;
		}
		if (board[x][y] > 0) {
			iterate(x, y, maxSolutions);
			return;
		}
		List<Integer> order = generateOrder();
		//Checks if done or inserts a valid field value.
		for (Integer integer : order) {
			if (solutionsFound >= maxSolutions) {
				return;
			}
			if (canBePlaced(x, y, integer)) {
				board[x][y] = integer;
				iterate(x, y, maxSolutions);
			}
		}
		//Ends up placing nothing in field.
		board[x][y] = 0;
	}
	
	//Iterates the index of the current field's value if not solved.
	//Otherwise updates number of solutions found and saves that solution.
	private void iterate(int x, int y, int maxSolutions) {
		if (x == board.length - 1 && y == board.length - 1) {
			if (sudokuSolved()) {
				solutions.add(copyOf(board));
				solutionsFound++;
			}
		}
		else if (y == board.length-1) {
			solveRecursive(x + 1, 0, maxSolutions);
		}
		else {
			solveRecursive(x, y + 1, maxSolutions);
		}
	}
	
	//Creates a list with numbers going from 1 to n^2.
	protected List<Integer> generateOrder() {
		List<Integer> order = new ArrayList<>();
		for (int i = 1; i <= getMaxValue(); i++) {
			order.add(i);
		}
		return order;
	}
	
}
