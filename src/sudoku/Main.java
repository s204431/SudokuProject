package sudoku;

public class Main {
	public static void main(String[] args) {
		View view = new View();
		Model model = new Model(3);
		Controller controller = new Controller();
		model.setView(view);
		controller.setModel(model);
		
		model.setField(5, 6, new Field(2, false));
		model.setField(3, 8, new Field(3, true));
	}
}
