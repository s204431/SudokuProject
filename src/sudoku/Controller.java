package sudoku;

import java.awt.event.KeyEvent;

public class Controller {
	private Model model;
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public void keyTyped(KeyEvent e, int[] selectedFieldPosition) {
		if (e.getKeyChar() == 's') {
			model.solve(5);
		}
		else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			model.save("test");
		}
		else if (e.getKeyChar() == 'l') {
			model.load("test");
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
				if (newValue > 0 && newValue <= model.getBoardSize()) {
					model.setField(selectedFieldPosition[0], selectedFieldPosition[1], newValue);
				}
			}
		}
	}

}
