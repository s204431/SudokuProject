package sudoku;

import java.awt.*;
import javax.swing.*;
import sudoku.Screens.MainScreen;

public class Main {
  public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
  public static final int SCREEN_HEIGHT = screenSize.height - screenSize.height / 9;
  public static final int SCREEN_WIDTH = screenSize.width - screenSize.width / 2;
  public static final boolean DEBUG_MODE = true;
  private static String title = "Sudoku";

  /*
   * When the main method calls restart()
   * this instance initializes a JFrame by creating
   * an instance of the Object MainScreen.
   * This MainScreen object is the base of
   * our programs logic and visualization
   */

  public static void main(String[] args) {
    restart();
  }

  public static void restart() {
    restart("");
  }

  public static void restart(String message) {
    JFrame frame = new JFrame(title);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    frame.pack();
    frame.setLocationByPlatform(true);
    frame.setLocationRelativeTo(null);
    if (message.equals("")) {
      MainScreen ms = new MainScreen(frame);
    } else {
      MainScreen ms = new MainScreen(frame, message);
    }
  }
}
