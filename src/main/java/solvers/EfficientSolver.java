package solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import MVC.Model;
import sudoku.Field;

/*
	The EfficientSolver is an extension of the SudokuSolver.
	It solves sudokus using different techniques.

	Responsible: Jens
*/


public class EfficientSolver extends SudokuSolver {
	private boolean detectUnsolvable 	= true,
					useSingleCandidate  = true,
					useSinglePosition 	= true,
					useCandidateLines 	= true,
					useMultipleLines 	= true,
					useXWing 			= true,
					useSwordfish 		= true,
					useForcingChains 	= true;
	public boolean cancel = false;
	public List<String> hintNames = new ArrayList<String>();
	public List<int[][]> positionHints = new ArrayList<int[][]>();
	public List<int[][]> subBoxHints = new ArrayList<int[][]>();
	public List<int[]> rowHints = new ArrayList<int[]>();
	public List<int[]> columnHints = new ArrayList<int[]>();

	//Constructor taking a field 2D array.
	public EfficientSolver(Field[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}

	//Constructor taking an int 2D array.
	public EfficientSolver(int[][] board, int innerSquareSize) {
		super(board, innerSquareSize);
	}

	//Find up to maxSolutions different solutions to a sudoku. Returns all of the solutions.
	public List<int[][]> solve(int maxSolutions) {
		reset();
		solveRecursive(board, initializePossibleValues(), maxSolutions);
		if (solutions.size() == 0) {
			difficulty = 0;
		}
		else if (difficulty == 0) {
			difficulty = 1;
		}
		guesses--;
		return solutions;
	}

	//Recursively solves the given sudoku.
	private void solveRecursive(int[][] board, List<Integer>[][] possibleValues, int maxSolutions) {
		recursiveCalls++;
		guesses++;
		if (recursiveCalls > 500000 || cancel) {
			return;
		}
		List<Integer>[] move = makeMove(board, possibleValues);
		if (move == null) {
			if (Model.sudokuSolved(board, innerSquareSize)) {
				solutions.add(copyOf(board));
				solutionsFound++;
			}
			return;
		}
		if (move[1].size() == 1) {
			guesses--;
		}
		generateOrder(move[1]);
		//Creates a copy of the original board and inserts new values
		//depending on the moves obtained by the makeMove method.
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
	
	//Generate the correct order of values (does nothing in this case, used for subclasses).
	protected void generateOrder(List<Integer> values) {
		//Do nothing.
	}
	
	//Find one move towards solving the sudoku.
	public List<Integer>[] makeMove(int[][] board, List<Integer>[][] possibleValues) {
		if (detectUnsolvable && detectUnsolvable(board, possibleValues)) {
			return null;
		}
		//Uses the different solving techniques. It solves in
		//a difficulty order where first is the easiest and
		//last is the most difficult, ordered by top to bottom.
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
		if (useMultipleLines) {
			boolean updated = multipleLines(board, possibleValues);
			if (updated) {
				if (difficulty < 4) {
					difficulty = 4;	
				}
				return makeMove(board, possibleValues);
			}
		}
		if (useXWing && kEqualsN()) {
			boolean updated = xWing(board, possibleValues);
			if (updated) {
				if (difficulty < 5) {
					difficulty = 5;	
				}
				return makeMove(board, possibleValues);
			}
		}
		if (useSwordfish && kEqualsN()) {
			boolean updated = swordfish(board, possibleValues);
			if (updated) {
				if (difficulty < 6) {
					difficulty = 6;	
				}
				return makeMove(board, possibleValues);
			}
		}
		if (useForcingChains) {
			List<Integer>[] result = forcingChains(board, possibleValues);
			if (result != null) {
				if (difficulty < 7) {
					difficulty = 7;	
				}
				return result;
			}
		}
		//Make a guess if none of the techniques can be used.
		return makeGuess(board, possibleValues);
	}
	
	//Algorithm for single candidate technique.
	private List<Integer>[] singleCandidate(int[][] board, List<Integer>[][] possibleValues) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (possibleValues[i][j].size() == 1) {
					addHint("single candidate", new int[][] {{i, j}}, null, null, null);
					return toMove(i, j, possibleValues[i][j]);
				}
			}
		}
		return null;
	}
	//Algorithm for single position technique.
	private List<Integer>[] singlePosition(int[][] board, List<Integer>[][] possibleValues) {
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
					for (int k = i / innerSquareSize * innerSquareSize; k < i / innerSquareSize * innerSquareSize + innerSquareSize; k++) {
						for (int l = j / innerSquareSize * innerSquareSize; l < j / innerSquareSize * innerSquareSize + innerSquareSize; l++) {
							if ((k != i || l != j) && possibleValues[k][l].contains(v)) {
								found3 = true;
								break;
							}
						}
					}
					if ((!found1 && kEqualsN()) || (!found2 && kEqualsN()) || !found3) {
						if (!found1) {
							addHint("single position", new int[][] {{i, j}}, new int[] {i}, null, null);
						}
						else if (!found2) {
							addHint("single position", new int[][] {{i, j}}, null, new int[] {j}, null);
						}
						else {
							addHint("single position", new int[][] {{i, j}}, null, null, new int[][] {{i/innerSquareSize, j/innerSquareSize}});
						}
						return toMove(i, j, v);
					}
				}
			}
		}
		return null;
	}

	//Algorithm for candidate lines technique.
	private boolean candidateLines(int[][] board, List<Integer>[][] possibleValues) {
		boolean updated = false;
		for (int i = 0; i < getNumInnerSquares(); i++) {
			for (int j = 0; j < getNumInnerSquares(); j++) {
				for (int v = 1; v <= getMaxValue(); v++) {
					int foundInRow = -1;
					int foundInColumn = -1;
					for (int k = i * innerSquareSize; k < i * innerSquareSize + innerSquareSize; k++) {
						for (int l = j * innerSquareSize; l < j*innerSquareSize + innerSquareSize; l++) {
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
						boolean updated2 = false;
						List<int[]> positions = new ArrayList<int[]>();
						for (int l = 0; l < board.length; l++) {
							if (l < j * innerSquareSize || l >= j * innerSquareSize + innerSquareSize) {
								if (possibleValues[foundInRow][l].contains(v)) {
									possibleValues[foundInRow][l].remove((Integer) v);
									updated = true;
									updated2 = true;
								}
							}
							else if (possibleValues[foundInRow][l].contains(v)) {
								positions.add(new int[] {foundInRow, l});
							}
						}
						if (updated2) {
							int[][] positionsArray = new int[positions.size()][2];
							for (int p = 0; p < positions.size(); p++) {
								positionsArray[p] = positions.get(p);
							}
							addHint("candidate lines", positionsArray, new int[] {foundInRow}, null, null);
						}
					}
					else if (foundInRow < 0 && foundInColumn >= 0) {
						boolean updated2 = false;
						List<int[]> positions = new ArrayList<int[]>();
						for (int k = 0; k < board.length; k++) {
							if (k < i * innerSquareSize || k >= i * innerSquareSize + innerSquareSize) {
								if (possibleValues[k][foundInColumn].contains(v)) {
									possibleValues[k][foundInColumn].remove((Integer) v);
									updated = true;
									updated2 = true;
								}
							}
							else if (possibleValues[k][foundInColumn].contains(v)) {
								positions.add(new int[] {k, foundInColumn});
							}
						}
						if (updated2) {
							int[][] positionsArray = new int[positions.size()][2];
							for (int p = 0; p < positions.size(); p++) {
								positionsArray[p] = positions.get(p);
							}
							addHint("candidate lines", positionsArray, null, new int[] {foundInColumn}, null);
						}
					}
				}
			}
		}
		return updated;
	}

	//Algorithm for multiple lines technique.
	private boolean multipleLines(int[][] board, List<Integer>[][] possibleValues) {
		boolean updated = false;
		for (int i = 0; i < getNumInnerSquares(); i++) {
			for (int j = 0; j < getNumInnerSquares(); j++) {
				for (int v = 1; v <= getMaxValue(); v++) {
					List<Integer> foundRows = new ArrayList<>();
					for (int r = i*innerSquareSize; r < i*innerSquareSize+innerSquareSize; r++) {
						for (int c = j*innerSquareSize; c < j*innerSquareSize+innerSquareSize; c++) {
							if (possibleValues[r][c].contains(v) && !foundRows.contains(r)) {
								foundRows.add(r);
							}
						}
					}
					if (foundRows.size() > 1 && foundRows.size() < innerSquareSize) {
						List<Integer> foundInnerSquares = new ArrayList<>();
						for (int k = j+1; k < getNumInnerSquares(); k++) {
							List<Integer> foundRows2 = new ArrayList<>();
							for (int r = i*innerSquareSize; r < i*innerSquareSize+innerSquareSize; r++) {
								for (int c = k*innerSquareSize; c < k*innerSquareSize+innerSquareSize; c++) {
									if (possibleValues[r][c].contains(v) && !foundRows2.contains(r)) {
										foundRows2.add(r);
									}
								}
							}
							if (foundRows2.size() == foundRows.size()) {
								foundRows2.removeAll(foundRows);
								if (foundRows2.size() == 0) {
									foundInnerSquares.add(k);
								}
							}
						}
						if (foundInnerSquares.size() == foundRows.size()-1) {
							List<int[]> boxes = new ArrayList<int[]>();
							boolean updated2 = false;
							for (int k = 0; k < getNumInnerSquares(); k++) {
								boxes.add(new int[] {i, k});
								if (k != j && !foundInnerSquares.contains(k)) {
									for (int r = i*innerSquareSize; r < i*innerSquareSize+innerSquareSize; r++) {
										for (int c = k*innerSquareSize; c < k*innerSquareSize+innerSquareSize; c++) {
											if (foundRows.contains(r)) {
												int size = possibleValues[r][c].size();
												possibleValues[r][c].remove((Integer) v);
												if (size != possibleValues[r][c].size()) {
													updated = true;
													updated2 = true;
												}
											}
										}
									}
								}
							}
							if (updated2) {
								int[][] boxesArray = new int[boxes.size()][2];
								for (int p = 0; p < boxes.size(); p++) {
									boxesArray[p] = boxes.get(p);
								}
								addHint("multiple lines", null, null, null, boxesArray);	
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < getNumInnerSquares(); i++) {
			for (int j = 0; j < getNumInnerSquares(); j++) {
				for (int v = 1; v <= getMaxValue(); v++) {
					List<Integer> foundColumns = new ArrayList<>();
					for (int r = j*innerSquareSize; r < j*innerSquareSize+innerSquareSize; r++) {
						for (int c = i*innerSquareSize; c < i*innerSquareSize+innerSquareSize; c++) {
							if (possibleValues[r][c].contains(v) && !foundColumns.contains(c)) {
								foundColumns.add(c);
							}
						}
					}
					if (foundColumns.size() > 1 && foundColumns.size() < innerSquareSize) {
						List<Integer> foundInnerSquares = new ArrayList<>();
						for (int k = i+1; k < getNumInnerSquares(); k++) {
							List<Integer> foundColumns2 = new ArrayList<>();
							for (int r = k*innerSquareSize; r < k*innerSquareSize+innerSquareSize; r++) {
								for (int c = j*innerSquareSize; c < j*innerSquareSize+innerSquareSize; c++) {
									if (possibleValues[r][c].contains(v) && !foundColumns2.contains(c)) {
										foundColumns2.add(c);
									}
								}
							}
							if (foundColumns2.size() == foundColumns.size()) {
								foundColumns2.removeAll(foundColumns);
								if (foundColumns2.size() == 0) {
									foundInnerSquares.add(k);
								}
							}
						}
						if (foundInnerSquares.size() == foundColumns.size()-1) {
							List<int[]> boxes = new ArrayList<int[]>();
							boolean updated2 = false;
							for (int k = 0; k < getNumInnerSquares(); k++) {
								boxes.add(new int[] {k, j});
								if (k != i && !foundInnerSquares.contains(k)) {
									for (int r = k*innerSquareSize; r < k*innerSquareSize+innerSquareSize; r++) {
										for (int c = j*innerSquareSize; c < j*innerSquareSize+innerSquareSize; c++) {
											if (foundColumns.contains(c)) {
												int size = possibleValues[r][c].size();
												possibleValues[r][c].remove((Integer) v);
												if (size != possibleValues[r][c].size()) {
													updated = true;
													updated2 = true;
												}
											}
										}
									}	
								}
							}
							if (updated2) {
								int[][] boxesArray = new int[boxes.size()][2];
								for (int p = 0; p < boxes.size(); p++) {
									boxesArray[p] = boxes.get(p);
								}
								addHint("multiple lines", null, null, null, boxesArray);	
							}
						}
					}
				}
			}
		}
		return updated;
	}

	//Algorithm for naked pairs technique.
	//Not used due to running time being slow.
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
							possibleValues[r][j].removeAll(distinctValues);
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
							possibleValues[i][c].removeAll(distinctValues);
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

	//Algorithm for X-Wing technique.
	private boolean xWing(int[][] board, List<Integer>[][] possibleValues) {
		boolean updated = false;
		for (int v = 1; v <= getMaxValue(); v++) {
			for (int i = 0; i < board.length; i++) {
				int[] cs = exactlyTwo(possibleValues, i, v, true);
				if (cs[1] >= 0) {
					for (int k = i+1; k < board.length; k++) {
						int[] cs2 = exactlyTwo(possibleValues, k, v, true);
						if (cs[0] == cs2[0] && cs[1] == cs2[1]) {
							boolean updated2 = false;
							for (int r = 0; r < board.length; r++) {
								if (r != i && r != k) {
									int size1 = possibleValues[r][cs[0]].size();
									int size2 = possibleValues[r][cs[1]].size();
									possibleValues[r][cs[0]].remove((Integer)v);
									possibleValues[r][cs[1]].remove((Integer)v);
									if (size1 != possibleValues[r][cs[0]].size() || size2 != possibleValues[r][cs[1]].size()) {
										updated = true;
										updated2 = true;
									}
								}	
							}
							if (updated2) {
								addHint("X-Wing", new int[][] {{i, cs[0]}, {i, cs[1]}, {k, cs[0]}, {k, cs[1]}}, new int[] {i, k}, null, null);
							}
						}
					}
				}
				int[] rs = exactlyTwo(possibleValues, i, v, false);
				if (rs[1] >= 0) {
					for (int k = i+1; k < board.length; k++) {
						int[] rs2 = exactlyTwo(possibleValues, k, v, false);
						if (rs[0] == rs2[0] && rs[1] == rs2[1]) {
							boolean updated2 = false;
							for (int c = 0; c < board.length; c++) {
								if (c != i && c != k) {
									int size1 = possibleValues[rs[0]][c].size();
									int size2 = possibleValues[rs[1]][c].size();
									possibleValues[rs[0]][c].remove((Integer)v);
									possibleValues[rs[1]][c].remove((Integer)v);
									if (size1 != possibleValues[rs[0]][c].size() || size2 != possibleValues[rs[1]][c].size()) {
										updated = true;
										updated2 = true;
									}
								}
							}
							if (updated2) {
								addHint("X-Wing", new int[][] {{rs[0], i}, {rs[1], i}, {rs[0], k}, {rs[1], k}}, null, new int[] {i, k}, null);
							}
						}
					}
				}
			}
		}
		return updated;
	}

	//Algorithm for swordfish technique.
	private boolean swordfish(int[][] board, List<Integer>[][] possibleValues) {
		boolean updated = false;
		for (int v = 1; v <= getMaxValue(); v++) {
			for (int i = 0; i < board.length; i++) {
				int[] cs = exactlyTwo(possibleValues, i, v, true);
				if (cs[1] >= 0) {
					for (int k = i+1; k < board.length; k++) {
						int[] cs2 = exactlyTwo(possibleValues, k, v, true);
						if (cs2[1] >= 0) {
							int i1 = -1;
							int i2 = -1;
							if (cs[0] == cs2[0] && cs[1] != cs2[1]) {
								i1 = 1;
								i2 = 1;
							}
							else if (cs[0] == cs2[1]) {
								i1 = 1;
								i2 = 0;
							}
							else if (cs[1] == cs2[0]) {
								i1 = 0;
								i2 = 1;
							}
							else if (cs[1] == cs2[1] && cs[0] != cs2[0]) {
								i1 = 0;
								i2 = 0;
							}
							if (i1 >= 0) {
								for (int l = k+1; l < board.length; l++) {
									int[] cs3 = exactlyTwo(possibleValues, l, v, true);
									if (cs3[0] == cs[i1] && cs3[1] == cs2[i2]) {
										boolean updated2 = false;
										for (int r = 0; r < board.length; r++) {
											if (r != i && r != k && r != l) {
												int size1 = possibleValues[r][cs3[0]].size();
												int size2 = possibleValues[r][cs3[1]].size();
												int size3 = possibleValues[r][cs[i1 == 0 ? 1 : 0]].size();
												possibleValues[r][cs3[0]].remove((Integer)v);
												possibleValues[r][cs3[1]].remove((Integer)v);
												possibleValues[r][cs[i1 == 0 ? 1 : 0]].remove((Integer)v);
												if (size1 != possibleValues[r][cs3[0]].size() || size2 != possibleValues[r][cs3[1]].size() || size3 != possibleValues[r][cs[i1 == 0 ? 1 : 0]].size()) {
													updated = true;
													updated2 = true;
												}
											}	
										}
										if (updated2) {
											addHint("swordfish", new int[][] {{i, cs[0]}, {i, cs[1]}, {k, cs2[0]}, {k, cs2[1]}, {l, cs3[0]}, {l, cs3[1]}}, new int[] {i, k, l}, null, null);
										}
									}
								}
							}
						}
					}
				}
				int[] rs = exactlyTwo(possibleValues, i, v, false);
				if (rs[1] >= 0) {
					for (int k = i+1; k < board.length; k++) {
						int[] rs2 = exactlyTwo(possibleValues, k, v, false);
						if (rs2[1] >= 0) {
							int i1 = -1;
							int i2 = -1;
							if (rs[0] == rs2[0] && rs[1] != rs2[1]) {
								i1 = 1;
								i2 = 1;
							}
							else if (rs[0] == rs2[1]) {
								i1 = 1;
								i2 = 0;
							}
							else if (rs[1] == rs2[0]) {
								i1 = 0;
								i2 = 1;
							}
							else if (rs[1] == rs2[1] && rs[0] != rs2[0]) {
								i1 = 0;
								i2 = 0;
							}
							if (i1 >= 0) {
								for (int l = k+1; l < board.length; l++) {
									int[] rs3 = exactlyTwo(possibleValues, l, v, false);
									if (rs3[0] == rs[i1] && rs3[1] == rs2[i2]) {
										boolean updated2 = false;
										for (int c = 0; c < board.length; c++) {
											if (c != i && c != k && c != l) {
												int size1 = possibleValues[rs3[0]][c].size();
												int size2 = possibleValues[rs3[1]][c].size();
												int size3 = possibleValues[rs[i1 == 0 ? 1 : 0]][c].size();
												possibleValues[rs3[0]][c].remove((Integer)v);
												possibleValues[rs3[1]][c].remove((Integer)v);
												possibleValues[rs[i1 == 0 ? 1 : 0]][c].remove((Integer)v);
												if (size1 != possibleValues[rs3[0]][c].size() || size2 != possibleValues[rs3[1]][c].size() || size3 != possibleValues[rs[i1 == 0 ? 1 : 0]][c].size()) {
													updated = true;
													updated2 = true;
												}
											}	
										}
										if (updated2) {
											addHint("swordfish", new int[][] {{rs[0], i}, {rs[1], i}, {rs2[0], k}, {rs2[1], k}, {rs3[0], l}, {rs3[1], l}}, null, new int[] {i, k, l}, null);
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
	
	//Used for X-Wing and Swordfish to check if a row or column contains a specific possible value in exactly two fields.
	private int[] exactlyTwo(List<Integer>[][] possibleValues, int k, int v, boolean row) {
		int cr1 = -1;
		int cr2 = -1;
		for (int i = 0; i < board.length; i++) {
			if ((row && possibleValues[k][i].contains(v)) || (!row && possibleValues[i][k].contains(v))) {
				if (cr1 == -1) {
					cr1 = i;
				}
				else if (cr2 == -1) {
					cr2 = i;
				}
				else {
					cr2 = -2;
				}
			}
		}
		return new int[] {cr1, cr2};
	}

	//Algorithm for forcing chains technique.
	private List<Integer>[] forcingChains(int[][] board, List<Integer>[][] possibleValues) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (possibleValues[i][j].size() == 2) {
					List<int[]> forces1 = new ArrayList<>();
					List<int[]> forces2 = new ArrayList<>();
					int[][] board1 = copyOf(board);
					board1[i][j] = possibleValues[i][j].get(0);
					forcingChainsRecursive(board1, possibleValues, forces1, i, j);
					int[][] board2 = copyOf(board);
					board2[i][j] = possibleValues[i][j].get(1);
					forcingChainsRecursive(board2, possibleValues, forces2, i, j);
					//Compute intersection
					for (int[] a : forces1) {
						for (int[] a2 : forces2) {
							if (a[0] == a2[0] && a[1] == a2[1] && a[2] == a2[2]) {
								addHint("forcing chains", new int[][] {{a[0], a[1]}}, null, null, null);
								return toMove(a[0], a[1], a[2]);
							}
						}
					}
				}
			}
		}
		return null;
	}

	//Performs the forcing chains technique recursively.
	private void forcingChainsRecursive(int[][] board, List<Integer>[][] possibleValues, List<int[]> forces, int x, int y) {
		for (int i = 0; i < board.length; i++) {
			if (i != x) { //Look through column.
				if (board[i][y] <= 0 && possibleValues[i][y].size() == 2) {
					int k = possibleValues[i][y].indexOf(board[x][y]);
					if (k >= 0) {
						board[i][y] = possibleValues[i][y].get(k == 0 ? 1 : 0);
						forces.add(new int[] {i, y, board[i][y]});
						forcingChainsRecursive(board, possibleValues, forces, i, y);
					}
				}
			}
			if (i != y) { //Look through row.
				if (board[x][i] <= 0 && possibleValues[x][i].size() == 2) {
					int k = possibleValues[x][i].indexOf(board[x][y]);
					if (k >= 0) {
						board[x][i] = possibleValues[x][i].get(k == 0 ? 1 : 0);
						forces.add(new int[] {x, i, board[x][i]});
						forcingChainsRecursive(board, possibleValues, forces, x, i);
					}
				}
			}
		}
		for (int i = x/innerSquareSize*innerSquareSize; i < x/innerSquareSize*innerSquareSize+innerSquareSize; i++) {
			for (int j = y/innerSquareSize*innerSquareSize; j < y/innerSquareSize*innerSquareSize+innerSquareSize; j++) {
				if ((i != x || j != y)) {
					if (board[i][j] <= 0 && possibleValues[i][j].size() == 2) {
						int k = possibleValues[i][j].indexOf(board[x][y]);
						if (k >= 0) {
							board[i][j] = possibleValues[i][j].get(k == 0 ? 1 : 0);
							forces.add(new int[] {i, j, board[i][j]});
							forcingChainsRecursive(board, possibleValues, forces, i, j);
						}
					}
				}
			}
		}
	}

	//Algorithm for making a guess.
	private List<Integer>[] makeGuess(int[][] board, List<Integer>[][] possibleValues) {
		//Choose field with the least possible values.
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
		difficulty = 8;
		if (lowestX < 0 || lowestY < 0) {
			return null;
		}
		addHint("guessing", new int[][] {{lowestX, lowestY}}, null, null, null);
		return toMove(lowestX, lowestY, possibleValues[lowestX][lowestY]);
	}

	//Takes x, y and list of values and wraps them in a list of integers.
	private List<Integer>[] toMove(int x, int y, List<Integer> values) {
		List<Integer> pos = new ArrayList<>();
		pos.add(x);
		pos.add(y);
		return new List[] {pos, values};
	}

	//Overloaded toMove method that takes only a single value instead of a list.
	private List<Integer>[] toMove(int x, int y, int value) {
		ArrayList<Integer> lv = new ArrayList<>();
		lv.add(value);
		return toMove(x, y, lv);
	}
	
	//This method is used to detect if a sudoku is unsolvable.
	private boolean detectUnsolvable(int[][] board, List<Integer>[][] possibleValues) {
		boolean[][] foundColumn = new boolean[board.length][getMaxValue()];
		boolean[][] foundRow = new boolean[board.length][getMaxValue()];
		boolean[][] foundInnerSquare = new boolean[getNumInnerSquares() * getNumInnerSquares()][getMaxValue()];
		boolean foundPossibleMove = false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (possibleValues[i][j].size() > 0) {
					foundPossibleMove = true;
				}
				if (board[i][j] < 1) {
					for (int v : possibleValues[i][j]) {
						foundRow[i][v - 1] = true;
						foundColumn[j][v - 1] = true;
						foundInnerSquare[(i / innerSquareSize) * getNumInnerSquares() + (j / innerSquareSize)][v - 1] = true;
					}
				}
				else {
					foundRow[i][board[i][j] - 1] = true;
					foundColumn[j][board[i][j] - 1] = true;
					foundInnerSquare[(i / innerSquareSize) * getNumInnerSquares() + (j / innerSquareSize)][board[i][j] - 1] = true;
				}
			}
		}
		if (!foundPossibleMove) {
			return true;
		}
		for (int i = 0; i < board.length; i++) {
			int rowCount = 0;
			int colCount = 0;
			for (int j = 0; j < getMaxValue(); j++) {
				if (foundRow[i][j]) {
					rowCount++;
				}
				if (foundColumn[i][j]) {
					colCount++;
				}
			}
			if (rowCount < board.length || colCount < board.length) {
				return true;
			}
		}
		for (boolean[] ba : foundInnerSquare) {
			for (boolean b : ba) {
				if (!b) {
					return true;
				}
			}
		}
		return false;
	}

	//Initializes the data structure containing the possible values for each field.
	private List<Integer>[][] initializePossibleValues() {
		ArrayList[][] possibleValues = new ArrayList[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				possibleValues[i][j] = new ArrayList<Integer>();
				for (int k = 1; k <= getMaxValue(); k++) {
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
	
	//Update possibleValues when adding a new value on the board.
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
		for (int i = x / innerSquareSize * innerSquareSize; i < x / innerSquareSize * innerSquareSize + innerSquareSize; i++) {
			for (int j = y / innerSquareSize * innerSquareSize; j < y / innerSquareSize * innerSquareSize + innerSquareSize; j++) {
				if ((i != x || j != y)) {
					possibleValues[i][j].remove((Integer)value);
				}
			}
		}
	}

	//Makes a copy of the possibleValues data structure.
	private List<Integer>[][] copyOf(List<Integer>[][] possibleValues) {
		ArrayList<Integer>[][] newPossibleValues = new ArrayList[possibleValues.length][possibleValues[0].length];
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

	//Makes only one move towards solving the sudoku if a solution exists.
	//Returns the move as int array containing x, y and the value.
	public int[] makeOneMove() {
		List<int[][]> solvedSudoku = solve(1);
		resetHints();
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
	
	//Adds a hint with given name and positions, rows, columns and sub boxes to be marked.
	private void addHint(String name, int[][] positions, int[] rows, int[] columns, int[][] subBoxes) {
		hintNames.add(name);
		positionHints.add(positions == null ? new int[0][0] : positions);
		rowHints.add(rows == null ? new int[0] : rows);
		columnHints.add(columns == null ? new int[0] : columns);
		subBoxHints.add(subBoxes == null ? new int[0][0] : subBoxes);
	}
	
	//Resets the hints.
	private void resetHints() {
		hintNames = new ArrayList<String>();
		positionHints = new ArrayList<int[][]>();
		rowHints = new ArrayList<int[]>();
		columnHints = new ArrayList<int[]>();
		subBoxHints = new ArrayList<int[][]>();
	}
	
}
