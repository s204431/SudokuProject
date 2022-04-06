package MVC;

import java.awt.event.KeyEvent;

import MVC.Model.Mode;
import solvers.SudokuSolver;
import sudoku.Main;
import testers.GeneratorTester;
import testers.SolverTester;

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
		if (e.getKeyChar() == 's' && Main.DEBUG_MODE) {
			model.solve(1);
		}
		else if (e.getKeyChar() == KeyEvent.VK_ENTER && Main.DEBUG_MODE) {
			model.save(view.textField.getText());
		}
		else if (e.getKeyChar() == 'l' && Main.DEBUG_MODE) {
			model.loadAndUpdate(view.textField.getText());
		}
		else if (e.getKeyChar() == 'n' && Main.DEBUG_MODE) {
			int[] range = SudokuSolver.getDifficultyRange();
			model.generateSudoku(range[0], range[1], 0.62);
			model.resetTimer();
		}
		else if (e.getKeyChar() == 'm' && Main.DEBUG_MODE) {
			view.winPopup(model.difficulty);
		}
		else if (e.getKeyChar() == 'a' && Main.DEBUG_MODE) {
			System.out.println(model.elapsedTime());
		}
		else if (e.getKeyChar() == 't' && Main.DEBUG_MODE) {
			new SolverTester().testAll(model);
		}
		else if (e.getKeyChar() == 'y' && Main.DEBUG_MODE) {
			new GeneratorTester().test(model);
		}
		else if (e.getKeyChar() == 'h' && Main.DEBUG_MODE) {
			model.giveHint();
		}
		else if (!model.board[selectedFieldPosition[0]][selectedFieldPosition[1]].interactable) {
			return;
		}
		else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
			int value = model.board[selectedFieldPosition[0]][selectedFieldPosition[1]].value;
			if (value > 0) {
				model.setField(selectedFieldPosition[0], selectedFieldPosition[1], value/10);
			}
		}
		else {
			char key = e.getKeyChar();
			if (Character.isDigit(key)) {
				int newValue = Integer.parseInt(model.board[selectedFieldPosition[0]][selectedFieldPosition[1]].value + "" + key);
				if (newValue > 0 && newValue <= model.innerSquareSize*model.innerSquareSize) {
					model.setField(selectedFieldPosition[0], selectedFieldPosition[1], newValue);
				}
			}
		}
	}

}
