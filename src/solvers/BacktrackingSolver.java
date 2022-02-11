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

	public List<int[][]> solve() {
		System.out.println("Solving");
		solveRecursive(0, 0);
		System.out.println("Done");
		return solutions;
	}
	
	public void solveRecursive(int x, int y) {
		if (board[x][y] > 0) {
			if (x == board.length-1 && y == board.length-1) {
				if (model.sudokuSolved(board)) {
					solutions.add(copyOf(board));
					print();
				}
			}
			else if (y == board.length-1) {
				solveRecursive(x+1, 0);
			}
			else {
				solveRecursive(x, y+1);
			}
			return;
		}
		for (int v = 1; v <= board.length; v++) {
			if (canBePlaced(x, y, v)) {
				board[x][y] = v;
				if (x == board.length-1 && y == board.length-1) {
					if (model.sudokuSolved(board)) {
						solutions.add(copyOf(board));
						print();
					}
				}
				else if (y == board.length-1) {
					solveRecursive(x+1, 0);
				}
				else {
					solveRecursive(x, y+1);
				}
			}
		}
		board[x][y] = 0;
	}
	
}
