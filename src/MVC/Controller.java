package MVC;

import java.awt.event.KeyEvent;

import MVC.Model.Mode;
import solvers.SolverTester;

public class Controller {
	private Model model;
	private View view;
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public void setView(View view) {
		this.view = view;
	}
	
	public void keyTyped(KeyEvent e, int[] selectedFieldPosition) {
		if (e.getKeyChar() == 's') {
			model.solve(1);
		}
		else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			model.save(view.textField.getText());
		}
		else if (e.getKeyChar() == 'l') {
			model.loadAndUpdate(view.textField.getText());
		}
		else if (e.getKeyChar() == 'n') {
			model.generateSudoku(6);
			Model.Stopwatch();
		}
		else if (e.getKeyChar() == 'a') {
			System.out.println(Model.elapsedTime());
		}
		else if (e.getKeyChar() == 't') {
			new SolverTester().testAll(model);
		}
		else if (e.getKeyChar() == 'h') {
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
				int newValue = Integer.parseInt(model.board[selectedFieldPosition[0]][selectedFieldPosition[1]].value+""+key);
				if (newValue > 0 && newValue <= model.innerSquareSize*model.innerSquareSize) {
					model.setField(selectedFieldPosition[0], selectedFieldPosition[1], newValue);
				}
			}
		}
	}

}
