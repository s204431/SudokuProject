package sudoku;

import MVC.Controller;
import MVC.Model;
import MVC.View;
import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGameScreen extends MenuScreen {
    private JLabel titleString;
    private JLabel assistModeString;
    private JCheckBox assistModeCheck;
    private JButton generateBtn;
    private JButton loadBtn;
    private JButton backBtn;

    public NewGameScreen(JFrame frame) {
        super(frame);
    }

    public void addComponents() {
        addLabels();
        addButtons();
    }

    private void addLabels() {
        // Title
        titleString = new JLabel("New Game");
        titleString.setFont(titleFont);
        titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleString);

        add(Box.createRigidArea(new Dimension(0, spacing*2)));
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(400,50));
        assistModeString = new JLabel("Assist mode: ");
        assistModeString.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        assistModeString.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(assistModeString);

        assistModeCheck = new JCheckBox();
        assistModeCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(assistModeCheck);
        add(panel);
    }

    private void addButtons() {
        generateBtn = new JButton("Generate New Sudoku");
        generateBtn.addActionListener(new generateNewAction());
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(generateBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        loadBtn = new JButton("Load Existing Sudoku");
        loadBtn.addActionListener(new loadGameAction());
        loadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(loadBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        backBtn = new JButton("Back");
        backBtn.addActionListener(new backAction());
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
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

    class generateNewAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Mode.play);
        }
    }

    class loadGameAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Mode.play);
        }
    }

    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}