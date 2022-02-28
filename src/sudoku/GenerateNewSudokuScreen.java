package sudoku;

import MVC.Controller;
import MVC.Model;
import MVC.Model.Mode;
import MVC.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GenerateNewSudokuScreen extends JPanel {
    private String title = "Sudoku";
    private Font titleFont = new Font(Font.SERIF, Font.BOLD,40);
    private int btnHeight = 50;
    private int btnWidth = 200;
    private JFrame frame;
    private JLabel titleString;
    private JLabel boardSizeString;
    private JLabel difficulyString;
    private JButton generateBtn;
    private JButton backBtn;
    private JComboBox<String> difficultyDDMenu;

    public GenerateNewSudokuScreen(JFrame frame) {
        this.frame = frame;

        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setLayout(null);

        // Add components
        addDropdownMenu();
        addLabels();
        addButtons();

        // Set frame
        frame.add(this);
        frame.setVisible(true);
    }

    private void addDropdownMenu() {
        String[] difficulties = { "Easy", "Medium", "Hard" };
        difficultyDDMenu = new JComboBox<>(difficulties);
        difficultyDDMenu.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        difficultyDDMenu.setBounds(400, 350, 150, btnHeight);
        add(difficultyDDMenu);
    }

    private void addLabels() {
        // Title
        titleString = new JLabel("New Game");
        titleString.setFont(titleFont);
        //titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleString.setBounds(300, 50, btnWidth, 100);
        add(titleString);

        boardSizeString = new JLabel("Size of board");
        boardSizeString.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        boardSizeString.setBounds(300, 200, btnWidth, btnHeight);
        add(boardSizeString);

        difficulyString = new JLabel("Difficulty: ");
        difficulyString.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        difficulyString.setBounds(250, 350, btnWidth, btnHeight);
        add(difficulyString);
    }

    private void addButtons() {
        generateBtn = new JButton("Generate Sudoku");
        generateBtn.addActionListener(new startAction());
        generateBtn.setBounds(300, 450, btnWidth, btnHeight);
        add(generateBtn);

        backBtn = new JButton("Back");
        backBtn.addActionListener(new backAction());
        backBtn.setBounds(300, 550, btnWidth, btnHeight);
        add(backBtn);
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

    private Model startGame(int n) {
        Model model = new Model(n, Mode.play);
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
            startGame(3);    // TODO: Use chosen size of board
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
