package solvers;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import sudoku.Field;
import sudoku.Model;

public class BacktrackingSolver extends SudokuSolver {
	List<int[][]> solutions = new ArrayList<>();
	
	public BacktrackingSolver(Field[][] board, Model model) {
		super(board, model);
	}

	public List<int[][]> solve(int maxSolutions) {
		System.out.println("Solving");
		solutionsFound = 0;
		solutions = new ArrayList<>();
		if (isValidSudoku()) {
			solveRecursive(0, 0, maxSolutions);
		}
		System.out.println("Done");
		return solutions;
	}
	
	public void solveRecursive(int x, int y, int maxSolutions) {
		if (solutionsFound >= maxSolutions) {
			return;
		}
		if (board[x][y] > 0) {
			if (x == board.length-1 && y == board.length-1) {
				if (model.sudokuSolved(board)) {
					solutions.add(copyOf(board));
					solutionsFound++;
					print();
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
					if (model.sudokuSolved(board)) {
						solutions.add(copyOf(board));
						solutionsFound++;
						print();
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
