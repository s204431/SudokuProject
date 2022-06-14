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
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/*
    This is an abstract class that all of the menu screens extend from.
    It contains functionalities that menu screens have in common.
*/

public abstract class MenuScreen extends JPanel {
    protected Font titleFont = new Font(Font.SERIF, Font.BOLD,Main.SCREEN_HEIGHT / 20);
    protected Font buttonFont = new Font(Font.SERIF, Font.BOLD, Main.SCREEN_HEIGHT / 40);
    protected Font textFont = new Font(Font.SERIF, Font.BOLD,Main.SCREEN_HEIGHT / 40);
    protected Font labelFont = new Font(Font.SERIF, Font.BOLD,Main.SCREEN_HEIGHT / 50);
    protected JFrame frame;
    protected int textSize = Main.SCREEN_HEIGHT / 20;
    protected int spacing = Main.SCREEN_HEIGHT / 30;
    protected int btnHeight = Main.SCREEN_HEIGHT / 20;
    protected int btnWidth = Main.SCREEN_WIDTH / 3;
    protected int k, n;
    protected Dimension panelDimension = new Dimension(400,60);
    protected Dimension buttonDimension = new Dimension(btnWidth, btnHeight);
    protected Dimension textDimension = new Dimension(textSize, textSize);
    protected Color buttonColor = new Color(180, 180, 180);
    protected Color hoverButtonColor = new Color(120, 120, 120);
    protected Mode mode;
    protected BufferedImage backgroundImage;


    //Constructor taking the frame.
    public MenuScreen(JFrame frame) {
        this.frame = frame;
        initialize();
    }

    //Constructor allowing to set the mode.
    public MenuScreen(JFrame frame, Mode mode) {
        this.frame = frame;
        this.mode = mode;
        initialize();
    }

    //Initializes values and settings.
    private void initialize() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        initBackground();
        addComponents();
        setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(this);
        frame.setVisible(true);
    }
    
    //Sets background image.
    protected void initBackground() {
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/Background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Draws the background image.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
    }

    //Adds the components to the screen. Should be extended by subclasses.
    public abstract void addComponents();

    //Sets the correct size, font, etc. of the buttons on the screen.
    protected void setButtons(JButton[] buttons){
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(buttonDimension);
            button.setMinimumSize(buttonDimension);
            button.setPreferredSize(buttonDimension);
            button.setFocusPainted(false);
            button.setBackground(buttonColor);
            button.setForeground(Color.BLACK);
            button.setFont(buttonFont);
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(hoverButtonColor);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(buttonColor);
                }
            });
            add(button);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }

    //Sets the correct design of the sliders.
    protected void setSliders(JLabel[] labels, ChangeListener[] listener, JSlider[] sliders, String[] names){
        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(labelFont);
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
            name.setFont(labelFont);
            name.setForeground(Color.BLACK);
            panel.add(name);
            panel.setOpaque(false);
            add(panel);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }

    //Sets the main panel of the screen and adds components to the panel.
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

    //Sets the correct design of the text fields.
    protected void setTextFields(JTextField[] fields){
        for (JTextField field : fields) {
            field.setAlignmentX(Component.CENTER_ALIGNMENT);
            field.setMaximumSize(buttonDimension);
            add(field);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }
    
    //Sets the correct design of the labels.
    protected void setLabels(JLabel[] labels){
        for (JLabel label : labels) {
            label.setFont(labelFont);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(label);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }

    //Sets the title of the screen.
    protected void setTitle(JLabel title){
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(titleFont);
        add(title);
        add(Box.createRigidArea(new Dimension(0, spacing)));
    }

    //Removes the main panel.
    protected void changePanel() {
        frame.remove(this);
    }

    //Creates the MultiplayerModel correctly in multiplayer.
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
    
    //Starts the game in multiplayer (in a parallel thread).
    protected void startThread(MultiplayerModel model){
        MultiplayerView view = new MultiplayerView(model);
        Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);

        new Thread(model).start(); //Creates thread to wait for opponent
    }

    //Creates a model with given k, n, mode and value of assist mode.
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
    
    //Decides which screen to go to when pressing a back button.
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

}