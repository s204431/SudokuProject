package sudoku;

import javax.swing.*;
import java.awt.*;

public class Main {
	public static final int SCREEN_HEIGHT = 800;
	public static final int SCREEN_WIDTH = 800;
	private static String title = "Sudoku";

	public static void main(String[] args) {
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setLocationRelativeTo(null);
		MainScreen ms = new MainScreen(frame);
	}
}