package sudoku.Screens;

import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGameScreen extends MenuScreen {
    private JLabel titleString,
                   assistModeString;
    private JCheckBox assistModeCheck;
    private JButton generateBtn,
                    loadBtn,
                    backBtn;

    public NewGameScreen(JFrame frame) {
        super(frame);
    }

    public void addComponents() {//init title, checkBoxx with label, and buttons
        // Title
        titleString = new JLabel("New Game");
        setTitle(titleString);
        //Panels
        assistModeString = new JLabel("Assist mode: ");
        assistModeCheck = new JCheckBox();
        assistModeCheck.setPreferredSize(new Dimension(50, 50));
        setPanel(new JPanel(), new JComponent[]{assistModeString, assistModeCheck});
        //Buttons
        generateBtn = new JButton("Generate New Sudoku");
        loadBtn = new JButton("Load Existing Sudoku");
        backBtn = new JButton("Back");
        setButtons(new JButton[]{generateBtn, loadBtn, backBtn});
        // Action listeners
        setActionListeners();
    }

    private void setActionListeners(){//makes buttons pressable
        generateBtn.addActionListener(new generateNewAction());
        loadBtn.addActionListener(new loadGameAction());
        backBtn.addActionListener(new backAction());
    }

    class generateNewAction implements ActionListener {//sets "Generate New Sudoku" to GenerateNewSudokuScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Mode.play, assistModeCheck.isSelected());
        }
    }

    class loadGameAction implements ActionListener {//sets "Load Existing Sudoku" to LoadGameScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Mode.play,false, assistModeCheck.isSelected());
        }
    }

    class backAction implements ActionListener {//sets "Back" to MainScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}