package solvers;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import sudoku.Field;
import MVC.Model;

/*
	This RandomBacktrackingSolver is a random version
	of the BacktrackingSolver.
*/


public class RandomBacktrackingSolver extends BacktrackingSolver {
	
	//Constructor taking a field 2D array.
	public RandomBacktrackingSolver(Field[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	//Constructor taking an int 2D array.
	public RandomBacktrackingSolver(int[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}
	
	//Shuffles the order of values to make the solver random.
	protected List<Integer> generateOrder() {
		List<Integer> randomOrder = super.generateOrder();
		Collections.shuffle(randomOrder);
		return randomOrder;
	}
	
}
