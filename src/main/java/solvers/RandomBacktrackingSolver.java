package solvers;

import java.util.Collections;
import java.util.List;
import sudoku.Field;

public class RandomBacktrackingSolver extends BacktrackingSolver {

  public RandomBacktrackingSolver(Field[][] board, int innerSquareSize) {
    super(board, innerSquareSize);
  }

  public RandomBacktrackingSolver(int[][] board, int innerSquareSize) {
    super(board, innerSquareSize);
  }

  protected List<Integer> generateOrder() {
    List<Integer> randomOrder = super.generateOrder();
    Collections.shuffle(randomOrder);
    return randomOrder;
  }
}
