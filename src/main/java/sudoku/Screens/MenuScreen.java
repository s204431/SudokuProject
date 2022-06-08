package sudoku.Screens;

import MVC.Controller;
import MVC.Model;
import MVC.Model.Mode;
import MVC.View;
import multiplayer.MultiplayerModel;
import multiplayer.MultiplayerView;
import sudoku.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
The abstract class MenuScreen is a screen that all the other Screens inherit from. It has built-in functions
that almost every other Screen calls, this is done to reduce redundancy. It sets the bounds and limits for
all classes and minimizes errors.
*/


public abstract class MenuScreen extends JPanel {
    protected Font titleFont = new Font(Font.SERIF, Font.BOLD,50);
    protected int textSize = 50;
    protected int spacing = 30;
    protected int btnHeight = 50;
    protected int btnWidth = 300;
    protected int k;
    protected int n;
    protected JFrame frame;
    protected Font buttonFont = new Font("Arial", Font.BOLD, 20);
    protected Font textFont = new Font("Serif", Font.BOLD,20);
    protected Font labelFont = new Font("Serif", Font.BOLD,30);
    protected Dimension panelDimension = new Dimension(400,60);
    protected Dimension buttonDimension = new Dimension(btnWidth, btnHeight);
    protected Dimension textDimension = new Dimension(textSize, textSize);
    protected Color buttonColor = new Color(180, 180, 180);
    protected Color hoverButtonColor = new Color(120, 120, 120);
    protected Mode mode;
    protected BufferedImage backgroundImage;


    public MenuScreen(JFrame frame) {
        this.frame = frame;
        initialize();
    }

    public MenuScreen(JFrame frame, Mode mode) {
        this.frame = frame;
        this.mode = mode;
        initialize();
    }

    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        initBackground();
        addComponents();
        setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(this);
        frame.setVisible(true);
    }

    private void initBackground() {
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }

    public abstract void addComponents();

    // All 'set...' JComponent methods creates a generic:
    // set element -> add space -> repeat...

    protected void setButtons(JButton[] buttons){
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(buttonDimension);
            button.setMinimumSize(buttonDimension);
            button.setPreferredSize(buttonDimension);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            //button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            //button.setOpaque(false);
            button.setBorderPainted(true);
            button.setBackground(buttonColor);
            button.setForeground(Color.BLACK);
            button.setFont(buttonFont);
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(hoverButtonColor);
                    button.setForeground(Color.BLACK);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(buttonColor);
                    button.setForeground(Color.BLACK);
                }
            });
            add(button);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }

    protected void setSliders(JLabel[] labels, ChangeListener[] listener, JSlider[] sliders, String[] names){
        for (int i = 0; i < labels.length; i++) {
            add(labels[i]);
            sliders[i].setPaintTicks(true);
            sliders[i].setMajorTickSpacing(2);
            sliders[i].setMinorTickSpacing(1);
            sliders[i].addChangeListener(listener[i]);
            sliders[i].setOpaque(false);
            JPanel panel = new JPanel();
            panel.setMaximumSize(new Dimension(200,60));
            panel.add(sliders[i]);
            JLabel name = new JLabel(names[i]);
            name.setForeground(Color.BLACK);
            panel.add(name);
            panel.setOpaque(false);
            add(panel);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }

    protected void setPanel(JPanel panel, JComponent[] components){
        panel.setMaximumSize(panelDimension);
        panel.setOpaque(false);
        for (JComponent component : components) {
            component.setOpaque(false);
            component.setAlignmentX(0.f);
            if (component instanceof JLabel ||
                    component instanceof JTextField) {
                component.setFont(textFont);
            }
            if (component instanceof JTextField) {
                component.setPreferredSize(textDimension);
            }
            panel.add(component);
        }
        add(panel);
        add(Box.createRigidArea(new Dimension(0, spacing)));
    }

    protected void setTextFields(JTextField[] fields){
        for (JTextField field : fields) {
            field.setAlignmentX(Component.CENTER_ALIGNMENT);
            field.setMaximumSize(buttonDimension);
            add(field);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }
    protected void setLabels(JLabel[] labels){
        for (JLabel label : labels) {
            label.setFont(labelFont);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(label);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }

    protected void setTitle(JLabel title){
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(titleFont);
        add(title);
        add(Box.createRigidArea(new Dimension(0, spacing)));
    }

    private void startGame() {
        Model model = new Model(k, n, Model.Mode.play);
        View view = new View(model);
        Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
    }

    protected void changePanel() {
        frame.remove(this);
    }


    protected Model setMultiplayerInstance(boolean isHost, String address){     // This method checks if the player
        MultiplayerModel model;                                                 // is the host or not and decides how
        if (isHost) {                                                           // to start the game depending on that.
            model = new MultiplayerModel(k, n);
        }
        else {
        	try {
                model = new MultiplayerModel(k, n, address);
        	}
			catch (IOException e) {
				Main.restart("Could not connect to server");
				return null;
			}
        }
        startThread(model);
        return model;
    }
    protected void startThread(MultiplayerModel model){
        MultiplayerView view = new MultiplayerView(model);
        Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);

        new Thread(model).start(); //Creates thread to wait for opponent
    }

    protected Model getModel(int k, int n, Mode mode, boolean assistMode) {
        Model model = new Model(k, n, mode, assistMode);
        View view = new View(model);
        Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
        return model;
    }

    protected void setUpMenuBar(JFrame frame){
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new exitAction());

        JMenu difficulty = new JMenu("Difficulty");
        menuBar.add(difficulty);
        difficulty.add(new JMenuItem("Easy"));
        difficulty.add(new JMenuItem("Medium"));
        difficulty.add(new JMenuItem("Hard"));

        JMenu restart = new JMenu("Restart");
        menuBar.add(restart);
        restart.add(new JMenuItem("Same Sudoku"));
        restart.add(new JMenuItem("New Sudoku"));
    }

    protected void backAction(){
        changePanel();
        switch (mode){
            case play:
                new NewGameScreen(frame);
                break;
            case create:
                new CreateSudokuScreen(frame);
                break;
            case multiplayer:
                new MultiplayerScreen(frame);
                break;
            default:
                new SudokuSolverScreen(frame);
        }
    }

    protected static class exitAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }

}