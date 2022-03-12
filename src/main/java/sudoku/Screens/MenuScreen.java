package sudoku.Screens;

import MVC.Controller;
import MVC.Model;
import MVC.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class MenuScreen extends JPanel {
    protected Font titleFont = new Font(Font.SERIF, Font.BOLD,50);
    protected int textSize = 50;
    protected int spacing = 30;
    protected int btnHeight = 50;
    protected int btnWidth = 200;
    protected int k;
    protected int n;
    protected JFrame frame;
    protected Font textFont = new Font("Serif", Font.BOLD,20);
    protected Dimension panelDimension = new Dimension(400,50);
    protected Dimension buttonDimension = new Dimension(200,50);

    public MenuScreen(JFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        addComponents();
        frame.add(this);
        frame.setVisible(true);
    }

    public abstract void addComponents();

    protected void setButtons(JButton[] buttons){
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(buttonDimension);
            add(button);
            add(Box.createRigidArea(new Dimension(0, spacing)));
        }
    }

    protected void setPanel(JPanel panel, JComponent[] components){
        panel.setMaximumSize(panelDimension);
        for (JComponent component : components) {
            component.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (component instanceof JLabel ||
                    component instanceof JTextField) {
                component.setFont(textFont);
            }
            panel.add(component);
            add(panel);
        }
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
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setMaximumSize(buttonDimension);
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

    protected class exitAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }

    protected void setUpMenuBar(JFrame frame){
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        menuBar.add(file);
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);
        exit.addActionListener(new MainScreen.exitAction());

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
}