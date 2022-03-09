package sudoku;

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

<<<<<<< HEAD
public class GenerateNewSudokuScreen extends MenuScreen {
=======
public class GenerateNewSudokuScreen extends JPanel {
    private String title = "Sudoku";
    private Font titleFont = new Font(Font.SERIF, Font.BOLD,50);
    private int btnHeight = 50;
    private int btnWidth = 200;
    private int spacing = 30;
    private JFrame frame;
>>>>>>> parent of 0139c6f (no message)
    private JLabel titleLabel;
    private JLabel boardSizeLabel;
    private JLabel difficultyLabel;
    private JButton generateBtn;
    private JButton backBtn;
    private JComboBox<String> difficultyBox;
<<<<<<< HEAD
    private JSlider nSlider;
    private JSlider kSlider;
    private JLabel nLabel;
    private JLabel kLabel;
=======
    private int n;
    private JSlider nSlider;
    private JSlider kSlider;

    private JLabel nLabel = new JLabel("3");
    private JLabel kLabel = new JLabel("3");
>>>>>>> parent of 0139c6f (no message)
    private JPanel sliderPanel;
    private ChangeListener nListener;
    private ChangeListener kListener;
    private Mode mode;

    public GenerateNewSudokuScreen(JFrame frame, Mode mode) {
<<<<<<< HEAD
        super(frame);
        this.mode = mode;
    }

    public void addComponents(){
=======
        this.frame = frame;
        this.mode = mode;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Add components
        addComponents();

        // Set frame
        frame.add(this);
        frame.setVisible(true);
    }

    private void addComponents(){
>>>>>>> parent of 0139c6f (no message)
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

<<<<<<< HEAD
=======

>>>>>>> parent of 0139c6f (no message)
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
<<<<<<< HEAD
        nLabel = new JLabel("3");
        add(nLabel);

        nListener = setListener(nListener, nLabel);
=======
        nListener = setListener(nListener, nLabel);

>>>>>>> parent of 0139c6f (no message)
        DefaultBoundedRangeModel model1 = new DefaultBoundedRangeModel(3, 0, 2, 10);
        nSlider = new JSlider(model1);
        nSlider.setPaintTicks(true);
        nSlider.setMajorTickSpacing(2);
        nSlider.setMinorTickSpacing(1);
        nSlider.addChangeListener(nListener);
<<<<<<< HEAD
=======

        kListener = setListener(kListener, kLabel);

        DefaultBoundedRangeModel model2 = new DefaultBoundedRangeModel(3, 0, 2, 10);
        kSlider = new JSlider(model2);
        kSlider.setPaintTicks(true);
        kSlider.setMajorTickSpacing(2);
        kSlider.setMinorTickSpacing(1);
        kSlider.addChangeListener(kListener);

        add(nLabel);

>>>>>>> parent of 0139c6f (no message)
        JPanel nPanel = new JPanel();
        nPanel.setMaximumSize(new Dimension(200,50));
        nPanel.add(nSlider);
        nPanel.add(new JLabel("n"));
<<<<<<< HEAD
=======

>>>>>>> parent of 0139c6f (no message)
        add(nPanel);

        add(Box.createRigidArea(new Dimension(0, spacing)));

<<<<<<< HEAD
        kLabel = new JLabel("3");
        add(kLabel);

        kListener = setListener(kListener, kLabel);
        DefaultBoundedRangeModel model2 = new DefaultBoundedRangeModel(3, 0, 2, 10);
        kSlider = new JSlider(model2);
        kSlider.setPaintTicks(true);
        kSlider.setMajorTickSpacing(2);
        kSlider.setMinorTickSpacing(1);
        kSlider.addChangeListener(kListener);
=======
        add(kLabel);

>>>>>>> parent of 0139c6f (no message)
        JPanel kPanel = new JPanel();
        kPanel.setMaximumSize(new Dimension(200,50));
        kPanel.add(kSlider);
        kPanel.add(new JLabel("k"));
<<<<<<< HEAD
        add(kPanel);
=======

        add(kPanel);

>>>>>>> parent of 0139c6f (no message)
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


    public void setUpMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new MainScreen.exitAction());

        JMenu difficulty = new JMenu("Difficulty");
        menuBar.add(difficulty);
        difficulty.add(new JMenuItem("Easy"));
        difficulty.add(new JMenuItem("Medium"));
        difficulty.add(new JMenuItem("Hard"));

        JMenu restart = new JMenu("Restart");
        menuBar.add(restart);
        restart.add(new JMenuItem("Same Sudoku"));
        restart.add(new JMenuItem("New Sudoku"));
    }

<<<<<<< HEAD
=======
    private void changePanel() {
        frame.remove(this);
    }

>>>>>>> parent of 0139c6f (no message)
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
<<<<<<< HEAD
                    model.generateSudoku(3, 4, 0);
                    break;
            	case "Hard":
                    model.generateSudoku(5, 8, 0);
=======
                    model.generateSudoku(3, 5, 0);
                    break;
            	case "Hard":
                    model.generateSudoku(6, 9, 0);
>>>>>>> parent of 0139c6f (no message)
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
<<<<<<< HEAD
=======

    static class exitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

>>>>>>> parent of 0139c6f (no message)
}
