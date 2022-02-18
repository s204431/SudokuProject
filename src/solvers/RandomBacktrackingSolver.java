package solvers;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import sudoku.Field;
import MVC.Model;

public class RandomBacktrackingSolver extends SudokuSolver {
	
	public RandomBacktrackingSolver(Field[][] board) {
		super(board);
	}
	
	public RandomBacktrackingSolver(int[][] board) {
		super(board);
	}

	public List<int[][]> solve(int maxSolutions) {
		reset();
		if (isValidSudoku()) {
			solveRecursive(0, 0, maxSolutions);
		}
		return solutions;
	}
	
	private void solveRecursive(int x, int y, int maxSolutions) {
		if (solutionsFound >= maxSolutions) {
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
		List<Integer> randomOrder = new ArrayList<>();
		for (int i = 1; i <= board.length; i++) {
			randomOrder.add(i);
		}
		Collections.shuffle(randomOrder);
		for (int i = 0; i < randomOrder.size(); i++) {
			if (solutionsFound >= maxSolutions) {
				return;
			}
			if (canBePlaced(x, y, randomOrder.get(i))) {
				board[x][y] = randomOrder.get(i);
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
