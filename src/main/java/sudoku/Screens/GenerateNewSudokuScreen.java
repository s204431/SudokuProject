package sudoku.Screens;

import MVC.Controller;
import MVC.Model;
import MVC.Model.Mode;
import MVC.View;
import solvers.SudokuSolver;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GenerateNewSudokuScreen extends MenuScreen {
    private Boolean isMultiplayer;
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
    private JPanel sliderPanel;
    private ChangeListener nListener;
    private ChangeListener kListener;


    public GenerateNewSudokuScreen(JFrame frame, Mode mode) {
        super(frame, mode);
    }

    public void addComponents(){
        // Title
        titleLabel = new JLabel("Choose size and Difficulty");
        setTitle(titleLabel);

        boardSizeLabel = new JLabel("Size of board");
        setLabels(new JLabel[]{boardSizeLabel});

        addLabels();

        generateBtn = new JButton("Generate Sudoku");
        backBtn = new JButton("Back");
        setButtons(new JButton[]{generateBtn, backBtn});

        setActionListeners();
    }

    private void addLabels() {
        //Adding elements to sliders
        nLabel = new JLabel("3");
        kLabel = new JLabel("3");

        nListener = setListener(nListener, nLabel);
        kListener = setListener(kListener, kLabel);

        DefaultBoundedRangeModel model1 = new DefaultBoundedRangeModel(3, 0, 2, 10);
        DefaultBoundedRangeModel model2 = new DefaultBoundedRangeModel(3, 0, 2, 10);

        nSlider = new JSlider(model1);
        kSlider = new JSlider(model2);

        //Setting up the sliders
        setSliders(new JLabel[]{nLabel, kLabel},
                new ChangeListener[]{nListener, kListener},
                new JSlider[]{nSlider, kSlider},
                new String[]{"n", "k"});


        difficultyLabel = new JLabel("Difficulty: ");

        if (mode == Mode.create) {
        	String[] difficulties = SudokuSolver.getDifficultyStrings();
        	String[] choices = new String[difficulties.length+1];
        	choices[0] = "Empty";
        	for (int i = 0; i < difficulties.length; i++) {
        		choices[i+1] = difficulties[i];
        	}
            difficultyBox = new JComboBox(choices);
        }
        else {
            difficultyBox = new JComboBox(SudokuSolver.getDifficultyStrings());
        }

        setPanel(new JPanel(), new JComponent[]{difficultyLabel, difficultyBox});
    }

    private void setActionListeners(){
        generateBtn.addActionListener(new startAction());
        backBtn.addActionListener(new backAction());
    }

    public ChangeListener setListener(ChangeListener listener, JLabel label){
        listener = new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider) event.getSource();
                label.setText("" + source.getValue());
            }
        };
        return listener;
    }

    private Model startGame(int k, int n, Mode mode) {
        Model model = new Model(k, n, mode);
        View view = new View(model);
        Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
        return model;
    }

    class startAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (kSlider.getValue() > nSlider.getValue()) {
                return;
            }
            frame.dispose();
            Model model = startGame(kSlider.getValue(), nSlider.getValue(), mode);
            String difficulty = (String)difficultyBox.getSelectedItem();
            int[] range = SudokuSolver.getDifficultyRange(difficulty);
            if (range != null) {
            	model.generateSudoku(range[0], range[1], difficulty.equals("Easy") ? 0.50 : 0);
            }
        }
    }

    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            if (isMultiplayer) {
                isMultiplayer = false;
                new MultiplayerScreen(frame);
            }
            else if (mode == Mode.play) {
                new NewGameScreen(frame);
            }
            else if (mode == Mode.create) {
                new CreateSudokuScreen(frame);
            }
            else {
                new SudokuSolverScreen(frame);
            }
        }
    }
}