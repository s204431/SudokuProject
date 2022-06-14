package sudoku.Screens;

import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

	//This class contains the menu screen when clicking sudoku solver.

public class SudokuSolverScreen extends MenuScreen {
    private JLabel titleString;
    private JButton generateBtn,
                    loadBtn,
                    backBtn;

    //Constructor taking the frame.
    public SudokuSolverScreen(JFrame frame) {
        super(frame);
    }

    //Adds the components to the screen.
    public void addComponents() {//init title and buttons
        // Title
        titleString = new JLabel("Sudoku Solver");
        setTitle(titleString);
        //Buttons
        generateBtn = new JButton("Generate New Sudoku");
        loadBtn = new JButton("Load Existing Sudoku");
        backBtn = new JButton("Back");
        setButtons(new JButton[]{generateBtn, loadBtn, backBtn});
        // Action listeners
        setActionListeners();
    }

    //Sets the action listeners for the buttons.
    private void setActionListeners(){//makes buttons pressable
        generateBtn.addActionListener(new generateNewAction());
        loadBtn.addActionListener(new loadGameAction());
        backBtn.addActionListener(new backAction());
    }

    //Action listener for the generate new sudoku button.
    class generateNewAction implements ActionListener {//sets "Generate New Sudoku" to GenerateNewSudokuScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Mode.solver);
        }
    }

    //Action listener for the load existing sudoku button.
    class loadGameAction implements ActionListener {//sets "Load Existing Sudoku" to LoadGameScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Mode.solver);
        }
    }

    //Action listener for the back button.
    class backAction implements ActionListener {//sets "Back" to MainScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}