package sudoku;

import MVC.Controller;
import MVC.Model;
import MVC.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGameScreen extends JPanel {
    private String title = "Sudoku";
    private Font titleFont = new Font(Font.SERIF, Font.BOLD, 40);
    private int btnHeight = 50;
    private int btnWidth = 200;
    private JFrame frame;
    private JLabel titleString;
    private JLabel assistModeString;
    private JCheckBox assistModeCheck;
    private JButton generateBtn;
    private JButton loadBtn;
    private JButton backBtn;

    public NewGameScreen(JFrame frame) {
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
        titleString = new JLabel("New Game");
        titleString.setFont(titleFont);
        //titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleString.setBounds(300, 50, 200, 100);
        add(titleString);

        assistModeString = new JLabel("Assist mode: ");
        assistModeString.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        assistModeString.setBounds(300, 600, 200, 100);
        add(assistModeString);

        assistModeCheck = new JCheckBox();
        assistModeCheck.setBounds(420, 600, 100, 100);
        add(assistModeCheck);
    }

    private void addButtons() {
        generateBtn = new JButton("Generate New Sudoku");
        generateBtn.addActionListener(null);
        generateBtn.setBounds(300, 200, btnWidth, btnHeight);
        add(generateBtn);

        loadBtn = new JButton("Load Existing Sudoku");
        //newGameBtn.setPreferredSize(new Dimension(btnSize,btnSize));
        //newGameBtn.setMaximumSize(new Dimension(btnSize,btnSize));
        //newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadBtn.addActionListener(null);
        loadBtn.setBounds(300, 300, btnWidth, btnHeight);
        add(loadBtn);

        backBtn = new JButton("Back");
        backBtn.addActionListener(new backAction());
        backBtn.setBounds(300, 400, btnWidth, btnHeight);
        add(backBtn);
    }

    private void addTextFields() {
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

    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }

    static class exitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

}
