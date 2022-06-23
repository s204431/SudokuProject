package sudoku;

import sudoku.Screens.MainScreen;

import javax.swing.*;
import java.awt.*;

/*
	Responsible: Gideon
 */

public class Main {
	public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int SCREEN_HEIGHT = screenSize.height - screenSize.height / 9;
	public static final int SCREEN_WIDTH = screenSize.width - screenSize.width / 2;
	public static final boolean DEBUG_MODE = false;
	private static String title = "Sudoku";

	/*
	 This class contains the start of the program.
	 */

	//This method is the first method called when the program starts.
	public static void main(String[] args) {
		restart();
	}
	
	//Restarts the game without displaying a message.
	public static void restart() {
		restart("");
	}

	//Restarts the program and displays a message.
	public static void restart(String message) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setLocationRelativeTo(null);
		if (message.equals("")) {
			MainScreen ms = new MainScreen(frame);	
		}
		else {
			MainScreen ms = new MainScreen(frame, message);
		}
	}
}