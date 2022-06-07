package testers;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.matchers.ClassMatcher;
import abbot.tester.JButtonTester;
import abbot.tester.JComponentTester;
import abbot.tester.JTextFieldTester;
import abbot.tester.JComboBoxTester;
import junit.extensions.abbot.ComponentTestFixture;
import multiplayer.MultiplayerView;
import multiplayer.MultiplayerModel;
import solvers.EfficientSolver;
import solvers.SudokuSolver;
import sudoku.Main;
import sudoku.Screens.*;
import MVC.*;

public class UITester extends ComponentTestFixture {
	private boolean testMainMenu = true;
	private boolean testPlayMode = true;
	private boolean testCreateMode = true;
	private boolean testSolverMode = true;
	private boolean testMultiplayerMode = false;
	private boolean monkeyTest = true;
	private String otherTesterIP = "10.209.128.1";

	public void testMainMenu() {
		if (!testMainMenu) {
			return;
		}
		try {
			Main.restart();
			checkPanelActive(MainScreen.class);

			//Test new game button.
			performButtonClickSequence(new String[] {"New Game", "Back", "New Game", "Generate New Sudoku", "Back", "Load Existing Sudoku", "Back", "Back"},
									   new Class<?>[] {NewGameScreen.class, MainScreen.class, NewGameScreen.class, GenerateNewSudokuScreen.class, NewGameScreen.class, LoadGameScreen.class, NewGameScreen.class, MainScreen.class});
			//Test multiplayer button.
			performButtonClickSequence(new String[] {"Multiplayer", "Back", "Multiplayer", "Host New Sudoku", "Back", "Host Old Sudoku", "Back", "Back"},
									   new Class<?>[] {MultiplayerScreen.class, MainScreen.class, MultiplayerScreen.class, GenerateNewSudokuScreen.class, MultiplayerScreen.class, LoadGameScreen.class, MultiplayerScreen.class, MainScreen.class});
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
		if (!testPlayMode) {
			return;
		}
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
		if (!testCreateMode) {
			return;
		}
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
		if (!testSolverMode) {
			return;
		}
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

	public void testMultiplayerMode() {
		if (!testMultiplayerMode) {
			return;
		}
		Main.restart();
		clickButton("Multiplayer");
		JTextFieldTester tester = new JTextFieldTester();
		tester.actionEnterText(getTextField(MultiplayerModel.getIP()), otherTesterIP);
		clickButton("Join");
		sleep(3000);
		boolean isHost = false;
		try {
			checkPanelActive(MainScreen.class);
			isHost = true;
		} catch (AssertionError ignored) {
		}
		if (isHost) {
			clickButton("OK");
			clickButton("Multiplayer");
			clickButton("Host New Sudoku");
			clickButton("Generate Sudoku");
			sleep(200);
			checkPanelActive(MultiplayerView.class);
			MultiplayerView view = (MultiplayerView)getPanel(MultiplayerView.class);
			sleep(1000);
			while (!((MultiplayerModel)view.model).started) {
				sleep(100);
			}
			sleep(3500);
			clickButton("Exit");
			sleep(500);
			checkPanelActive(MainScreen.class);
		}
		else {
			checkPanelActive(MultiplayerView.class);
			sleep(2000);
			checkPanelActive(MainScreen.class);
		}
	}

	public void testMonkey() {
		if (!monkeyTest) {
			return;
		}
		Main.restart();
		try {
			//Monkey test
			Robot robot = new Robot();
			JButtonTester buttonTester = new JButtonTester();
			int centerX = Main.screenSize.width/2;
			int centerY = Main.screenSize.height/2;
			Random r = new Random();
			for (int i = 0; i < 200; i++) {
				int num = r.nextInt(10);
				if (num > 3) {
					int randomX = ThreadLocalRandom.current().nextInt(centerX - Main.SCREEN_WIDTH/3, centerX + Main.SCREEN_WIDTH/3);
					int randomY = ThreadLocalRandom.current().nextInt(centerY - Main.SCREEN_HEIGHT/3, centerY + Main.SCREEN_HEIGHT/3);
					int mask = InputEvent.getMaskForButton(1);
					robot.mouseMove(randomX, randomY);
					robot.delay(20);
					robot.mousePress(mask);
					robot.delay(20);
					robot.mouseRelease(mask);
				}
				else if (num > 0) {
					int keyCode = r.nextInt(10)+48;
					robot.keyPress(keyCode);
					robot.delay(20);
					robot.keyRelease(keyCode);
				}
				else {
					List<JButton> buttons = new ArrayList<>();
					while (true) {
					    try {
					    	buttons.add((JButton)getFinder().find(new Matcher() {
							    public boolean matches(Component c) {
							        return c instanceof JButton && !buttons.contains(c);
							    }
							}));
						} catch (ComponentNotFoundException | MultipleComponentsFoundException e1) {
							break;
						}
					}
					if (buttons.size() > 0) {
						JButton button = buttons.get(r.nextInt(buttons.size()));
						try {
							buttonTester.click(button);
						} catch (Exception e2) {}
					}
					robot.delay(20);
				}
			}
		} catch(Exception e3) {
			sleep(5000);
			e3.printStackTrace();
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
		sleep(500);
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
		double initialBoardX = view.boardX;
		double initialBoardY = view.boardY;
		double initialFieldWidth = view.fieldWidth;
		double initialFieldHeight = view.fieldHeight;
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
		tester.click(view, (int)(view.boardX+y*view.fieldWidth+view.fieldWidth/2), (int)(view.boardY+x*view.fieldHeight+view.fieldHeight/2));
		assertEquals("Clicked x position incorrect.", x, view.clickedPosition[0]);
		assertEquals("Clicked y position incorrect.", y, view.clickedPosition[1]);
		tester.key('1');
		assertEquals("Wrong value in field.", 1, model.board[x][y].value);
		tester.key(KeyEvent.VK_BACK_SPACE);
		assertEquals("Wrong value in field.", 0, model.board[x][y].value);
		double boardX = view.boardX;
		double boardY = view.boardY;
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
		tester.click(view, (int)(view.boardX+y*view.fieldWidth+view.fieldWidth/2), (int)(view.boardY+x*view.fieldHeight+view.fieldHeight/2));
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
		assertTrue("Wrong board position.", view.fieldWidth == initialFieldWidth && view.fieldHeight == initialFieldHeight && view.boardX == initialBoardX && view.boardY == initialBoardY);
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
	
	private Dialog getDialog() {
		try {
			return (Dialog)getFinder().find(new ClassMatcher(Dialog.class));
		} catch (ComponentNotFoundException | MultipleComponentsFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private JComboBox getDropdown() {
		try {
			return (JComboBox)getFinder().find(new ClassMatcher(JComboBox.class));
		} catch (ComponentNotFoundException | MultipleComponentsFoundException e) {
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
		} catch (ComponentNotFoundException | MultipleComponentsFoundException e) {
		}
		assertTrue("Button "+buttonText+" does not exist.", false);
	}
	
	private JPanel getPanel(Class<?> panelClass) {
		try {
			return (JPanel)getFinder().find(new ClassMatcher(panelClass));
		} catch (ComponentNotFoundException | MultipleComponentsFoundException e) {
		}
		return null;
	}
	
	private JTextField getTextField(String text) {
	    try {
			JTextField tf = (JTextField)getFinder().find(new Matcher() {
			    public boolean matches(Component c) {
			        return c instanceof JTextField && ((JTextField)c).getText().equals(text);
			    }
			});
			return tf;
		} catch (ComponentNotFoundException | MultipleComponentsFoundException e) {
		}
		return null;
	}

}
