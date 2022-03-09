package sudoku.Screens;

import MVC.Controller;
import MVC.Model;
import MVC.Model.Mode;
import MVC.View;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GenerateNewSudokuScreen extends MenuScreen {
    private JLabel titleLabel;
    private JLabel boardSizeLabel;
    private JLabel difficultyLabel;
    private JButton generateBtn;
    private JButton backBtn;
    private JComboBox<String> difficultyBox;
    private JSlider nSlider;
    private JSlider kSlider;
    private JLabel nLabel;
    private JLabel kLabel;
    private JPanel sliderPanel;
    private ChangeListener nListener;
    private ChangeListener kListener;
    private Mode mode;

    public GenerateNewSudokuScreen(JFrame frame, Mode mode) {
        super(frame);
        this.mode = mode;
    }

    public void addComponents(){
        addLabels();
        addButtons();
    }

    private void addLabels() {
        // Title
        titleLabel = new JLabel("Choose size and Difficulty");
        titleLabel.setFont(titleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createRigidArea(new Dimension(0, spacing*2)));

        boardSizeLabel = new JLabel("Size of board");
        boardSizeLabel.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        boardSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(boardSizeLabel);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        addSlider();

        add(Box.createRigidArea(new Dimension(0, spacing)));

        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(400,50));
        difficultyLabel = new JLabel("Difficulty: ");
        difficultyLabel.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(difficultyLabel);
        if (mode == Mode.create) {
            difficultyBox = new JComboBox(new String[]{"Empty", "Easy", "Medium", "Hard"});
        }
        else {
            difficultyBox = new JComboBox(new String[]{"Easy", "Medium", "Hard"});
        }
        panel.add(difficultyBox);
        add(panel);

        add(Box.createRigidArea(new Dimension(0, spacing)));
    }

    private void addButtons() {
        generateBtn = new JButton("Generate Sudoku");
        generateBtn.addActionListener(new startAction());
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(generateBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        backBtn = new JButton("Back");
        backBtn.addActionListener(new backAction());
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(backBtn);
    }

    public void addSlider() {
        nLabel = new JLabel("3");
        add(nLabel);

        nListener = setListener(nListener, nLabel);
        DefaultBoundedRangeModel model1 = new DefaultBoundedRangeModel(3, 0, 2, 10);
        nSlider = new JSlider(model1);
        nSlider.setPaintTicks(true);
        nSlider.setMajorTickSpacing(2);
        nSlider.setMinorTickSpacing(1);
        nSlider.addChangeListener(nListener);
        JPanel nPanel = new JPanel();
        nPanel.setMaximumSize(new Dimension(200,50));
        nPanel.add(nSlider);
        nPanel.add(new JLabel("n"));
        add(nPanel);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        kLabel = new JLabel("3");
        add(kLabel);

        kListener = setListener(kListener, kLabel);
        DefaultBoundedRangeModel model2 = new DefaultBoundedRangeModel(3, 0, 2, 10);
        kSlider = new JSlider(model2);
        kSlider.setPaintTicks(true);
        kSlider.setMajorTickSpacing(2);
        kSlider.setMinorTickSpacing(1);
        kSlider.addChangeListener(kListener);
        JPanel kPanel = new JPanel();
        kPanel.setMaximumSize(new Dimension(200,50));
        kPanel.add(kSlider);
        kPanel.add(new JLabel("k"));
        add(kPanel);
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
            switch (difficulty) {
            	case "Easy":
                    model.generateSudoku(1, 2, 0.62);
                    break;
            	case "Medium":
                    model.generateSudoku(3, 4, 0);
                    break;
            	case "Hard":
                    model.generateSudoku(5, 8, 0);
                    break;
                default:
                	break;
            }
        }
    }

    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            if (mode == Mode.play) {
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
