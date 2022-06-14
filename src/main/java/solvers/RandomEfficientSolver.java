package solvers;

import java.util.Collections;
import java.util.List;

import sudoku.Field;

/*
	This RandomEfficientSolver is a random version
	of the EfficientSolver.
*/

public class RandomEfficientSolver extends EfficientSolver {
	
	//Constructor taking a field 2D array.
	public RandomEfficientSolver(Field[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	//Constructor taking an int 2D array.
	public RandomEfficientSolver(int[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	//Shuffles the order of values to make the solver random.
	protected void generateOrder(List<Integer> values) {
		Collections.shuffle(values);
	}

}
