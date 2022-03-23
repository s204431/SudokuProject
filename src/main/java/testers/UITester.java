package testers;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalComboBoxButton;

import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.matchers.ClassMatcher;
import abbot.tester.JButtonTester;
import abbot.tester.JComponentTester;
import abbot.tester.JComboBoxTester;
import junit.extensions.abbot.ComponentTestFixture;
import solvers.EfficientSolver;
import solvers.SudokuSolver;
import sudoku.Field;
import sudoku.Main;
import sudoku.Screens.*;
import MVC.*;

public class UITester extends ComponentTestFixture {

	public void testMainMenu() {
		try {
			Main.restart();
			checkPanelActive(MainScreen.class);
			//Test new game button.
			performButtonClickSequence(new String[] {"New Game", "Back", "New Game", "Generate New Sudoku", "Back", "Load Existing Sudoku", "Back", "Back"},
									   new Class<?>[] {NewGameScreen.class, MainScreen.class, NewGameScreen.class, GenerateNewSudokuScreen.class, NewGameScreen.class, LoadGameScreen.class, NewGameScreen.class, MainScreen.class});
			//Test multiplayer button.
			//performButtonClickSequence(new String[] {"Multiplayer", "Back", "Multiplayer", "Host New Sudoku", "Back", "Host Old Sudoku", "Back", "Back"},
			//						   new Class<?>[] {MultiplayerScreen.class, MainScreen.class, MultiplayerScreen.class, GenerateNewSudokuScreen.class, MultiplayerScreen.class, LoadGameScreen.class, MultiplayerScreen.class, MainScreen.class});
			//Test create sudoku button.
			performButtonClickSequence(new String[] {"Create Sudoku", "Back", "Create Sudoku", "Generate New Sudoku", "Back", "Load Existing Sudoku", "Back", "Back"},
									   new Class<?>[] {CreateSudokuScreen.class, MainScreen.class, CreateSudokuScreen.class, GenerateNewSudokuScreen.class, CreateSudokuScreen.class, LoadGameScreen.class, CreateSudokuScreen.class, MainScreen.class});
			//Test sudoku solver button.
			performButtonClickSequence(new String[] {"Sudoku Solver", "Back", "Sudoku Solver", "Generate New Sudoku", "Back", "Load Existing Sudoku", "Back", "Back"},
									   new Class<?>[] {SudokuSolverScreen.class, MainScreen.class, SudokuSolverScreen.class, GenerateNewSudokuScreen.class, SudokuSolverScreen.class, LoadGameScreen.class, SudokuSolverScreen.class, MainScreen.class});	
		} catch (AssertionError e) {
			sleep(2000);
			throw e;
		}
	}
	
	public void testPlayMode() {
		try {
			Main.restart();
			for (String difficulty : SudokuSolver.getDifficultyStrings()) {
				checkDifficulty(difficulty, "New Game", false, "Easy", "Medium", "Hard", "Extreme");
				clickButton("Exit");
				sleep(200);
				checkPanelActive(MainScreen.class);
			}
		} catch (AssertionError e) {
			sleep(2000);
			throw e;
		}
	}
	
	public void testCreateMode() {
		try {
			Main.restart();
			checkDifficulty("Easy", "Create Sudoku", true, "Empty", "Easy", "Medium", "Hard", "Extreme");
			clickButton("Exit");
			sleep(200);
			checkPanelActive(MainScreen.class);
		} catch (AssertionError e) {
			sleep(2000);
			throw e;
		}
	}
	
	public void testSolverMode() {
		try {
			Main.restart();
			checkDifficulty("Easy", "Sudoku Solver", true, "Easy", "Medium", "Hard", "Extreme");
			clickButton("Exit");
			sleep(200);
			checkPanelActive(MainScreen.class);
		} catch (AssertionError e) {
			sleep(2000);
			throw e;
		}
	}
	
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void checkDifficulty(String difficulty, String initialButtonName, boolean allowRemovingInitialValues, String... expectedChoices) {
		clickButton(initialButtonName);
		clickButton("Generate New Sudoku");
		checkDifficultyDropdown(expectedChoices);
		selectDropdownValue(getDropdown(), difficulty);
		clickButton("Generate Sudoku");
		checkPanelActive(View.class);
		View view = (View) getPanel(View.class);
		Model model = view.model;
		SudokuSolver solver = new EfficientSolver(model.board, model.innerSquareSize);
		List<int[][]> solutions = solver.solve(1);
		assertEquals("Wrong number of solutions to sudoku.", 1, solutions.size());
		assertEquals("Sudoku has wrong difficulty.", difficulty, SudokuSolver.getDifficultyString(solver.difficulty));
		checkSudokuActions(view, model, allowRemovingInitialValues);
	}
	
