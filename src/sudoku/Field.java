package sudoku;

public class Field {
	public static final int WIDTH = 5;
	public static final int HEIGHT = 5;
	public int value = -1;
	public boolean interactable = true;
	
	public Field() {}
	
	public Field(int value, boolean interactable) {
		this.value = value;
		this.interactable = interactable;
	}
}
