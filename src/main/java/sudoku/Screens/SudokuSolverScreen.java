package sudoku.Screens;

import MVC.Model.Mode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SudokuSolverScreen extends MenuScreen {
  private JLabel titleString;
  private JButton generateBtn;
  private JButton loadBtn;
  private JButton backBtn;

  public SudokuSolverScreen(JFrame frame) {
    super(frame);
  }

  public void addComponents() {
    // Title
    titleString = new JLabel("Sudoku Solver");
    setTitle(titleString);
    // Buttons
    generateBtn = new JButton("Generate New Sudoku");
    loadBtn = new JButton("Load Existing Sudoku");
    backBtn = new JButton("Back");
    setButtons(new JButton[] {generateBtn, loadBtn, backBtn});
    // Action listeners
    setActionListeners();
  }

  private void setActionListeners() {
    generateBtn.addActionListener(new generateNewAction());
    loadBtn.addActionListener(new loadGameAction());
    backBtn.addActionListener(new backAction());
  }

  class generateNewAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      changePanel();
      new GenerateNewSudokuScreen(frame, Mode.solver);
    }
  }

  class loadGameAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      changePanel();
      new LoadGameScreen(frame, Mode.solver);
    }
  }

  class backAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      changePanel();
      new MainScreen(frame);
    }
  }
}
