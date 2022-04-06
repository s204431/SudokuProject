package MVC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import solvers.*;
import sudoku.Field;
import Generators.*;


public class Model {
	private static long start;
	protected View view;
	public Field[][] board;
	public int innerSquareSize; //Width/height an inner square (n).
	public int numInnerSquares; //Number of inner squares in one side of the sudoku (k).
	public enum Mode {play, create, solver, multiplayer};
	public Mode mode = Mode.play;
	public String fileName = "";
	public boolean assistMode = false;
	protected int difficulty;

	//Constructor taking number of inner squares, inner square size and mode.
	public Model(int numInnerSquares, int innerSquareSize, Mode mode) {
		this.mode = mode;
		this.innerSquareSize = innerSquareSize;
		this.numInnerSquares = numInnerSquares;
		board = new Field[getBoardSize()][getBoardSize()];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				board[i][j] = new Field();
			}
		}
		resetTimer();
	}

	//Constructor that allows setting assist mode.
	public Model(int numInnerSquares, int innerSquareSize, Mode mode, boolean assistMode) {
		this(numInnerSquares, innerSquareSize, mode);
		this.assistMode = assistMode;
	}
	
	//Sets the reference to the view.
	public void setView(View view) {
		this.view = view;
	}
	
	//Returns the size of one side of the sudoku.
	public int getBoardSize() {
		return innerSquareSize*numInnerSquares;
	}
	
	//Set the field at position (x,y).
	public void setField(int x, int y, Field field) {
		board[x][y] = field;
		if (sudokuSolved(board, innerSquareSize)) {
			view.winPopup(difficulty);
		}
		view.repaint();
	}
	
	//Change the value of field at position (x,y).
	public void setField(int x, int y, int value) {
		if (!view.notesOn) {
			board[x][y].value = value;
		} else {
			if (value <= 9 && value > 0) {
				board[x][y].notes[value - 1] = board[x][y].notes[value - 1] == value ? 0 : value;
			}
		}
		if (sudokuSolved(board, innerSquareSize)) {
			view.winPopup(difficulty);
		}
		view.repaint();
	}
	
	//Checks if the current sudoku is solved. Takes a 2D integer array.
	public static boolean sudokuSolved(int[][] board, int innerSquareSize) {
		Field[][] toBeSolved = new Field[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				toBeSolved[i][j] = new Field(board[i][j], true);
			}
		}
		
		return sudokuSolved(toBeSolved, innerSquareSize);
	}
	
	//Checks if the current sudoku is solved. Takes a 2D field array.
	public static boolean sudokuSolved(Field[][] board, int innerSquareSize) {
		int boardSize = board.length;
		int numInnerSquares = boardSize/innerSquareSize;
		int maxValue = innerSquareSize*innerSquareSize;
		boolean[][] foundColumn = new boolean[boardSize][maxValue];
		boolean[][] foundRow = new boolean[boardSize][maxValue];
		boolean[][] foundInnerSquare = new boolean[numInnerSquares*numInnerSquares][maxValue];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j].value < 1 || board[i][j].value > maxValue) {
					return false;
				}
				else {
					int innerIndex = (i / innerSquareSize) * numInnerSquares + (j / innerSquareSize);
					if (foundRow[i][board[i][j].value-1] || foundColumn[j][board[i][j].value-1] || foundInnerSquare[innerIndex][board[i][j].value-1]) {
						return false;
					}
					foundRow[i][board[i][j].value-1] = true;
					foundColumn[j][board[i][j].value-1] = true;
					foundInnerSquare[innerIndex][board[i][j].value-1] = true;
				}
			}
		}
		return true;
	}
	
	//Find all solutions for the current sudoku.
	public void solve() {
		solve(Integer.MAX_VALUE);
	}
	
	//Find max solutions for the current sudoku. Update current sudoku to first solution found.
	public void solve(int maxSolutions) {
		SudokuSolver solver = new EfficientSolver(board, innerSquareSize);
		List<int[][]> results = solver.solve(maxSolutions);
		System.out.println("Found "+results.size()+" solutions. Took "+solver.recursiveCalls+" recursive calls and "+solver.guesses+" guesses. Difficulty: "+solver.difficulty+".");
		if (results.size() > 0) {
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					board[i][j].value = results.get(0)[i][j];
				}
			}
		}
		if (sudokuSolved(board, innerSquareSize)) {
			System.out.println("Solved!");
			view.winPopup(difficulty);
		}
		view.repaint();
	}
	
	//Save current sudoku to file with specific file name without saving the difficulty.
	public void save(String fileName) {
		save(fileName, -1);
	}
	
	//Save current sudoku to file with specific file name and save the give difficulty.
	public void save(String fileName, int difficulty) {
		File file = new File("savedsudokus/"+fileName+".su");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(numInnerSquares+";"+innerSquareSize+"\n");
			for (int i = 0; i < getBoardSize(); i++) {
				for (int j = 0; j < getBoardSize(); j++) {
					if (board[i][j].value <= 0) {
						writer.write(".");
					}
					else {
						writer.write(""+board[i][j].value);
					}
					if (j < getBoardSize()-1) {
						writer.write(";");
					}
				}
				if (i < getBoardSize()-1) {
					writer.write("\n");
				}
			}
			if (difficulty >= 0) {
				writer.write("\n"+difficulty);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Loads without updating the current sudoku. Returns loaded sudoku, innerSquareSize, numInnerSquares and difficulty (difficulty = -1 if it is not saved in the file).
	public static Object[] load(String fileName, Mode mode) {
		File file = new File("savedsudokus/"+fileName+".su");
		try {
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(Pattern.compile("[\\r\\n;]+"));
			int numInnerSquares = scanner.nextInt();
			int innerSquareSize = scanner.nextInt();
			int boardSize = numInnerSquares*innerSquareSize;
			Field[][] board = new Field[boardSize][boardSize];
			for (int i = 0; i < boardSize; i++) {
				for (int j = 0; j < boardSize; j++) {
					String next = scanner.next();
					if (next.equals(".")) {
						board[i][j] = new Field(0, true);
					}
					else {
						board[i][j] = new Field(Integer.parseInt(next), mode != Mode.play && mode != Mode.multiplayer);
					}
				}
			}
			int difficulty = scanner.hasNextInt() ? scanner.nextInt() : -1;
			scanner.close();
			return new Object[] {board, innerSquareSize, numInnerSquares, difficulty};
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Loads a sudoku from a file and updates the current sudoku to the loaded sudoku.
	public void loadAndUpdate(String fileName) {
		Object[] result = load(fileName, mode);
		board = (Field[][]) result[0];
		innerSquareSize = (int) result[1];
		numInnerSquares = (int) result[2];
		generateNotes();
		view.clickedPosition = new int[] {0, 0};
		view.resetBoardPosition();
		view.repaint();
		this.fileName = fileName;
	}
	
	//Checks if a value can be placed at a position in a sudoku (value is not blocked by same number on row, column or box).
	public static boolean canBePlaced(Field[][] board, int innerSquareSize, int x, int y, int value) {
		for (int i = 0; i < board.length; i++) {
			if ((i != y && board[x][i].value == value) || (i != x && board[i][y].value == value)) {
				return false;
			}
		}
		for (int i = x/innerSquareSize*innerSquareSize; i < x/innerSquareSize*innerSquareSize+innerSquareSize; i++) {
			for (int j = y/innerSquareSize*innerSquareSize; j < y/innerSquareSize*innerSquareSize+innerSquareSize; j++) {
				if ((i != x || j != y) && board[i][j].value == value) {
					return false;
				}
			}
		}
		return true;
	}
	
	//Generates a sudoku (same size as current sudoku) with specified minimum difficulty, maximum difficulty and minimum percentage of missing fields.
	public void generateSudoku(int minDifficulty, int maxDifficulty, double minMissingFieldsPercent) {
		generateSudoku(minDifficulty, maxDifficulty, minMissingFieldsPercent, innerSquareSize, numInnerSquares);
	}

	//Generates a sudoku with specified size, minimum difficulty, maximum difficulty and minimum percentage of missing fields.
	public void generateSudoku(int minDifficulty, int maxDifficulty, double minMissingFieldsPercent, int innerSquareSize, int numInnerSquares) {
		this.innerSquareSize = innerSquareSize;
		this.numInnerSquares = numInnerSquares;
		SudokuGenerator generator = new SudokuGenerator();
		int[][] matrix = generator.generateSudoku(innerSquareSize, numInnerSquares, minDifficulty, maxDifficulty, (int)(getBoardSize()*getBoardSize()*minMissingFieldsPercent));
		difficulty = generator.difficulty;
		board = new Field[getBoardSize()][getBoardSize()];
		for(int i = 0; i < getBoardSize(); i++) {
			for(int j = 0; j < getBoardSize(); j++){
				boolean interactable = matrix[i][j] <= 0 || (mode != Mode.play && mode != Mode.multiplayer);
				board[i][j] = new Field(matrix[i][j], interactable);
			}
		}
		generateNotes();
		view.resetBoardPosition();
		view.repaint();
	}

	// Adds notes of all number that are not on vertical, horizontal lines or in innersquares.
	public void generateNotes() {
		for (Field[] fields : board) {
			for (Field field : fields) {
				for (int k = 1; k <= 9; k++) {
					field.notes[k - 1] = k;
				}
			}
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].value > 0) {
					add(i, j, board[i][j].value);
				}
			}
		}
	}
	
	public void add(int x, int y, int value) {
		for (int i = 0; i < board.length; i++) {
			if (i != y && value <= 9 && value >= 1) {
				board[x][i].notes[value-1] = 0;
			}
			if (i != x && value <= 9 && value >= 1) {
				board[i][y].notes[value-1] = 0;
			}
		}
		for (int i = x/innerSquareSize*innerSquareSize; i < x/innerSquareSize*innerSquareSize+innerSquareSize; i++) {
			for (int j = y/innerSquareSize*innerSquareSize; j < y/innerSquareSize*innerSquareSize+innerSquareSize; j++) {
				if ((i != x || j != y) & value <= 9 && value >= 1) {
					board[i][j].notes[value-1] = 0;
				}
			}
		}
	}
	
	//Gives a hint by giving the correct value for one of the empty fields.
	public void giveHint() {
		int[] move = new EfficientSolver(board, innerSquareSize).makeOneMove();
		if (move != null) {
			setField(move[0], move[1], move[2]);	
		}
	}
	
	//Returns the sudoku board.
	public Field[][] getBoard() {
		Field[][] newBoard = new Field[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				newBoard[i][j] = new Field(board[i][j].value, board[i][j].interactable);
			}
		}
		return newBoard;
	}
	
	//Resets the timer.
	public static void resetTimer() {
		start = System.currentTimeMillis();
	}
	
	//Returns the current time on the timer.
    public static int elapsedTime() {
        long now = System.currentTimeMillis();
		return (int) ((now - start) / 1000.0);
    }
    
    //Returns the maximum number a field is allowed to have in the current sudoku.
    public int getMaxNumber() {
    	return innerSquareSize*innerSquareSize;
    }
	
    //Loads the stats from and returns it as an array of 8 integers.
    public static int[] loadStat() {
    	File file = new File("savedsudokus/Stats.st");
    	if(!file.exists()) {
    		try {
        		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        		for(int i = 0; i < 7; i++) {
        			writer.write("0 ");
        		}
        		writer.write("0");
        		writer.close();
        		
        	}catch (IOException e) {
        		e.printStackTrace();
        	}
    	}
    	
    	int[] stats = new int[8];
    	Scanner scanner = null;
    	try {
    		scanner = new Scanner(file);
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	for(int i = 0; i < 8; i++) {
    		stats[i] = scanner.nextInt();
    	}
    	return stats;
    	
    	
    }
    
    //Overrides stats file with a new time (if time beats best time) and number of solved sudoku for a difficulty.
    public static void saveStat(int time, int difficulty) {
    	int[] Stats = loadStat();
    	File file = new File("savedsudokus/Stats.st");
    	if(time < Stats[difficulty] || Stats[difficulty] == 0) {//change index for stats for correct difficulty (0-3) or (1-4)
    		Stats[difficulty] = time;
    	}
    	Stats[difficulty + 4]++;
    	try {
    		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    		for(int i = 0; i < 8; i++) {
    			writer.write(Stats[i] + " ");
    		}
    		writer.close();
    		
    	}catch (IOException e) {
    		e.printStackTrace();
    	}
    }
}

