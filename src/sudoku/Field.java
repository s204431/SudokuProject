package sudoku;

public class Field {
	public static final int DEFAULT_WIDTH = 70;
	public static final int DEFAULT_HEIGHT = 70;
	public int value = 0;
	public boolean interactable = true;
	public boolean clicked;
	public boolean highlighted;
	
	public Field() {}
	
	public Field(int value, boolean interactable) {
		this.value = value;
		this.interactable = interactable;
	}

	public int getValue() {
		return value;
	}
}
