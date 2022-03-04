package solvers;

import java.util.Date;
import java.util.List;

import Generators.SudokuGenerator;
import MVC.Model;

public class SolverTester {
	
	private boolean includeRandom = true;
	private int numberOfRandomTests = 100;
	
	public void testAll(Model model) {
		//test(model, new BacktrackingSolver(model.board, model.innerSquareSize));
		//test(model, new RandomBacktrackingSolver(model.board, model.innerSquareSize));
		test(model, new EfficientSolver(model.board, model.innerSquareSize));
	}

	public void test(Model model, SudokuSolver solver) {
		long start = new Date().getTime();
		System.out.println("Testing "+solver.getClass().getSimpleName()+".");
		TestCase[] testCases = new TestCase[] {new TestCase("given", true),
											   new TestCase("empty", true),
											   new TestCase("smallunique", true),
											   new TestCase("unsolvable", false),
											   new TestCase("extreme1", true),
											   new TestCase("extreme2", true),
											   new TestCase("extreme3", true),
											   new TestCase("extreme4", true),
											   new TestCase("hard16", true),
											   new TestCase("sixbysix1", true),
											   new TestCase("difficulty3", true),
											   new TestCase("difficulty5", true),
											   new TestCase("difficulty6", true),
											   new TestCase("Puzzle_3_01", true),
											   new TestCase("Puzzle_3_02", true),
											   new TestCase("Puzzle_3_03", true),
											   new TestCase("Puzzle_3_04", true),
											   new TestCase("Puzzle_3_05", true),
											   new TestCase("Puzzle_3_06", true),
											   new TestCase("Puzzle_3_07", true),
											   new TestCase("Puzzle_3_08", true),
											   new TestCase("Puzzle_3_09", true),
											   new TestCase("Puzzle_3_10", true),
											   new TestCase("Puzzle_3_X1", false),
											   new TestCase("Puzzle_4_01", true),
											   new TestCase("Puzzle_4_02", true),
											   new TestCase("Puzzle_5_01", true),
											   new TestCase("Puzzle_6_01", true)};
		boolean success = true;
		for (TestCase testCase : testCases) {
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
				model.generateSudoku(1, 3, 3);
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
	}
	
	private class TestCase {
		public String fileName;
		public boolean solvable;
		
		public TestCase(String fileName, boolean solvable) {
			this.fileName = fileName;
			this.solvable = solvable;
		}
	}
	
}
