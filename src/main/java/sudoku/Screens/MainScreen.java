package sudoku.Screens;
import multiplayer.MultiplayerModel;
import sudoku.Main;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;

/*
	This class contains the menu screen for the first screen of the menu.

	Responsible: Gideon
 */

public class MainScreen extends MenuScreen {
    private JLabel titleString;
    private JTextField kText, nText;
    private JButton playBtn,
                    newGameBtn,
                    MPBtn,
                    createBtn,
                    solverBtn,
                    highScoreBtn;

    //Constructor taking the frame.
    public MainScreen (JFrame frame) {
        super(frame);
    }
    
    //Constructor that displays a message when starting.
    public MainScreen (JFrame frame, String message) {
        this(frame);
        JOptionPane.showMessageDialog(frame, message);
    }

    //Adds the components to the screen.
    public void addComponents() {//inits title, 6 buttons and 2 textfields with labels
        // Title
        titleString = new JLabel("Main Menu");
        setTitle(titleString);
        // Enables fast of code when DEBUG_MODE is activated.
        // (When not these elements will be found in GenerateNewSudokuScreen.java)
        if (Main.DEBUG_MODE) {
            //Play button
            playBtn = new JButton("Play Game");
            setButtons(new JButton[]{playBtn});
            // k- and n text fields
            JLabel nLabel = new JLabel("N:");
            nText = new JTextField();
            JLabel kLabel = new JLabel("K:");
            kText = new JTextField();
            setPanel(new JPanel(), new JComponent[]{nLabel, nText, kLabel, kText});
        }
        // The rest of the buttons
        newGameBtn = new JButton("New Game");
        MPBtn = new JButton("Multiplayer");
        createBtn = new JButton("Create Sudoku");
        solverBtn = new JButton("Sudoku Solver");
        highScoreBtn = new JButton("Stats");
        setButtons(new JButton[]{newGameBtn, MPBtn, createBtn, solverBtn, highScoreBtn});
        // Action listeners
        setActionListeners();
    }

    //Sets the action listeners of the buttons.
    private void setActionListeners(){//makes the buttons pressable
    	if (Main.DEBUG_MODE) {
            playBtn.addActionListener(new playAction());	
    	}
        MPBtn.addActionListener(new MPAction());
        newGameBtn.addActionListener(new newGameAction());
        createBtn.addActionListener(new createSudokuAction());
        solverBtn.addActionListener(new sudokuSolverAction());
        highScoreBtn.addActionListener(new statsAction());
    }

    //Starts the game when clicking play game in debug mode.
    private void startGame() {
        // Game is initialized.
        // If k == 3 you are the host,
        // otherwise you join.
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

    //Actions listener for the play game button in debug mode.
    class playAction implements ActionListener {//goes to play game based on the 2 text field
        public void actionPerformed (ActionEvent e){
            // Initializes the game with the values inserted.
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

    //Action listener for the new game button.
    private class newGameAction implements ActionListener {//sets "New Game" go to newgamescreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new NewGameScreen(frame);
        }
    }

    //Action listener for the multiplayer button.
    private class MPAction implements ActionListener {//sets "multiplayer" go to multiplayerscreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MultiplayerScreen(frame);
        }
    }
    
    //Action listener for the create sudoku button.
    private class createSudokuAction implements ActionListener {//setes "create Sudoku" to go to createsudokuscreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new CreateSudokuScreen(frame);
        }
    }

    //Action listener for the sudoku solver button.
    private class sudokuSolverAction implements ActionListener {//sets "Sudoku solver" to sudokuSolverscreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new SudokuSolverScreen(frame);
        }
    }
    
    //Action listener for the stats button.
    private class statsAction implements ActionListener {//sets "Stats" to go to Statsscreen
    	public void actionPerformed(ActionEvent e) {
    		changePanel();
    		new StatsScreen(frame);
    	}
    }
}