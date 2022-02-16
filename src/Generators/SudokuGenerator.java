package Generators;
import java.util.Random;

import solvers.*;


public class SudokuGenerator {

	
	public int[][] generateSudoku(int N){
		int[][] matrix = new int[N][N];
		matrix = new RandomBacktrackingSolver(matrix).solve(1).get(0);
		
		int col;
		int row;
		int lastNumber;
		
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
		} while (new BacktrackingSolver(matrix).hasUniqueSolution());
		
		matrix[col][row] = lastNumber;
		return matrix;
		
	}
	
	public int randomNumber(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	

}
