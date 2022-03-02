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

public class GenerateNewSudokuScreen extends JPanel {
    private String title = "Sudoku";
    private Font titleFont = new Font(Font.SERIF, Font.BOLD,50);
    private int btnHeight = 50;
    private int btnWidth = 200;
    private int spacing = 30;
    private JFrame frame;
    private JLabel titleLabel;
    private JLabel boardSizeLabel;
    private JLabel difficultyLabel;
    private JButton generateBtn;
    private JButton backBtn;
    private JComboBox<String> difficultyDDMenu;
    private int n;
    private JSlider nSlider;
    private JSlider kSlider;

    private JLabel nLabel = new JLabel("5");
    private JLabel kLabel = new JLabel("5");
    private JPanel sliderPanel;
    private ChangeListener nListener;
    private ChangeListener kListener;

    public GenerateNewSudokuScreen(JFrame frame) {
        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Add components
        addComponents();

        // Set frame
        frame.add(this);
        frame.setVisible(true);
    }

    private void addComponents(){
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
        JComboBox difficultyBox = new JComboBox(new String[]{"Easy", "Medium", "Hard"});
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

    public void addSliders(){
    }

    public void addSlider() {
        nListener = new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider) event.getSource();
                if (source == nSlider) {
                    nLabel.setText("" + source.getValue());
                }
            }
        };
        DefaultBoundedRangeModel model1 = new DefaultBoundedRangeModel(5, 0, 2, 10);
        nSlider = new JSlider(model1);
        nSlider.setPaintTicks(true);
        nSlider.setMajorTickSpacing(2);
        nSlider.setMinorTickSpacing(1);
        nSlider.addChangeListener(nListener);

        kListener = new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider) event.getSource();
                if (source == kSlider ){
                    kLabel.setText("" + source.getValue());
                }
            }
        };
        DefaultBoundedRangeModel model2 = new DefaultBoundedRangeModel(5, 0, 2, 10);
        kSlider = new JSlider(model2);
        kSlider.setPaintTicks(true);
        kSlider.setMajorTickSpacing(2);
        kSlider.setMinorTickSpacing(1);
        kSlider.addChangeListener(kListener);

        add(nLabel);

        JPanel nPanel = new JPanel();
        nPanel.setMaximumSize(new Dimension(200,50));
        nPanel.add(nSlider);
        nPanel.add(new JLabel("n"));

        add(nPanel);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        add(kLabel);

        JPanel kPanel = new JPanel();
        kPanel.setMaximumSize(new Dimension(200,50));
        kPanel.add(kSlider);
        kPanel.add(new JLabel("k"));

        add(kPanel);

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

    private void changePanel() {
        frame.remove(this);
    }

    private Model startGame(int k, int n) {
        Model model = new Model(k, n, Mode.play);
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
            frame.dispose();
            if (kSlider.getValue() > nSlider.getValue()) {
                return;
            }
            startGame(kSlider.getValue(), nSlider.getValue());

            // TODO: Generate the sudoku
        }
    }

    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new NewGameScreen(frame);
        }
    }

    static class exitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

}
