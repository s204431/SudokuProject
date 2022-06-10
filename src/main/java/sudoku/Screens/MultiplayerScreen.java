package sudoku.Screens;

import MVC.Model;
import multiplayer.MultiplayerModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MultiplayerScreen extends MenuScreen{
    private JLabel titleLabel;
    private JButton hostBtn,
                    loadBtn,
                    joinBtn,
                    backBtn;
    private JTextField ip_address;


    public MultiplayerScreen(JFrame frame) {
        super(frame);
    }
    public void addComponents(){//title and buttons
        // Title
        titleLabel = new JLabel("Multiplayer");
        setTitle(titleLabel);
        // First 2 buttons
        hostBtn = new JButton("Host New Sudoku");
        loadBtn = new JButton("Host Old Sudoku");
        setButtons(new JButton[]{hostBtn, loadBtn});
        // IP text field
        ip_address = new JTextField(MultiplayerModel.getIP());
        setTextFields(new JTextField[]{ip_address});
        // Last 2 buttons
        joinBtn = new JButton("Join");
        backBtn = new JButton("Back");
        setButtons(new JButton[]{joinBtn, backBtn});
        // Action listeners
        setActionListeners();
    }

    private void setActionListeners() {//makes buttons pressable
        hostBtn.addActionListener(new hostAction());
        loadBtn.addActionListener(new loadAction());
        joinBtn.addActionListener(new joinAction());
        backBtn.addActionListener(new backAction());
    }

    class hostAction implements ActionListener  {//sets "Host new Sudoku" to GenerateNewSudokuScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Model.Mode.multiplayer);
        }
    }
    class loadAction implements ActionListener  {//sets "Host Old Sudoku" to LoadGameScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Model.Mode.multiplayer, true);
        }
    }
    class joinAction implements ActionListener  {//sets "Join" connect to the written Ip in textfield
        public void actionPerformed(ActionEvent e) {
            String address = ip_address.getText();
            frame.dispose();
            setMultiplayerInstance(false, address);
        }
    }
    class backAction implements ActionListener  {//sets "Back" go to mainscreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}
