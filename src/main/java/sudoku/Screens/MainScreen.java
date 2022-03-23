package sudoku.Screens;
import multiplayer.MultiplayerModel;
import sudoku.Main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MainScreen extends MenuScreen {
    private JLabel titleString;
    private JTextField kText;
    private JTextField nText;
    private JButton playBtn;
    private JButton newGameBtn;
    private JButton MPBtn;
    private JButton createBtn;
    private JButton solverBtn;
    private JButton highScoreBtn;
    Dimension panelSize = new Dimension(400,50);


    public MainScreen (JFrame frame) {
        super(frame);
    }
    
    public MainScreen (JFrame frame, String message) {
        this(frame);
        JOptionPane.showMessageDialog(frame, message);
    }

    public void addComponents() {
        //Title
        titleString = new JLabel("Main Menu");
        setTitle(titleString);

        if (Main.DEBUG_MODE) {
            //Play button
            playBtn = new JButton("Play Game");
            setButtons(new JButton[]{playBtn});

            //k and n text fields
            JLabel nLabel = new JLabel("N:");
            nText = new JTextField();
            JLabel kLabel = new JLabel("K:");
            kText = new JTextField();
            setPanel(new JPanel(), new JComponent[]{nLabel, nText, kLabel, kText});	
        }

        //The rest of the buttons
        newGameBtn = new JButton("New Game");
        MPBtn = new JButton("Multiplayer");
        createBtn = new JButton("Create Sudoku");
        solverBtn = new JButton("Sudoku Solver");
        highScoreBtn = new JButton("Stats");
        setButtons(new JButton[]{newGameBtn, MPBtn, createBtn, solverBtn, highScoreBtn});

        setActionListeners();
    }

    private void setActionListeners(){
    	if (Main.DEBUG_MODE) {
            playBtn.addActionListener(new playAction());	
    	}
        MPBtn.addActionListener(new MPAction());
        newGameBtn.addActionListener(new newGameAction());
        createBtn.addActionListener(new createSudokuAction());
        solverBtn.addActionListener(new sudokuSolverAction());
        highScoreBtn.addActionListener(null);
    }

    private void startGame() {
    	MultiplayerModel model;
    	if (k == 3) {
        	model = new MultiplayerModel(k, n);
    	}
    	else {
    		try {
        		model = new MultiplayerModel(k, n, MultiplayerModel.getIP());
    		}
			catch (IOException e) {
				Main.restart("Could not connect to server");
				return;
			}
		}
        startThread(model);
    }

    class playAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
            try {
                n = Integer.parseInt(nText.getText());
                k = Integer.parseInt(kText.getText());
                if (n < 1) {
                    return;
                }
                frame.dispose();
                startGame();
            } catch (NumberFormatException ignored) {

            }
        }
    }

    private class newGameAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new NewGameScreen(frame);
        }
    }

    private class MPAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MultiplayerScreen(frame);
        }
    }
    
    private class createSudokuAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new CreateSudokuScreen(frame);
        }
    }

    private class sudokuSolverAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new SudokuSolverScreen(frame);
        }
    }
}