package sudoku.Screens;

import MVC.Model;
import multiplayer.MultiplayerModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MultiplayerScreen extends MenuScreen{
    private JLabel titleLabel;
    private JButton hostBtn;
    private JButton loadBtn;
    private JButton joinBtn;
    private JButton backBtn;
    private JTextField ip_address;


    public MultiplayerScreen(JFrame frame) {
        super(frame);
    }
    public void addComponents(){
        titleLabel = new JLabel("Multiplayer");
        hostBtn = new JButton("Host New Sudoku");
        loadBtn = new JButton("Host Old Sudoku");
        joinBtn = new JButton("Join");
        backBtn = new JButton("Back");
        ip_address = new JTextField(MultiplayerModel.getIP());

        setTitle(titleLabel);
        setButtons(new JButton[]{hostBtn, loadBtn});
        setTextFields(new JTextField[]{ip_address});
        setButtons(new JButton[]{joinBtn, backBtn});

        setActionListeners();
    }

    private void setActionListeners() {
        hostBtn.addActionListener(new hostAction());
        loadBtn.addActionListener(new loadAction());
        joinBtn.addActionListener(new joinAction());
        backBtn.addActionListener(new backAction());
    }

    class hostAction implements ActionListener  {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new GenerateNewSudokuScreen(frame, Model.Mode.multiplayer);
        }
    }
    class loadAction implements ActionListener  {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new LoadGameScreen(frame, Model.Mode.play, true);
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
