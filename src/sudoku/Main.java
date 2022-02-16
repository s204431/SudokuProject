package sudoku;

public class Main {
	public static void main(String[] args) {
		//MainScreen ms = new MainScreen();
		Model model = new Model(3);
		View view = new View(model);
		Controller controller = new Controller();
		model.setView(view);
		controller.setModel(model);
		view.setController(controller);
	}
}