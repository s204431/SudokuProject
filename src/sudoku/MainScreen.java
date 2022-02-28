package sudoku;
import MVC.Controller;
import MVC.Model;
import MVC.View;
import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainScreen extends JPanel{

    private Font titleFont = new Font(Font.SERIF, Font.BOLD,40);
    private int btnHeight = 50;
    private int btnWidth = 200;
    private int textSize = 200;
    private int spacing = 50;
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
    private JButton highscoreBtn;


    public MainScreen (JFrame frame) {
        this.frame = frame;

        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setLayout(null);

        // Add components
        addLabels();
        addButtons();
        addTextFields();

        // Set frame
        frame.add(this);
        frame.setVisible(true);
    }

    private void addLabels() {
        // Title
        titleString = new JLabel("Main Menu");
        titleString.setFont(titleFont);
        //titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleString.setBounds(300, 50, 200, 100);
        add(titleString);
    }

    private void addButtons() {
        playBtn = new JButton("Play Game");
        playBtn.addActionListener(new playAction());
        playBtn.setBounds(300, 200, btnWidth, btnHeight);
        add(playBtn);

        newGameBtn = new JButton("New Game");
        //newGameBtn.setPreferredSize(new Dimension(btnSize,btnSize));
        //newGameBtn.setMaximumSize(new Dimension(btnSize,btnSize));
        //newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameBtn.addActionListener(new newGameAction());
        newGameBtn.setBounds(300, 300, btnWidth, btnHeight);
        add(newGameBtn);

        createBtn = new JButton("Create Sudoku");
        createBtn.addActionListener(null);
        createBtn.setBounds(300, 400, btnWidth, btnHeight);
        add(createBtn);

        solverBtn = new JButton("Sudoku Solver");
        solverBtn.addActionListener(null);
        solverBtn.setBounds(300, 500, btnWidth, btnHeight);
        add(solverBtn);

        highscoreBtn = new JButton("Stats");
        highscoreBtn.addActionListener(null);
        highscoreBtn.setBounds(300, 600, btnWidth, btnHeight);
        add(highscoreBtn);
    }

    private void addTextFields() {
        nText = new JTextField();
        //nText.setPreferredSize(new Dimension(textSize,textSize));
        //nText.setMaximumSize(new Dimension(textSize,textSize));
        nText.setFont(new Font("Serif", Font.BOLD, 30));
        nText.setHorizontalAlignment(JTextField.CENTER);
        nText.setBounds(550, 200, 50, 50);
        add(nText);
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
		Model model = new Model(n, Mode.play);
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
                if (n < 1) {
                	return;
                }
                //k = Integer.parseInt(kText.getText());
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
