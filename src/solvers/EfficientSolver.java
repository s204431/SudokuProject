package solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import MVC.Model;
import sudoku.Field;

public class EfficientSolver extends SudokuSolver {
	
	public EfficientSolver(Field[][] board) {
		super(board);
	}
	
	public EfficientSolver(int[][] board) {
		super(board);
	}
	
	public List<int[][]> solve(int maxSolutions) {
		reset();
		solveRecursive(board, initializePossibleValues(), maxSolutions);
		return solutions;
	}
	
	private void solveRecursive(int[][] board, List<Integer>[][] possibleValues, int maxSolutions) {
		recursiveCalls++;
		List<Integer>[] move = makeMove(board, possibleValues);
		if (move == null) {
			if (Model.sudokuSolved(board)) {
				solutions.add(copyOf(board));
				solutionsFound++;
			}
			return;
		}
		for (int i = 0; i < move[1].size(); i++) {
			int[][] newBoard = copyOf(board);
			newBoard[move[0].get(0)][move[0].get(1)] = move[1].get(i);
			List<Integer>[][] copy = copyOf(possibleValues);
			add(board, copy, move[0].get(0), move[0].get(1), move[1].get(i));
			solveRecursive(newBoard, copy, maxSolutions);
			if (solutionsFound >= maxSolutions) {
				return;
			}
		}
	}
	
	private List<Integer>[] makeMove(int[][] board, List<Integer>[][] possibleValues) {
		//Detect unsolvable.
		int innerSquareSize = (int)Math.sqrt(board.length);
		boolean[][] foundColumn = new boolean[board.length][board.length];
		boolean[][] foundRow = new boolean[board.length][board.length];
		boolean[][] foundInnerSquare = new boolean[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] < 1) {
					for (int v : possibleValues[i][j]) {
						foundRow[i][v-1] = true;
						foundColumn[j][v-1] = true;
						foundInnerSquare[(i/innerSquareSize)*innerSquareSize+(j/innerSquareSize)][v-1] = true;
					}
				}
				else {
					foundRow[i][board[i][j]-1] = true;
					foundColumn[j][board[i][j]-1] = true;
					foundInnerSquare[(i/innerSquareSize)*innerSquareSize+(j/innerSquareSize)][board[i][j]-1] = true;
				}
			}
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (!foundRow[i][j] || !foundColumn[i][j] || !foundInnerSquare[i][j]) {
					return null;
				}
			}
		}
		//Single position.
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				for (int v : possibleValues[i][j]) {
					boolean found1 = false;
					boolean found2 = false;
					boolean found3 = false;
					for (int k = 0; k < board.length; k++) {
						if (k != j && possibleValues[i][k].contains(v)) {
							found1 = true;
						}
						if (k != i && possibleValues[k][j].contains(v)) {
							found2 = true;
						}
					}
					for (int k = i/innerSquareSize*innerSquareSize; k < i/innerSquareSize*innerSquareSize+innerSquareSize; k++) {
						for (int l = j/innerSquareSize*innerSquareSize; l < j/innerSquareSize*innerSquareSize+innerSquareSize; l++) {
							if ((k != i || l != j) && possibleValues[k][l].contains(v)) {
								found3 = true;
							}
						}
					}
					if (!found1 || !found2 || !found3) {
						ArrayList<Integer> pos = new ArrayList<>();
						pos.add(i);
						pos.add(j);
						ArrayList<Integer> lv = new ArrayList<>();
						lv.add(v);
						return new ArrayList[] {pos, lv};
					}
				}
			}
		}
		//Choose field with least possible values.
		int lowestPossibleValues = Integer.MAX_VALUE;
		int lowestX = -1;
		int lowestY = -1;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (possibleValues[i][j].size() > 0 && possibleValues[i][j].size() < lowestPossibleValues) {
					lowestPossibleValues = possibleValues[i][j].size();
					lowestX = i;
					lowestY = j;
				}
				else if (possibleValues[i][j].size() == 0 && board[i][j] <= 0) {
					return null;
				}
			}
		}
		if (lowestPossibleValues == Integer.MAX_VALUE) {
			return null;
		}
		ArrayList<Integer> pos = new ArrayList<>();
		pos.add(lowestX);
		pos.add(lowestY);
		return new ArrayList[] {pos, (ArrayList)possibleValues[lowestX][lowestY]};
	}
	
	private List<Integer>[][] initializePossibleValues() {
		List<Integer>[][] possibleValues = new ArrayList[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				possibleValues[i][j] = new ArrayList<Integer>();
				for (int k = 1; k <= board.length; k++) {
					possibleValues[i][j].add(k);
				}
			}
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] > 0) {
					add(board, possibleValues, i, j, board[i][j]);
				}
			}
		}
		return possibleValues;
	}
	
	//Update possibleValues.
	private void add(int[][] board, List<Integer>[][] possibleValues, int x, int y, int value) {
		possibleValues[x][y] = new ArrayList<Integer>();
		for (int i = 0; i < board.length; i++) {
			if (i != y) {
				possibleValues[x][i].remove((Integer)value);
			}
			if (i != x) {
				possibleValues[i][y].remove((Integer)value);
			}
		}
		int innerSquareSize = (int) Math.sqrt(board.length);
		for (int i = x/innerSquareSize*innerSquareSize; i < x/innerSquareSize*innerSquareSize+innerSquareSize; i++) {
			for (int j = y/innerSquareSize*innerSquareSize; j < y/innerSquareSize*innerSquareSize+innerSquareSize; j++) {
				if ((i != x || j != y)) {
					possibleValues[i][j].remove((Integer)value);
				}
			}
		}
	}
	
	private List<Integer>[][] copyOf(List<Integer>[][] possibleValues) {
		List<Integer>[][] newPossibleValues = new ArrayList[possibleValues.length][possibleValues[0].length];
		for (int i = 0; i < possibleValues.length; i++) {
			for (int j = 0; j < possibleValues[i].length; j++) {
				newPossibleValues[i][j] = new ArrayList<Integer>();
				for (int k = 0; k < possibleValues[i][j].size(); k++) {
					newPossibleValues[i][j].add(possibleValues[i][j].get(k));
				}
			}
		}
		return newPossibleValues;
	}
	
}
