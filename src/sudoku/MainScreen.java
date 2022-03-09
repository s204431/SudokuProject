package sudoku;
import MVC.Controller;
import MVC.Model;
import MVC.View;
import multiplayer.MultiplayerModel;
import multiplayer.MultiplayerView;
import MVC.Model.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainScreen extends MenuScreen {
    private JLabel titleString;
    private JTextField kText;
    private JTextField nText;
    private JButton playBtn;
    private JButton newGameBtn;
    private JButton createBtn;
    private JButton solverBtn;
    private JButton highScoreBtn;


    public MainScreen (JFrame frame) {
        super(frame);
    }

    public void addComponents() {
        addLabels();
        addButtons();
    }

    private void addLabels() {
        // Title
        titleString = new JLabel("Main Menu");
        titleString.setFont(titleFont);
        titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleString);
    }

    private void addButtons() {
        add(Box.createRigidArea(new Dimension(0, spacing*2)));

        playBtn = new JButton("Play Game");
        playBtn.addActionListener(new playAction());
        playBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(playBtn);

        add(Box.createRigidArea(new Dimension(0, spacing/2)));

        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(400,50));
        addTextFields(panel);


        add(Box.createRigidArea(new Dimension(0, spacing)));

        newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(new newGameAction());
        newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(newGameBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        createBtn = new JButton("Create Sudoku");
        createBtn.addActionListener(new createSudokuAction());
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        createBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(createBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        solverBtn = new JButton("Sudoku Solver");
        solverBtn.addActionListener(new sudokuSolverAction());
        solverBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        solverBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(solverBtn);

        add(Box.createRigidArea(new Dimension(0, spacing)));

        highScoreBtn = new JButton("Stats");
        highScoreBtn.addActionListener(null);
        highScoreBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoreBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(highScoreBtn);
    }

    private void addTextFields(JPanel panel) {
        JLabel nLabel = new JLabel("N:");
        nLabel.setFont(new Font("Serif", Font.BOLD, 20));
        nLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nLabel);
        nText = new JTextField();
        nText.setFont(new Font("Serif", Font.BOLD, 20));
        nText.setAlignmentX(Component.CENTER_ALIGNMENT);
        nText.setPreferredSize(new Dimension(textSize,textSize));
        panel.add(nText);
        JLabel kLabel = new JLabel("K:");
        kLabel.setFont(new Font("Serif", Font.BOLD, 20));
        kLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(kLabel);
        kText = new JTextField();
        kText.setFont(new Font("Serif", Font.BOLD, 20));
        kText.setAlignmentX(Component.CENTER_ALIGNMENT);
        kText.setPreferredSize(new Dimension(textSize,textSize));
        panel.add(kText);

        add(panel);
    }

    private void startGame() {
       /*Model model = new Model(k, n, Mode.play);
        View view = new View(model);
        Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);*/
    	MultiplayerModel model;
    	if (k == 3) {
        	model = new MultiplayerModel(k, n);
    	}
    	else {
    		model = new MultiplayerModel(k, n, MultiplayerModel.getIP());
    	}
    	MultiplayerView view = new MultiplayerView(model);
    	Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
    	new Thread(model).start();
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
            } catch (NumberFormatException e2) {

            }
        }
    }

    private class newGameAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new NewGameScreen(frame);
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