package sudoku;

/*
    Objects of this class represents a field in a sudoku.
    It contains all information about a specific field.
*/

public class Field {
	public static final double DEFAULT_WIDTH  = 70.0,
							   DEFAULT_HEIGHT = 70.0;
	public int value = 0;
	public int[] notes = new int[9];
	public boolean interactable = true;
	public boolean clicked, highlighted;
	
	//Empty default constructor.
	public Field() {}
	
	//Constructor that takes the value of the field and if it is interactable.
	public Field(int value, boolean interactable) {
		this.value = value;
		this.interactable = interactable;
	}

	//Returns the value of this field.
	public int getValue() {
		return value;
	}
}
