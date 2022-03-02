package sudoku;
import MVC.Controller;
import MVC.Model;
import MVC.View;
import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainScreen extends JPanel{

    private Font titleFont = new Font(Font.SERIF, Font.BOLD,50);
    private int textSize = 100;
    private int spacing = 30;
    private int k;
    private int n;
    private JFrame frame;
    private JLabel titleString;
    private JTextField kText;
    private JTextField nText;
    private JButton playBtn;
    private JButton newGameBtn;
    private JButton createBtn;
    private JButton solverBtn;
    private JButton highScoreBtn;


    public MainScreen (JFrame frame) {
        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Add components
        addLabels();

        add(Box.createRigidArea(new Dimension(0, spacing*2)));

        addButtons();
        add(Box.createRigidArea(new Dimension(0, spacing)));
        addTextFields();

        // Set frame
        frame.add(this);
        frame.setVisible(true);
    }

    private void addLabels() {
        // Title
        titleString = new JLabel("Main Menu");
        titleString.setFont(titleFont);
        titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleString);
    }

    private void addButtons() {
        playBtn = new JButton("Play Game");
        playBtn.addActionListener(new playAction());
        playBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(playBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(new newGameAction());
        newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(newGameBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        createBtn = new JButton("Create Sudoku");
        createBtn.addActionListener(null);
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(createBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        solverBtn = new JButton("Sudoku Solver");
        solverBtn.addActionListener(null);
        solverBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(solverBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        highScoreBtn = new JButton("Stats");
        highScoreBtn.addActionListener(null);
        highScoreBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(highScoreBtn);
    }

    private void addTextFields() {
        nText = new JTextField();
        nText.setFont(new Font("Serif", Font.BOLD, 30));
        nText.setMaximumSize(new Dimension(textSize,textSize));
        nText.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nText);
        kText = new JTextField();
        //nText.setPreferredSize(new Dimension(textSize,textSize));
        //nText.setMaximumSize(new Dimension(textSize,textSize));
        kText.setFont(new Font("Serif", Font.BOLD, 30));
        kText.setHorizontalAlignment(JTextField.CENTER);
        kText.setBounds(620, 200, 50, 50);
        add(kText);
    }

    public void setUpMenuBar(JFrame frame){
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new exitAction());

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

    private void startGame() {
        Model model = new Model(k, n, Mode.play);
        View view = new View(model);
        Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
    }

    private void changePanel() {
        frame.remove(this);
    }

    class playAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
            try {
                n = Integer.parseInt(nText.getText());
                k = Integer.parseInt(kText.getText());
                if (n < 1 || k > n) {
                	return;
                }
                frame.dispose();
                startGame();
            } catch (NumberFormatException e2) {

            }
        }
    }

    class newGameAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new NewGameScreen(frame);
        }
    }

    static class exitAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }
}