package Generators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import solvers.*;

/*
	This is a sudoku generator. Using an algorithm described in the section 6.3 in the report.

	Responsible: Jens & Magnus
 */

public class SudokuGenerator {
	private int currentMissingFields = 0,
				recursiveCalls 		 = 0;
	public int difficulty = 0;
	public boolean cancelGenerator = false;
	
	//Generates and returns a sudoku with given n, k, minimum and maximum difficulty, and minimum empty fields.
	public int[][] generateSudoku(int innerSquareSize, int numInnerSquares, int minDifficulty, int maxDifficulty, int minMissingFields) {
		int[][] matrix = new int[innerSquareSize * numInnerSquares][innerSquareSize * numInnerSquares];
		RandomEfficientSolver randomEfficientSolver = new RandomEfficientSolver(matrix, innerSquareSize);
		(new Thread(new CancelSolverChecker(randomEfficientSolver))).start();
		List<int[][]> solutions = randomEfficientSolver.solve(1);
		if (solutions.isEmpty()) {
			return null;
		}
		int[][] solvedMatrix = solutions.get(0);
		int[][] result = generateRecursive(solvedMatrix, innerSquareSize, minDifficulty, maxDifficulty, minMissingFields);
		while (result == null && !cancelGenerator) {
			recursiveCalls = 0;
			solvedMatrix = new RandomEfficientSolver(matrix, innerSquareSize).solve(1).get(0);
			result = generateRecursive(solvedMatrix, innerSquareSize, minDifficulty, maxDifficulty, minMissingFields);
		}
		return result;
	}
	
	//Recursively generates a sudoku from a given random solved sudoku.
	private int[][] generateRecursive(int[][] board, int innerSquareSize, int minDifficulty, int maxDifficulty, int minMissingFields) {
		recursiveCalls++;
		if (recursiveCalls > 2 * (board.length * board.length) || cancelGenerator) {
			return null;
		}
		SudokuSolver solver = new EfficientSolver(board, innerSquareSize);
		if (!solver.hasUniqueSolution() || solver.difficulty > maxDifficulty) {
			return null;
		}

		if (solver.difficulty >= minDifficulty && currentMissingFields >= minMissingFields) {
			difficulty = solver.difficulty;
			return board; //Sudoku found.
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
			int[][] result = generateRecursive(board, innerSquareSize, minDifficulty, maxDifficulty, minMissingFields);
			if (result == null || (result.length == 1 && result[0] == null)) {
				board[coord[0]][coord[1]] = num;
				currentMissingFields--;
			}
			else {
				return result;
			}
		}
		return null;
	}
	
	//Returns a random number between min and max.
	public int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	//This class runs in a parallel thread and checks if the random solver should stop running.
	public class CancelSolverChecker implements Runnable {
		RandomEfficientSolver randomEfficientSolver;

		public CancelSolverChecker(RandomEfficientSolver randomEfficientSolver) {
			this.randomEfficientSolver = randomEfficientSolver;
		}

		@Override
		public void run() {
			while(true) {
				if (cancelGenerator) {
					randomEfficientSolver.cancel = true;
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
