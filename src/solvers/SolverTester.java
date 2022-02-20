package solvers;

import java.util.Date;
import java.util.List;

import MVC.Model;

public class SolverTester {
	
	public void testAll(Model model) {
		//test(model, new BacktrackingSolver(model.board));
		//test(model, new RandomBacktrackingSolver(model.board));
		test(model, new EfficientSolver(model.board));
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
											   new TestCase("hard16", true)};
		boolean success = true;
		for (TestCase testCase : testCases) {
			model.load(testCase.fileName);
			solver.setBoard(model.board);
			long time1 = new Date().getTime();
			List<int[][]> solutions = solver.solve(1);
			long time2 = new Date().getTime();
			if (solver.recursiveCalls >= 500000) {
				System.out.println(solver.getClass().getSimpleName()+" took too long for test \""+testCase.fileName+"\".");
				success = false;
			}
			else if (testCase.solvable && solutions.size() > 0 && Model.sudokuSolved(solutions.get(0))) {
				System.out.println(solver.getClass().getSimpleName()+" passed test \""+testCase.fileName+"\" in "+(time2-time1)+" ms and "+solver.recursiveCalls+" recursive calls.");
			}
			else if (!testCase.solvable && solutions.size() == 0) {
				System.out.println(solver.getClass().getSimpleName()+" passed test \""+testCase.fileName+"\" in "+(time2-time1)+" ms and "+solver.recursiveCalls+" recursive calls.");
			}
			else {
				System.out.println(solver.getClass().getSimpleName()+" failed test \""+testCase.fileName+"\".");
				success = false;
			}
		}
		if (success) {
			System.out.println(solver.getClass().getSimpleName()+" passed all tests in "+(new Date().getTime()-start)+" ms.");
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
