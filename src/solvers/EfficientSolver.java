package solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import MVC.Model;
import sudoku.Field;

public class EfficientSolver extends SudokuSolver {
	
	private boolean detectUnsolvable = true;
	private boolean useSingleCandidate = true;
	private boolean useSinglePosition = true;
	private boolean useCandidateLines = true;
	private boolean useNakedPairs = true;
	private boolean useXWing = true;
	
	public EfficientSolver(Field[][] board) {
		super(board);
	}
	
	public EfficientSolver(int[][] board) {
		super(board);
	}
	
	public List<int[][]> solve(int maxSolutions) {
		reset();
		solveRecursive(board, initializePossibleValues(), maxSolutions);
		if (recursiveCalls > 500000) {
			System.out.println("Solver took too long to find solutions.");
		}
		if (solutions.size() == 0) {
			difficulty = 0;
		}
		guesses--;
		return solutions;
	}
	
	private void solveRecursive(int[][] board, List<Integer>[][] possibleValues, int maxSolutions) {
		recursiveCalls++;
		guesses++;
		if (recursiveCalls > 500000) {
			return;
		}
		List<Integer>[] move = makeMove(board, possibleValues);
		if (move == null) {
			if (Model.sudokuSolved(board)) {
				solutions.add(copyOf(board));
				solutionsFound++;
			}
			return;
		}
		if (move[1].size() == 1) {
			guesses--;
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
	
	public List<Integer>[] makeMove(int[][] board, List<Integer>[][] possibleValues) {
		if (detectUnsolvable && detectUnsolvable(board, possibleValues)) {
			return null;
		}
		if (useSingleCandidate) {
			List<Integer>[] result = singleCandidate(board, possibleValues);
			if (result != null) {
				if (difficulty < 1) {
					difficulty = 1;
				}
				return result;
			}
		}
		if (useSinglePosition) {
			List<Integer>[] result = singlePosition(board, possibleValues);
			if (result != null) {
				if (difficulty < 2) {
					difficulty = 2;	
				}
				return result;
			}
		}
		if (useCandidateLines) {
			boolean updated = candidateLines(board, possibleValues);
			if (updated) {
				if (difficulty < 3) {
					difficulty = 3;	
				}
				return makeMove(board, possibleValues);
			}
		}
		if (useNakedPairs) {
			boolean updated = nakedPairs(board, possibleValues);
			if (updated) {
				if (difficulty < 4) {
					difficulty = 4;	
				}
				return makeMove(board, possibleValues);
			}
		}
		if (useXWing) {
			boolean updated = xWing(board, possibleValues);
			if (updated) {
				if (difficulty < 5) {
					difficulty = 5;	
				}
				return makeMove(board, possibleValues);
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
			}
		}
		difficulty = 6;
		return toMove(lowestX, lowestY, (ArrayList)possibleValues[lowestX][lowestY]);
	}
	
	private List<Integer>[] singleCandidate(int[][] board, List<Integer>[][] possibleValues) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (possibleValues[i][j].size() == 1) {
					return toMove(i, j, (ArrayList)possibleValues[i][j]);
				}
			}
		}
		return null;
	}
	
	private List<Integer>[] singlePosition(int[][] board, List<Integer>[][] possibleValues) {
		int innerSquareSize = (int)Math.sqrt(board.length);
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
						return toMove(i, j, v);
					}
				}
			}
		}
		return null;
	}
	
	private boolean candidateLines(int[][] board, List<Integer>[][] possibleValues) {
		int innerSquareSize = (int)Math.sqrt(board.length);
		boolean updated = false;
		for (int i = 0; i < innerSquareSize; i++) {
			for (int j = 0; j < innerSquareSize; j++) {
				for (int v = 1; v <= board.length; v++) {
					int foundInRow = -1;
					int foundInColumn = -1;
					for (int k = i*innerSquareSize; k < i*innerSquareSize+innerSquareSize; k++) {
						for (int l = j*innerSquareSize; l < j*innerSquareSize+innerSquareSize; l++) {
							if (possibleValues[k][l].contains(v)) {
								if (foundInRow == -1 || k == foundInRow) {
									foundInRow = k;
								}
								else {
									foundInRow = -2;
								}
								if (foundInColumn == -1 || l == foundInColumn) {
									foundInColumn = l;
								}
								else {
									foundInColumn = -2;
								}
							}
						}
					}
					if (foundInRow >= 0 && foundInColumn < 0) {
						for (int l = 0; l < board.length; l++) {
							if (l < j*innerSquareSize || l >= j*innerSquareSize+innerSquareSize) {
								if (possibleValues[foundInRow][l].contains(v)) {
									possibleValues[foundInRow][l].remove((Integer) v);
									updated = true;
								}
							}
						}
					}
					else if (foundInRow < 0 && foundInColumn >= 0) {
						for (int k = 0; k < board.length; k++) {
							if (k < i*innerSquareSize || k >= i*innerSquareSize+innerSquareSize) {
								if (possibleValues[k][foundInColumn].contains(v)) {
									possibleValues[k][foundInColumn].remove((Integer) v);
									updated = true;
								}
							}
						}
					}
				}
			}
		}
		return updated;
	}
	
	private boolean nakedPairs(int[][] board, List<Integer>[][] possibleValues) {
		boolean updated = false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				List<Integer> foundPositions = new ArrayList<Integer>();
				for (int r = 0; r < board.length; r++) {
					if (possibleValues[i][j].size() == possibleValues[r][j].size()) {
						boolean add = false;
						for (int v : possibleValues[i][j]) {
							if (possibleValues[r][j].contains(v)) {
								add = true;
							}
						}
						if (add) {
							foundPositions.add(r);
						}
					}
				}
				boolean remove = true;
				while (remove) {
					remove = false;
					for (int p : foundPositions) {
						remove = false;
						for (int v : possibleValues[p][j]) {
							boolean found = false;
							for (int p2 : foundPositions) {
								if (p != p2 && possibleValues[p2][j].contains(v)) {
									found = true;
									break;
								}
							}
							if (!found) {
								remove = true;
								break;
							}
						}
						if (remove) {
							foundPositions.remove((Integer)p);
							break;
						}
					}
				}
				List<Integer> distinctValues = new ArrayList<>();
				for (int p : foundPositions) {
					for (int v : possibleValues[p][j]) {
						if (!distinctValues.contains(v)) {
							distinctValues.add(v);
						}
					}
				}
				if (foundPositions.size() > 0 && foundPositions.size() < board.length && distinctValues.size() == foundPositions.size()) {
					for (int r = 0; r < board.length; r++) {
						if (!foundPositions.contains(r)) {
							int sizeBefore = possibleValues[r][j].size();
							possibleValues[r][j].removeAll(possibleValues[i][j]);
							if (sizeBefore != possibleValues[r][j].size()) {
								updated = true;
							}
						}
					}
				}
				foundPositions = new ArrayList<Integer>();
				for (int c = 0; c < board.length; c++) {
					if (possibleValues[i][j].size() == possibleValues[i][c].size()) {
						boolean add = false;
						for (int v : possibleValues[i][j]) {
							if (possibleValues[i][c].contains(v)) {
								add = true;
							}
						}
						if (add) {
							foundPositions.add(c);
						}
					}
				}
				remove = true;
				while (remove) {
					remove = false;
					for (int p : foundPositions) {
						remove = false;
						for (int v : possibleValues[i][p]) {
							boolean found = false;
							for (int p2 : foundPositions) {
								if (p != p2 && possibleValues[i][p].contains(v)) {
									found = true;
									break;
								}
							}
							if (!found) {
								remove = true;
								break;
							}
						}
						if (remove) {
							foundPositions.remove((Integer)p);
							break;
						}
					}
				}
				distinctValues = new ArrayList<>();
				for (int p : foundPositions) {
					for (int v : possibleValues[i][p]) {
						if (!distinctValues.contains(v)) {
							distinctValues.add(v);
						}
					}
				}
				if (foundPositions.size() > 0 && foundPositions.size() < board.length && distinctValues.size() == foundPositions.size()) {
					for (int c = 0; c < board.length; c++) {
						if (!foundPositions.contains(c)) {
							int sizeBefore = possibleValues[i][c].size();
							possibleValues[i][c].removeAll(possibleValues[i][j]);
							if (sizeBefore != possibleValues[i][c].size()) {
								updated = true;
							}
						}
					}
				}
			}
		}
		return updated;
	}
	
	//Needs optimization.
	private boolean xWing(int[][] board, List<Integer>[][] possibleValues) {
		boolean updated = false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				for (int v : possibleValues[i][j]) {
					for (int c = j+1; c < board.length; c++) {
						if (possibleValues[i][c].contains(v)) {
							for (int r = i+1; r < board.length; r++) {
								if (possibleValues[r][c].contains(v) && possibleValues[r][j].contains(v)) {
									boolean foundRow = false;
									boolean foundColumn = false;
									for (int k = 0; k < board.length; k++) {
										if (k != j && k != c && (possibleValues[i][k].contains(v) || possibleValues[r][k].contains(v))) {
											foundRow = true;
 										}
										if (k != i && k != r && (possibleValues[k][j].contains(v) || possibleValues[k][c].contains(v))) {
											foundColumn = true;
										}
									}
									if (foundRow != foundColumn) {
										for (int k = 0; k < board.length; k++) {
											if (!foundColumn && k != j && k != c) {
												int size1 = possibleValues[i][k].size();
												int size2 = possibleValues[r][k].size();
												possibleValues[i][k].remove((Integer)v);
												possibleValues[r][k].remove((Integer)v);
												if (size1 != possibleValues[i][k].size() || size2 != possibleValues[r][k].size()) {
													updated = true;
												}
	 										}
											if (!foundRow && k != i && k != r) {
												int size1 = possibleValues[k][j].size();
												int size2 = possibleValues[k][c].size();
												possibleValues[k][j].remove((Integer)v);
												possibleValues[k][c].remove((Integer)v);
												if (size1 != possibleValues[k][j].size() || size2 != possibleValues[k][c].size()) {
													updated = true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return updated;
	}
	
	private List<Integer>[] toMove(int x, int y, int value) {
		ArrayList<Integer> lv = new ArrayList<>();
		lv.add(value);
		return toMove(x, y, lv);
	}
	
	private List<Integer>[] toMove(int x, int y, ArrayList<Integer> values) {
		ArrayList<Integer> pos = new ArrayList<>();
		pos.add(x);
		pos.add(y);
		return new ArrayList[] {pos, values};
	}
	
	private boolean detectUnsolvable(int[][] board, List<Integer>[][] possibleValues) {
		int innerSquareSize = (int)Math.sqrt(board.length);
		boolean[][] foundColumn = new boolean[board.length][board.length];
		boolean[][] foundRow = new boolean[board.length][board.length];
		boolean[][] foundInnerSquare = new boolean[board.length][board.length];
		boolean foundPossibleMove = false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (possibleValues[i][j].size() > 0) {
					foundPossibleMove = true;
				}
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
		if (!foundPossibleMove) {
			return true;
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (!foundRow[i][j] || !foundColumn[i][j] || !foundInnerSquare[i][j]) {
					return true;
				}
			}
		}
		return false;
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
	
	public int[] makeOneMove() {
		List<int[][]> solvedSudoku = solve(1);
		if (solvedSudoku.size() > 0) {
			reset();
			List<Integer>[] move = makeMove(board, initializePossibleValues());
			if (move == null) {
				return null;
			}
			if (solvedSudoku.get(0)[move[0].get(0)][move[0].get(1)] == move[1].get(0)) {
				return new int[] {move[0].get(0), move[0].get(1), move[1].get(0)};
			}
			else {
				return new int[] {move[0].get(0), move[0].get(1), solvedSudoku.get(0)[move[0].get(0)][move[0].get(1)]};
			}
		}
		return null;
	}
	
}
