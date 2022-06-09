package solvers;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import sudoku.Field;
import MVC.Model;

/*
	This RandomBacktrackingSolver uses the BacktrackingSolver
	and differs with that it takes the order of the backtracking
	from and shuffles it in a random order that makes the backtracking
	not follow logic, but randomness.
*/


public class RandomBacktrackingSolver extends BacktrackingSolver {
	
	public RandomBacktrackingSolver(Field[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	public RandomBacktrackingSolver(int[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	//Shuffles the order.
	protected List<Integer> generateOrder() {
		List<Integer> randomOrder = super.generateOrder();
		Collections.shuffle(randomOrder);
		return randomOrder;
	}
	
}
