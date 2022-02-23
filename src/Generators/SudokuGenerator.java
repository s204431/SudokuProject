package Generators;
import java.util.Random;

import solvers.*;


public class SudokuGenerator {

	
	public int[][] generateSudoku(int N){
		int[][] matrix = new int[N][N];
		if (N < 2) {
			return matrix;
		}
		matrix = new RandomBacktrackingSolver(matrix).solve(1).get(0);
		
		int col;
		int row;
		int lastNumber;
//		int uniqueAttempts = 0;
		int difficulty;
		
		
		do {
			while(true) {
				col = randomNumber(0,N - 1);
				row = randomNumber(0,N - 1);
				if(matrix[col][row] != 0) {
					lastNumber = matrix[col][row];
					matrix[col][row] = 0;
					break;
					
				}
				
			}
			SudokuSolver solver = new EfficientSolver(matrix);
			if(!(solver.hasUniqueSolution()) || solver.difficulty > 2) {
				matrix[col][row] = lastNumber;
//				uniqueAttempts++;
			}
			difficulty = solver.difficulty;
			System.out.println(difficulty);
		} while (difficulty != 2);
		
		
		
//		do {
//			while(true) {
//				col = randomNumber(0,N - 1);
//				row = randomNumber(0,N - 1);
//				if(matrix[col][row] != 0) {
//					lastNumber = matrix[col][row];
//					matrix[col][row] = 0;
//					break;
//					
//				}
//				
//			}
//			System.out.println("test");
//		} while (new BacktrackingSolver(matrix).hasUniqueSolution());
//		
//		matrix[col][row] = lastNumber;
		

		return matrix;
		
	}
	
	public int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	

}
