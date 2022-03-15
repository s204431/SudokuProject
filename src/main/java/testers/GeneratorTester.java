package testers;

import java.util.Date;

import Generators.SudokuGenerator;
import MVC.Model;
import solvers.EfficientSolver;
import solvers.SudokuSolver;

public class GeneratorTester {
	
	public void test(Model model) {
		long start = new Date().getTime();
		System.out.println("Testing generator...");
		GeneratorTestCase[] testCases = new GeneratorTestCase[] {new GeneratorTestCase(50, "Easy", 3, 3),
																 new GeneratorTestCase(5, "Medium", 3, 3),
																 new GeneratorTestCase(5, "Hard", 3, 3),
																 new GeneratorTestCase(5, "Extreme", 3, 3),
																 new GeneratorTestCase(50, "Easy", 3, 2),
																 new GeneratorTestCase(2, "Easy", 4, 4),
																 new GeneratorTestCase(2, "Medium", 4, 4),
																 new GeneratorTestCase(2, "Hard", 4, 4),
																 new GeneratorTestCase(2, "Extreme", 4, 4),
																 new GeneratorTestCase(1, "Easy", 5, 5),
																 new GeneratorTestCase(1, "Medium", 5, 5),
																 new GeneratorTestCase(1, "Hard", 5, 5),
																 new GeneratorTestCase(1, "Extreme", 5, 5),
																 new GeneratorTestCase(1, 3, 3, 3, 3)};
		int totalGenerated = 0;
		boolean passed = true;
		for (int i = 0; i < testCases.length; i++) {
			long timeStart = new Date().getTime();
			totalGenerated += testCases[i].amount;
			SudokuGenerator generator = new SudokuGenerator();
			boolean passedTestCase = true;
			for (int j = 0; j < testCases[i].amount; j++) {
				int[][] result;
				if (testCases[i].difficulty.equals("")) {
					result = generator.generateSudoku(testCases[i].innerSquareSize, testCases[i].numInnerSquares, testCases[i].minDifficulty, testCases[i].maxDifficulty, 0);
				}
				else {
					int[] range = SudokuSolver.getDifficultyRange(testCases[i].difficulty);
					result = generator.generateSudoku(testCases[i].innerSquareSize, testCases[i].numInnerSquares, range[0], range[1], 0);
				}
				SudokuSolver solver = new EfficientSolver(result, testCases[i].innerSquareSize);
				int[] range = testCases[i].difficulty.equals("") ? new int[] {testCases[i].minDifficulty, testCases[i].maxDifficulty} : SudokuSolver.getDifficultyRange(testCases[i].difficulty);
				if (solver.solve(2).size() != 1 || !(solver.difficulty >= range[0] && solver.difficulty <= range[1])) {
					passedTestCase = false;
					break;
				}
			}
			long time = new Date().getTime() - timeStart;
			int size = (testCases[i].innerSquareSize*testCases[i].numInnerSquares);
			if (passedTestCase) {
				int averageTime = (int)(time/testCases[i].amount);
				if (testCases[i].difficulty.equals("")) {
					System.out.println("Generator passed "+testCases[i].amount+" "+size+"x"+size+" sudokus of difficulty "+testCases[i].minDifficulty+" to "+testCases[i].maxDifficulty+" in "+time+" ms. Average time: "+averageTime+" ms per sudoku.");
				}
				else {
					System.out.println("Generator passed "+testCases[i].amount+" "+size+"x"+size+" sudokus of difficulty "+testCases[i].difficulty+" in "+time+" ms. Average time: "+averageTime+" ms per sudoku.");
				}
			}
			else {
				passed = false;
				if (testCases[i].difficulty.equals("")) {
					System.out.println("Generator failed "+testCases[i].amount+" "+size+"x"+size+" sudokus of difficulty "+testCases[i].minDifficulty+" to "+testCases[i].maxDifficulty+".");
				}
				else {
					System.out.println("Generator failed "+testCases[i].amount+" "+size+"x"+size+" sudokus of difficulty "+testCases[i].difficulty+".");
				}
			}
		}
		long totalTime = new Date().getTime() - start;
		int averageTime = (int)(totalTime/totalGenerated);
		if (passed) {
			System.out.println("Generator passed all tests in "+totalTime+" ms. Average time: "+averageTime+" ms per sudoku.");
		}
		else {
			System.out.println("Generator failed tests.");
		}
	}
	
	private class GeneratorTestCase {
		public int amount = 0;
		public String difficulty = "";
		public int minDifficulty = 0; //Only used if difficulty is not specified.
		public int maxDifficulty = 0; //Only used if difficulty is not specified.
		public int innerSquareSize = 0;
		public int numInnerSquares = 0;
		
		public GeneratorTestCase(int amount, String difficulty, int innerSquareSize, int numInnerSquares) {
			this.amount = amount;
			this.difficulty = difficulty;
			this.innerSquareSize = innerSquareSize;
			this.numInnerSquares = numInnerSquares;
		}
		
		public GeneratorTestCase(int amount, int minDifficulty, int maxDifficulty, int innerSquareSize, int numInnerSquares) {
			this.amount = amount;
			this.minDifficulty = minDifficulty;
			this.maxDifficulty = maxDifficulty;
			this.innerSquareSize = innerSquareSize;
			this.numInnerSquares = numInnerSquares;
		}
	}

}
