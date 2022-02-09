package sudoku;

public class Field {
	public static final int WIDTH = 70;
	public static final int HEIGHT = 70;
	public int value = -1;
	public boolean interactable = true;
	public boolean clicked;
	public boolean highlighted;
	
	public Field() {}
	
	public Field(int value, boolean interactable) {
		this.value = value;
		this.interactable = interactable;
	}
}
