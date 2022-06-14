package sudoku.Screens;

import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
	This class contains the menu screen when clicking create sudoku.

	Responsible: Gideon
 */

public class CreateSudokuScreen extends MenuScreen {
    private JLabel titleString;
    private JButton generateBtn, loadBtn, backBtn;

    //Constructor taking the frame.
    public CreateSudokuScreen(JFrame frame) {
        super(frame);
    }

    //Adds the components to the screen.
    public void addComponents() { //init 3 buttons and a title
        //Title
        titleString = new JLabel("Create Sudoku");
        setTitle(titleString);
        //Buttons
        generateBtn = new JButton("Generate New Sudoku");
        loadBtn = new JButton("Load Existing Sudoku");
        backBtn = new JButton("Back");
        setButtons(new JButton[]{generateBtn, loadBtn, backBtn});
        //Buttons action listeners
        setActionListeners();
    }
    
    //Sets the action listeners of the buttons.
    private void setActionListeners(){//makes 3 buttons pressable
        generateBtn.addActionListener(new generateNewAction());
        loadBtn.addActionListener(new loadGameAction());
        backBtn.addActionListener(new backAction());
    }

    //Action listener for the generate new sudoku button.
    class generateNewAction implements ActionListener {//sets "Generate Empty Sudoku" to go to GenerateNewSudokuScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Mode.create);
        }
    }

  //Action listener for the load existing sudoku button.
    class loadGameAction implements ActionListener {//sets "Load Existing Sudoku" to go to LoadGameScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Mode.create);
        }
    }

  //Action listener for the back button.
    class backAction implements ActionListener {//sets "back", to go to main menu
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}