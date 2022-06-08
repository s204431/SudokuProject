package sudoku.Screens;

import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateSudokuScreen extends MenuScreen {
    private JLabel titleString;
    private JButton generateBtn;
    private JButton loadBtn;
    private JButton backBtn;

    public CreateSudokuScreen(JFrame frame) {
        super(frame);
    }

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
    private void setActionListeners(){//makes 3 buttons pressable
        generateBtn.addActionListener(new generateNewAction());
        loadBtn.addActionListener(new loadGameAction());
        backBtn.addActionListener(new backAction());
    }


    class generateNewAction implements ActionListener {//sets "Generate Empty Sudoku" to go to GenerateNewSudokuScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Mode.create);
        }
    }

    class loadGameAction implements ActionListener {//sets "Load Existing Sudoku" to go to LoadGameScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Mode.create);
        }
    }

    class backAction implements ActionListener {//sets "back", to go to main menu
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}