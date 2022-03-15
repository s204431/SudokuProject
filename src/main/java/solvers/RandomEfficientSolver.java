package solvers;

import java.util.Collections;
import java.util.List;

import sudoku.Field;

public class RandomEfficientSolver extends EfficientSolver {
	
	public RandomEfficientSolver(Field[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	public RandomEfficientSolver(int[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	//Shuffle the order of the values.
	protected void generateOrder(List<Integer> values) {
		Collections.shuffle(values);
	}

}