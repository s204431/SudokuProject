package testers;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import abbot.finder.ComponentNotFoundException;
import abbot.finder.Matcher;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.matchers.ClassMatcher;
import abbot.tester.ComponentTester;
import junit.extensions.abbot.ComponentTestFixture;
import sudoku.Main;
import sudoku.Screens.*;

public class UITester extends ComponentTestFixture {
	
	public void test() {
		Main.restart();
	    // Suppose MyComponent has a text field and a button...
		try {
			JFrame frame = (JFrame)getFinder().find(new ClassMatcher(JFrame.class));
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
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		} catch (MultipleComponentsFoundException e) {
			e.printStackTrace();
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
			ComponentTester tester = new ComponentTester();
			tester.actionClick(button);
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
