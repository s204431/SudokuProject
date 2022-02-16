package sudoku;

public class Main {
	public static void main(String[] args) {
<<<<<<< HEAD
		MainScreen ms = new MainScreen();
=======
		//MainScreen ms = new MainScreen();
		Model model = new Model(3);
		View view = new View(model);
		Controller controller = new Controller();
		model.setView(view);
		controller.setModel(model);
		view.setController(controller);
>>>>>>> cf4b88b7014512de9b30e4cc4b3294190beab291
	}
}