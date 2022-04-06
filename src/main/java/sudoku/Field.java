package sudoku;

/*
    Field.java is a field in the sudoku board. Every field has a value that is decided when the sudoku board
    is initialized. Fields that are placed by the game and not by a player is not interactable, since the
    initial sudoku shouldn't be edited.
*/

public class Field {
	public static final double DEFAULT_WIDTH = 70.0;
	public static final double DEFAULT_HEIGHT = 70.0;
	public int value = 0;
	public int[] notes;
	public boolean interactable = true;
	public boolean clicked;
	public boolean highlighted;
	
	public Field() {
		this.notes = new int[9];
	}
	
	public Field(int value, boolean interactable) {
		this.value = value;
		this.interactable = interactable;
		this.notes = new int[9];
	}

	//Returns the value of this field.
	public int getValue() {
		return value;
	}
}
