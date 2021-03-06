package MVC;

import java.awt.event.KeyEvent;

import solvers.SudokuSolver;
import sudoku.Main;
import testers.GeneratorTester;
import testers.SolverTester;

/*
	The controller of our MVC-module.
	This class receives user input from the view and calls appropriate methods on the model.

	Responsible: Magnus
*/

public class Controller {
	private Model model;
	private View view;
	
	//Sets the reference to the model.
	public void setModel(Model model) {
		this.model = model;
	}
	
	//Sets the reference to the view.
	public void setView(View view) {
		this.view = view;
	}
	
	//Handles a key typed by the user.
	public void keyTyped(KeyEvent e, int[] selectedFieldPosition) {
		//Solves sudoku when 's' is pressed. Only in debug mode.
		if (e.getKeyCode() == KeyEvent.VK_S && Main.DEBUG_MODE) {
			model.usedSolver = true;
			model.solve(1);
		}//Saves the sudoku when 'enter' is pressed. Only in debug mode.
		else if (e.getKeyCode() == KeyEvent.VK_ENTER && Main.DEBUG_MODE) {
			model.save(view.textField.getText());
		}//Loads sudoku file typed in the small text field when 'l' is pressed. Only in debug mode.
		else if (e.getKeyCode() == KeyEvent.VK_L && Main.DEBUG_MODE) {
			model.loadAndUpdate(view.textField.getText());
		}//Generates a new sudoku with when 'n' is pressed. Only in debug mode.
		else if (e.getKeyCode() == KeyEvent.VK_N && Main.DEBUG_MODE) {
			int[] range = SudokuSolver.getDifficultyRange();
			model.generateSudoku(range[0], range[1], 0.62);
		}//Shows your score as if solved when 'm' is pressed. Only in debug mode.
		else if (e.getKeyCode() == KeyEvent.VK_M && Main.DEBUG_MODE) {
			view.winPopup(model.difficulty);
		}//Solves different sudokus and prints the time when 't' is pressed. Only in debug mode.
		else if (e.getKeyCode() == KeyEvent.VK_T && Main.DEBUG_MODE) {
			new SolverTester().testAll(model);
		}//Generates different sudokus and prints the time when 'y' is pressed. Only in debug mode.
		else if (e.getKeyCode() == KeyEvent.VK_Y && Main.DEBUG_MODE) {
			new GeneratorTester().test(model);
		}//Gives a hint when 'h' is pressed. Only in debug mode.
		else if (e.getKeyCode() == KeyEvent.VK_H && Main.DEBUG_MODE) {
			model.giveHint();
		}
		else if (!model.board[selectedFieldPosition[0]][selectedFieldPosition[1]].interactable) {
			return;
		}//Deletes interactable field value when 'backspace' is pressed.
		else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			int value = model.board[selectedFieldPosition[0]][selectedFieldPosition[1]].value;
			if (value > 0) {
				model.setField(selectedFieldPosition[0], selectedFieldPosition[1], value/10);
			}
		}//Inserts a value in the selected field if the value is a digit and does not exceed the maximum value.
		else {
			char key = (char)e.getKeyCode();
			if (Character.isDigit(key)) {
				int newValue = Integer.parseInt(model.board[selectedFieldPosition[0]][selectedFieldPosition[1]].value+""+key);
				if (!view.notesOn && newValue > 0 && newValue <= model.innerSquareSize * model.innerSquareSize) {
					model.setField(selectedFieldPosition[0], selectedFieldPosition[1], newValue);
				} else if (view.notesOn && newValue > 0 && newValue <= 9) {
					model.setNote(selectedFieldPosition[0], selectedFieldPosition[1], newValue);
				}
			}
		}
	}
}
