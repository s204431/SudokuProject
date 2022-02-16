package sudoku;
import MVC.Controller;
import MVC.Model;
import MVC.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainScreen extends JPanel{
    private Font titleFont = new Font(Font.SERIF, Font.PLAIN,100);
    private String title = "Sudoku";
    private int btnSize = 100;
    private int textSize = 200;
    private int spacing = 50;
    private JTextField kText;
    private JTextField nText;
    private int k;
    private int n;
    JFrame frame;


    public MainScreen () {
        frame = new JFrame(title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);

        JPanel leftBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        leftBoxPanel.setLayout(new BoxLayout(leftBoxPanel, BoxLayout.Y_AXIS));

        //Add title
        JLabel titleString = new JLabel("Main menu");
        titleString.setFont(new Font("Serif", Font.BOLD,40));
        titleString.setPreferredSize(new Dimension(300,200));
        titleString.setMinimumSize(new Dimension(300,200));
        leftBoxPanel.add(titleString);
        //Add play button
        JButton playBtn = new JButton("Play");
        playBtn.setPreferredSize(new Dimension(btnSize,btnSize));
        playBtn.addActionListener(new playAction());
        leftBoxPanel.add(playBtn);
        //k and n text fields
        //kText = new JTextField("Enter k here: ");
        //kText.setPreferredSize(new Dimension(textSize,textSize));
        //kText.setMaximumSize(new Dimension(textSize,textSize));
        //frame.add(kText);
        //leftBoxPanel.add(kText);

        nText = new JTextField();
        nText.setPreferredSize(new Dimension(textSize,textSize));
        nText.setMaximumSize(new Dimension(textSize,textSize));
        frame.add(nText);
        leftBoxPanel.add(nText);

        frame.add(leftBoxPanel);
        frame.setPreferredSize(new Dimension(1600,800));
        frame.pack();
        setUpMenuBar(frame);
        frame.setVisible(true);
    }


    public void setUpMenuBar(JFrame frame){
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
    
    private void startGame() {
		Model model = new Model(n);
		View view = new View(model);
		Controller controller = new Controller();
		model.setView(view);
		controller.setModel(model);
		view.setController(controller);
    }

    class playAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
        	try {
                n = Integer.parseInt(nText.getText());
                //k = Integer.parseInt(kText.getText());
                startGame();
                frame.dispose();
        	} catch (NumberFormatException e2) {
        		
        	}
        }
    }

    static class exitAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }

    public int getN() {
        return n;
    }
}
