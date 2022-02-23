package Generators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import solvers.*;


public class SudokuGenerator {
	private int currentMissingFields = 0;
	
	public int[][] generateSudoku(int N, int difficulty, int minMissingFields) {
		int[][] matrix = new int[N][N];
		if (N < 2) {
			return matrix;
		}
		matrix = new RandomBacktrackingSolver(matrix).solve(1).get(0);
		return generateRecursive(matrix, difficulty, minMissingFields);
	}
	
	private int[][] generateRecursive(int[][] board, int difficulty, int minMissingFields) {
		SudokuSolver solver = new EfficientSolver(board);
		if (!solver.hasUniqueSolution() || solver.difficulty > difficulty) {
			return null;
		}
		//System.out.println(solver.difficulty);
		if (solver.difficulty == difficulty && currentMissingFields >= minMissingFields) {
			return board;
		}
		List<Integer[]> coordinates = new ArrayList();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] > 0) {
					coordinates.add(new Integer[] {i, j});
				}
			}
		}
		Collections.shuffle(coordinates);
		for (Integer[] coord : coordinates) {
			int num = board[coord[0]][coord[1]];
			board[coord[0]][coord[1]] = 0;
			currentMissingFields++;
			int[][] result = generateRecursive(board, difficulty, minMissingFields);
			if (result == null) {
				board[coord[0]][coord[1]] = num;
				currentMissingFields--;
			}
			else {
				return result;
			}
		}
		return null;
	}
	
	public int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	

}
