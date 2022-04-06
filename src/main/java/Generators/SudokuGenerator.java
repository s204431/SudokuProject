package Generators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import testers.GeneratorTester.*;
import solvers.*;

/*
	This is a sudoku generator. Using an algorithm described in the appendix TODO
	this class generates a sudoku board containing enough fields to be solved.

*/

public class SudokuGenerator {
	private int currentMissingFields = 0;
	private int recursiveCalls = 0;
	public int difficulty = 0;
	
	public int[][] generateSudoku(int innerSquareSize, int numInnerSquares, int minDifficulty, int maxDifficulty, int minMissingFields) {
		int[][] matrix = new int[innerSquareSize*numInnerSquares][innerSquareSize*numInnerSquares];
		int[][] solvedMatrix = new RandomEfficientSolver(matrix, innerSquareSize).solve(1).get(0);
		int[][] result = generateRecursive(solvedMatrix, innerSquareSize, minDifficulty, maxDifficulty, minMissingFields);
		while (result == null) {
			recursiveCalls = 0;
			solvedMatrix = new RandomEfficientSolver(matrix, innerSquareSize).solve(1).get(0);
			result = generateRecursive(solvedMatrix, innerSquareSize, minDifficulty, maxDifficulty, minMissingFields);
		}
		return result;
	}
	
	private int[][] generateRecursive(int[][] board, int innerSquareSize, int minDifficulty, int maxDifficulty, int minMissingFields) {
		recursiveCalls++;
		if (recursiveCalls > 2*(board.length*board.length)) {
			return null;
		}
		SudokuSolver solver = new EfficientSolver(board, innerSquareSize);
		if (!solver.hasUniqueSolution() || solver.difficulty > maxDifficulty) {
			return null;
		}

		if (solver.difficulty >= minDifficulty && solver.difficulty <= maxDifficulty && currentMissingFields >= minMissingFields) {
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
	
	public int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
