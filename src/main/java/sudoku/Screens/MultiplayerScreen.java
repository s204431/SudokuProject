package sudoku.Screens;

import MVC.Model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MultiplayerScreen extends MenuScreen{
    private JLabel titleLabel;
    private JButton hostBtn;
    private JButton joinBtn;
    private JButton backBtn;
    private JTextField ip_address;


    public MultiplayerScreen(JFrame frame) {
        super(frame);
    }
    public void addComponents(){
        titleLabel = new JLabel("Multiplayer");
        hostBtn = new JButton("Host");
        joinBtn = new JButton("Join");
        backBtn = new JButton("Back");
        ip_address = new JTextField();

        setTitle(titleLabel);
        setButtons(new JButton[]{hostBtn});
        setTextFields(new JTextField[]{ip_address});
        setButtons(new JButton[]{joinBtn, backBtn});

        setActionListeners();
    }

    private void setActionListeners() {
        hostBtn.addActionListener(new hostAction());
        joinBtn.addActionListener(new joinAction());
        backBtn.addActionListener(new backAction());
    }

    class hostAction implements ActionListener  {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            //setMultiplayerInstance(true, "");
            new GenerateNewSudokuScreen(frame, Model.Mode.multiplayer);
        }
    }
    class joinAction implements ActionListener  {
        public void actionPerformed(ActionEvent e) {
            String address = ip_address.getText();
            frame.dispose();
            setMultiplayerInstance(false, address);
        }
    }
    class backAction implements ActionListener  {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }
}
