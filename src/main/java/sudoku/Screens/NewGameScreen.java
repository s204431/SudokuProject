package sudoku.Screens;

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
        // Title
        titleString = new JLabel("New Game");
        setTitle(titleString);

        //Panel
        assistModeString = new JLabel("Assist mode: ");
        assistModeCheck = new JCheckBox();
        setPanel(new JPanel(), new JComponent[]{assistModeCheck, assistModeCheck});

        addButtons();
        setActionListeners();
    }

    private void addButtons() {
        //Buttons
        generateBtn = new JButton("Generate New Sudoku");
        loadBtn = new JButton("Load Existing Sudoku");
        backBtn = new JButton("Back");
        setButtons(new JButton[]{generateBtn, loadBtn, backBtn});
    }

    private void setActionListeners(){
        generateBtn.addActionListener(new generateNewAction());
        loadBtn.addActionListener(new loadGameAction());
        backBtn.addActionListener(new backAction());
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