package testers;

import java.io.File;
import java.util.Date;
import java.util.List;

import MVC.Model;
import solvers.EfficientSolver;
import solvers.SudokuSolver;
import sudoku.Field;

/*
	This class is used to test sudoku solvers.

	Responsible: Jens
 */

public class SolverTester {
	
	private boolean includeRandom = false;
	private int numberOfRandomTests = 100;
	private boolean doTestCases = true;
	private boolean doTestSudokus = false;
	
	//Performs all tests
	public void testAll(Model model) {
		//test(model, new BacktrackingSolver(model.board, model.innerSquareSize));
		//test(model, new RandomBacktrackingSolver(model.board, model.innerSquareSize));
		//test(model, new RandomEfficientSolver(model.board, model.innerSquareSize));
		test(model, new EfficientSolver(model.board, model.innerSquareSize));
	}

	//Performs all tests on a specific solver.
	public void test(Model model, SudokuSolver solver) {
		long start = new Date().getTime();
		System.out.println("Testing "+solver.getClass().getSimpleName()+".");

		//These are the different test cases.
		SolverTestCase[] testCases = new SolverTestCase[] {new SolverTestCase("given", true),
											   new SolverTestCase("empty", true),
											   new SolverTestCase("smallunique", true),
											   new SolverTestCase("unsolvable", false),
											   new SolverTestCase("extreme1", true),
											   new SolverTestCase("extreme2", true),
											   new SolverTestCase("extreme3", true),
											   new SolverTestCase("extreme4", true),
											   new SolverTestCase("extreme5", true),
											   new SolverTestCase("hard16", true),
											   new SolverTestCase("sixbysix1", true),
											   new SolverTestCase("difficulty3", true),
											   new SolverTestCase("difficulty4", true),
											   new SolverTestCase("difficulty7", true),
											   new SolverTestCase("difficulty8", true),
											   new SolverTestCase("Puzzle_3_01", true),
											   new SolverTestCase("Puzzle_3_02", true),
											   new SolverTestCase("Puzzle_3_03", true),
											   new SolverTestCase("Puzzle_3_04", true),
											   new SolverTestCase("Puzzle_3_05", true),
											   new SolverTestCase("Puzzle_3_06", true),
											   new SolverTestCase("Puzzle_3_07", true),
											   new SolverTestCase("Puzzle_3_08", true),
											   new SolverTestCase("Puzzle_3_09", true),
											   new SolverTestCase("Puzzle_3_10", true),
											   new SolverTestCase("Puzzle_3_X1", false),
											   new SolverTestCase("Puzzle_4_01", true),
											   new SolverTestCase("Puzzle_4_02", true),
											   new SolverTestCase("Puzzle_5_01", true),
											   new SolverTestCase("Puzzle_6_01", true)};
		if (doTestCases) {
			boolean success = true;

			//Tests all test cases.
			for (SolverTestCase testCase : testCases) {
				model.loadAndUpdate(testCase.fileName);
				solver.setBoard(model.board, model.innerSquareSize);
				long time1 = new Date().getTime();
				List<int[][]> solutions = solver.solve(1);
				long time2 = new Date().getTime();
				if (solver.recursiveCalls >= 500000) {
					System.out.println(solver.getClass().getSimpleName()+" took too long for test \""+testCase.fileName+"\".");
					success = false;
				}
				else if (testCase.solvable && solutions.size() > 0 && Model.sudokuSolved(solutions.get(0), model.innerSquareSize)) {
					System.out.println(solver.getClass().getSimpleName()+" passed test \""+testCase.fileName+"\" in "+(time2-time1)+" ms, "+solver.recursiveCalls+" recursive calls and "+solver.guesses+" guesses. Difficulty: "+solver.difficulty+".");
				}
				else if (!testCase.solvable && solutions.size() == 0) {
					System.out.println(solver.getClass().getSimpleName()+" passed test \""+testCase.fileName+"\" in "+(time2-time1)+" ms, "+solver.recursiveCalls+" recursive calls and "+solver.guesses+" guesses. Difficulty: "+solver.difficulty+".");
				}
				else {
					System.out.println(solver.getClass().getSimpleName()+" failed test \""+testCase.fileName+"\".");
					success = false;
				}
			}
			long timeToGenerate = 0;
			if (includeRandom) {
				long totalTime = new Date().getTime();
				int recursiveCalls = 0;
				int guesses = 0;
				int sumOfDifficulties = 0;
				boolean successGenerated = true;
				for (int i = 0; i < numberOfRandomTests; i++) {
					long time1 = new Date().getTime();
					int[] range = SudokuSolver.getDifficultyRange();
					model.generateSudoku(range[0], range[1], 0.62, 3, 3);
					while (!model.generatingSudokuDone) {}
					System.out.println("asd");
					long time2 = new Date().getTime();
					timeToGenerate += time2 - time1;
					solver.setBoard(model.board, model.innerSquareSize);
					List<int[][]> solutions = solver.solve(1);
					if (solver.recursiveCalls >= 500000) {
						System.out.println(solver.getClass().getSimpleName()+" took too long for "+numberOfRandomTests+" random tests.");
						successGenerated = false;
						break;
					}
					else if (solutions.size() == 0) {
						System.out.println(solver.getClass().getSimpleName()+" failed "+numberOfRandomTests+" random tests.");
						successGenerated = false;
						break;
					}
					else {
						recursiveCalls += solver.recursiveCalls;
						guesses += solver.guesses;
						sumOfDifficulties += solver.difficulty;
					}
				}
				totalTime = new Date().getTime()-totalTime-timeToGenerate;
				if (successGenerated) {
					System.out.println(solver.getClass().getSimpleName()+" passed "+numberOfRandomTests+" random tests in "+totalTime+" ms, "+recursiveCalls+" recursive calls and "+guesses+" guesses. Average difficulty: "+sumOfDifficulties/(double)numberOfRandomTests+".");
				}
				else {
					success = false;
				}
			}
			if (success) {
				System.out.println(solver.getClass().getSimpleName()+" passed all tests in "+(new Date().getTime()-start-timeToGenerate)+" ms.");
			}
			else {
				System.out.println(solver.getClass().getSimpleName()+" failed tests.");
			}
		}
		if (doTestSudokus) {
			boolean success = true;
			String[] unsolvableSudokus = new String[] {};
	        File folder = new File("testsudokus");
	        File[] matchingFiles = folder.listFiles();
			long startTime = new Date().getTime();
	        for (File file : matchingFiles) {
	        	Object[] loaded = Model.load(file, model.mode);
				solver.setBoard((Field[][])loaded[0], (int)loaded[1]);
				long time1 = new Date().getTime();
				List<int[][]> solutions = solver.solve(1);
				long time2 = new Date().getTime();
				boolean unsolvable = false;
				for (String s : unsolvableSudokus) {
					if (s.equals(file.getName())) {
						unsolvable = true;
					}
				}
				if ((unsolvable && solutions.isEmpty()) || (!solutions.isEmpty() && Model.sudokuSolved(solutions.get(0), (int)loaded[1]))) {
					System.out.println("Test sudoku "+file.getName()+" succeeded in "+(time2-time1)+" ms, "+solver.recursiveCalls+" recursive calls and "+solver.guesses+" guesses. Difficulty: "+solver.difficulty+".");
				}
				else {
					System.out.println("Test sudoku "+file.getName()+" failed.");
					success = false;
				}
	        }
	        if (success) {
		        System.out.println("Test sudokus succeeded in "+(new Date().getTime()-startTime)+" ms.");
	        }
	        else {
		        System.out.println("Test sudokus failed.");	
	        }
		}
	}

	//This class represents a test case for the solver tester.
	private static class SolverTestCase {
		public String fileName;
		public boolean solvable;
		
		//Constructor taking the file name for the test sudoku and whether the sudoku is solvable.
		public SolverTestCase(String fileName, boolean solvable) {
			this.fileName = fileName;
			this.solvable = solvable;
		}
	}
}
