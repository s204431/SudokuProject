package sudoku.Screens;

import MVC.Model;
import multiplayer.MultiplayerModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

	//This class contains the menu screen when clicking multiplayer.

public class MultiplayerScreen extends MenuScreen{
    private JLabel titleLabel;
    private JButton hostBtn,
                    loadBtn,
                    joinBtn,
                    backBtn;
    private JTextField ip_address;


    //Constructor taking the frame.
    public MultiplayerScreen(JFrame frame) {
        super(frame);
    }
    
    //Adds the components to the screen.
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

    //Sets the action listeners for the buttons.
    private void setActionListeners() {//makes buttons pressable
        hostBtn.addActionListener(new hostAction());
        loadBtn.addActionListener(new loadAction());
        joinBtn.addActionListener(new joinAction());
        backBtn.addActionListener(new backAction());
    }

    //Action listener for the host new sudoku button.
    class hostAction implements ActionListener  {//sets "Host new Sudoku" to GenerateNewSudokuScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Model.Mode.multiplayer);
        }
    }
    
    //Action listener for the host old sudoku button.
    class loadAction implements ActionListener  {//sets "Host Old Sudoku" to LoadGameScreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Model.Mode.multiplayer, true);
        }
    }
    
    //Action listener for the join button.
    class joinAction implements ActionListener  {//sets "Join" connect to the written Ip in textfield
        public void actionPerformed(ActionEvent e) {
            String address = ip_address.getText();
            frame.dispose();
            setMultiplayerInstance(false, address);
        }
    }
    
    //Sets the action listener for the back button.
    class backAction implements ActionListener  {//sets "Back" go to mainscreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}
