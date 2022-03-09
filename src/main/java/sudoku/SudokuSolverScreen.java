package sudoku;

import MVC.Controller;
import MVC.Model;
import MVC.View;
import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuSolverScreen extends MenuScreen {
    private JLabel titleString;
    private JLabel assistModeString;
    private JCheckBox assistModeCheck;
    private JButton generateBtn;
    private JButton loadBtn;
    private JButton backBtn;

    public SudokuSolverScreen(JFrame frame) {
        super(frame);
    }

    public void addComponents() {
        addLabels();
        addButtons();
    }


    private void addLabels() {
        // Title
        titleString = new JLabel("Sudoku Solver");
        titleString.setFont(titleFont);
        titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleString);

        add(Box.createRigidArea(new Dimension(0, spacing*2)));
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(400,50));
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

    class generateNewAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Mode.solver);
        }
    }

    class loadGameAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Mode.solver);
        }
    }

    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}