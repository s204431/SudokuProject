package sudoku.Screens;

import MVC.Model;
import MVC.Model.Mode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import solvers.SudokuSolver;

public class GenerateNewSudokuScreen extends MenuScreen {
  private JLabel titleLabel;
  private JLabel boardSizeLabel;
  private JLabel difficultyLabel;
  private JButton generateBtn;
  private JButton backBtn;
  private JComboBox difficultyBox;
  private JSlider nSlider;
  private JSlider kSlider;
  private JLabel nLabel;
  private JLabel kLabel;
  private ChangeListener nListener;
  private ChangeListener kListener;
  private boolean assistMode;

  public GenerateNewSudokuScreen(JFrame frame, Mode mode) {
    super(frame, mode);
  }

  public GenerateNewSudokuScreen(JFrame frame, Mode mode, boolean assistMode) {
    this(frame, mode);
    this.assistMode = assistMode;
  }

  public void addComponents() {
    // Title
    titleLabel = new JLabel("Choose size and Difficulty");
    setTitle(titleLabel);
    // Label
    boardSizeLabel = new JLabel("Size of board");
    setLabels(new JLabel[] {boardSizeLabel});
    makeSliders();

    generateBtn = new JButton("Generate Sudoku");
    backBtn = new JButton("Back");
    setButtons(new JButton[] {generateBtn, backBtn});

    setActionListeners();
  }

  private void makeSliders() {
    // Adding elements to sliders
    nLabel = new JLabel("3");
    kLabel = new JLabel("3");

    nListener = setListener(nListener, nLabel);
    kListener = setListener(kListener, kLabel);

    DefaultBoundedRangeModel model1 = new DefaultBoundedRangeModel(3, 0, 2, 10);
    DefaultBoundedRangeModel model2 = new DefaultBoundedRangeModel(3, 0, 2, 10);

    nSlider = new JSlider(model1); // Can't have same model, since they
    kSlider = new JSlider(model2); // have to have 2 different values.

    // Setting up the sliders
    setSliders(
        new JLabel[] {nLabel, kLabel},
        new ChangeListener[] {nListener, kListener},
        new JSlider[] {nSlider, kSlider},
        new String[] {"n", "k"});

    difficultyLabel = new JLabel("Difficulty: ");
    if (mode == Mode.create) { // Adds the choice 'Empty' when the mode
      String[] difficulties =
          SudokuSolver.getDifficultyStrings(); // is 'create' to give the opportunity
      String[] choices = new String[difficulties.length + 1]; // to create your own sudoku.
      choices[0] = "Empty";
      System.arraycopy(difficulties, 0, choices, 1, difficulties.length);
      difficultyBox = new JComboBox(choices);
    } else {
      difficultyBox = new JComboBox(SudokuSolver.getDifficultyStrings());
    }
    setPanel(new JPanel(), new JComponent[] {difficultyLabel, difficultyBox});
  }

  private void setActionListeners() {
    generateBtn.addActionListener(new startAction());
    backBtn.addActionListener(new backAction());
  }

  public ChangeListener setListener(ChangeListener listener, JLabel label) {
    listener =
        new ChangeListener() {
          public void stateChanged(ChangeEvent event) {
            JSlider source = (JSlider) event.getSource();
            label.setText("" + source.getValue());
          }
        };
    return listener;
  }

  private Model startGame(int k, int n, Mode mode) {
    return getModel(k, n, mode, assistMode);
  }

  class startAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Model model;
      if (kSlider.getValue() > nSlider.getValue()) {
        return;
      } // Initializes game.
      frame.dispose(); // Depending on multiplayer
      if (mode == Mode.multiplayer) { // or single player this
        k = kSlider.getValue(); // creates a game instance.
        n = nSlider.getValue(); // It also finds the difficulty
        model = setMultiplayerInstance(true, ""); // chosen and uses this.
      } else {
        model = startGame(kSlider.getValue(), nSlider.getValue(), mode);
      }
      String difficulty = (String) difficultyBox.getSelectedItem();
      int[] range = SudokuSolver.getDifficultyRange(difficulty);
      if (range != null) {
        model.generateSudoku(range[0], range[1], difficulty.equals("Easy") ? 0.50 : 0);
      }
    }
  }

  class backAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      changePanel();
      backAction();
    }
  }
}
