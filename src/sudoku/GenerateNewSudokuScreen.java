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
    private JLabel titleString;
    private JLabel boardSizeString;
    private JLabel difficulyString;
    private JButton generateBtn;
    private JButton backBtn;
    private JComboBox<String> difficultyDDMenu;
    private int n;
    JSlider nSlider = new JSlider();

    private JLabel nLabel = new JLabel("5");
    private JPanel sliderPanel;
    private ChangeListener listener;

    public GenerateNewSudokuScreen(JFrame frame) {
        this.frame = frame;

        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
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
        titleString = new JLabel("Choose size and Difficulty");
        titleString.setFont(titleFont);
        //titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleString);

        add(Box.createRigidArea(new Dimension(0, spacing*2)));

        boardSizeString = new JLabel("Size of board");
        boardSizeString.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        boardSizeString.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(boardSizeString);

        addSlider();

        add(Box.createRigidArea(new Dimension(0, spacing)));

        difficulyString = new JLabel("Difficulty: ");
        difficulyString.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        difficulyString.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(difficulyString);

        add(Box.createRigidArea(new Dimension(0, spacing)));
    }

    private void addButtons() {
        generateBtn = new JButton("Generate Sudoku");
        generateBtn.addActionListener(new startAction());
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(generateBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        backBtn = new JButton("Back");
        backBtn.addActionListener(new backAction());
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(backBtn);
    }

    public void addSlider() {
        add(nLabel);
        listener = new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider) event.getSource();
                nLabel.setText("" + source.getValue());
            }
        };
        DefaultBoundedRangeModel model = new DefaultBoundedRangeModel(5, 0, 2, 10);
        nSlider = new JSlider(model);
        nSlider.setPaintTicks(true);
        nSlider.setMajorTickSpacing(2);
        nSlider.setMinorTickSpacing(1);
        nSlider.addChangeListener(listener);
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(200,50));
        panel.add(nSlider);
        panel.add(new JLabel("n"));
        add(panel);
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
            startGame(3, 3);    // TODO: Use chosen size of board
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