	private void checkSudokuActions(View view, Model model, boolean allowRemovingInitialValues) {
		int initialBoardX = view.boardX;
		int initialBoardY = view.boardY;
		int x = -1;
		int y = -1;
		for (int i = 0; i < model.board.length; i++) {
			boolean stop = false;
			for (int j = 1; j < model.board[0].length; j++) {
				if (model.board[i][j].value == 0) {
					x = i;
					y = j;
					stop = true;
					break;
				}
			}
			if (stop) {
				break;
			}
		}
		JComponentTester tester = new JComponentTester();
		tester.click(view, view.boardX+y*view.fieldWidth+view.fieldWidth/2, view.boardY+x*view.fieldHeight+view.fieldHeight/2);
		assertEquals("Clicked x position incorrect.", x, view.clickedPosition[0]);
		assertEquals("Clicked y position incorrect.", y, view.clickedPosition[1]);
		tester.key('1');
		assertEquals("Wrong value in field.", 1, model.board[x][y].value);
		tester.key(KeyEvent.VK_BACK_SPACE);
		assertEquals("Wrong value in field.", 0, model.board[x][y].value);
		int boardX = view.boardX;
		int boardY = view.boardY;
		tester.drag(view, 50, 50);
		tester.mouseRelease();
		assertTrue("Wrong board x position.", view.boardX != boardX);
		assertTrue("Wrong board y position.", view.boardY != boardY);
		x = -1;
		y = -1;
		for (int i = 0; i < model.board.length; i++) {
			boolean stop = false;
			for (int j = 1; j < model.board[0].length; j++) {
				if (model.board[i][j].value != 0) {
					x = i;
					y = j;
					stop = true;
					break;
				}
			}
			if (stop) {
				break;
			}
		}
		tester.click(view, view.boardX+y*view.fieldWidth+view.fieldWidth/2, view.boardY+x*view.fieldHeight+view.fieldHeight/2);
		assertEquals("Clicked x position incorrect.", x, view.clickedPosition[0]);
		assertEquals("Clicked y position incorrect.", y, view.clickedPosition[1]);
		tester.key('1');
		assertTrue("Wrong value in field.", model.board[x][y].value > 0);
		tester.key(KeyEvent.VK_BACK_SPACE);
		if (allowRemovingInitialValues) {
			assertEquals("Wrong value in field.", 0, model.board[x][y].value);	
		}
		else {
			assertTrue("Wrong value in field.", model.board[x][y].value > 0);
		}
		tester.key(KeyEvent.VK_SPACE);
		assertTrue("Wrong board position.", view.fieldWidth == Field.DEFAULT_WIDTH && view.fieldHeight == Field.DEFAULT_HEIGHT && view.boardX == initialBoardX && view.boardY == initialBoardY);
	}
	
	private void selectDropdownValue(JComboBox dropdown, String value) {
		JComboBoxTester tester = new JComboBoxTester();
		int index = -1;
		for (int i = 0; i < dropdown.getItemCount(); i++) {
			if (dropdown.getItemAt(i).equals(value)) {
				index = i;
			}
		}
		tester.actionSelectIndex(dropdown, index);
	}
	
	private JComboBox getDropdown() {
		try {
			return (JComboBox)getFinder().find(new ClassMatcher(JComboBox.class));
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		} catch (MultipleComponentsFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void checkDifficultyDropdown(String[] expectedChoices) {
		JComboBox dropdown = getDropdown();
		assertNotNull("Difficulty dropdown does not exist.", dropdown);
		assertEquals("Wrong choices in difficulty dropdown.", expectedChoices.length, dropdown.getItemCount());
		for (int i = 0; i < expectedChoices.length; i++) {
			assertTrue("Wrong choices in difficulty dropdown.", expectedChoices[i].equals(dropdown.getItemAt(i)));
		}
	}
	
	private void performButtonClickSequence(String[] buttonTexts, Class<?>[] expectedPanelClasses) {
		assertTrue("Buttons and expected panel classes must have same lengts.", buttonTexts.length == expectedPanelClasses.length);
		for (int i = 0; i < buttonTexts.length; i++) {
			clickButton(buttonTexts[i]);
			checkPanelActive(expectedPanelClasses[i]);
		}
	}
	
	private void checkPanelActive(Class<?> panelClass) {
		assertNotNull("Wrong panel active. Expected "+panelClass+".", getPanel(panelClass));
	}
	
	private void clickButton(String buttonText) {
	    try {
			JButton button = (JButton)getFinder().find(new Matcher() {
			    public boolean matches(Component c) {
			        // Add as much information as needed to distinguish the component
			        return c instanceof JButton && ((JButton)c).getText().equals(buttonText);
			    }
			});
			JButtonTester tester = new JButtonTester();
			tester.click(button);
			return;
		} catch (ComponentNotFoundException e) {
		} catch (MultipleComponentsFoundException e) {
		}
		assertTrue("Button "+buttonText+" does not exist.", false);
	}
	
	private JPanel getPanel(Class<?> panelClass) {
		try {
			return (JPanel)getFinder().find(new ClassMatcher(panelClass));
		} catch (ComponentNotFoundException e) {
		} catch (MultipleComponentsFoundException e) {
		}
		return null;
	}
	
	private JTextField getTextField(String text) {
	    try {
			JTextField tf = (JTextField)getFinder().find(new Matcher() {
			    public boolean matches(Component c) {
			        // Add as much information as needed to distinguish the component
			        return c instanceof JTextField && ((JTextField)c).getText().equals(text);
			    }
			});
			return tf;
		} catch (ComponentNotFoundException e) {
		} catch (MultipleComponentsFoundException e) {
		}
		return null;
	}

}