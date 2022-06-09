package solvers;

import java.util.Collections;
import java.util.List;

import sudoku.Field;

/*
	Just like the RandomBacktrackingSolver that randomizes the
	BacktrackingSolver this class also shuffles the order of
	the values that are generated and therefore makes the order
	of what solving method should be chosen random.
*/

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
