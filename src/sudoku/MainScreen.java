package sudoku;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;


public class MainScreen extends JPanel implements MouseListener{
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
    private boolean isInMainScreen;


    public MainScreen () {
        isInMainScreen = true;
        frame = new JFrame(title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);

        //frame.setLocationRelativeTo(null);
        JPanel leftBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        leftBoxPanel.setLayout(new BoxLayout(leftBoxPanel, BoxLayout.Y_AXIS));

        JLabel titleString = new JLabel("Main menu");
        titleString.setFont(new Font("Serif", Font.BOLD,40));
        titleString.setPreferredSize(new Dimension(300,200));
        titleString.setMinimumSize(new Dimension(300,200));
        //titleString.setSize(new Dimension(500,500));
        leftBoxPanel.add(titleString);


        JButton playBtn = new JButton("Play");
        playBtn.setPreferredSize(new Dimension(btnSize,btnSize));
        playBtn.addActionListener(new playAction());
        leftBoxPanel.add(playBtn);

        frame.setPreferredSize(new Dimension(1600,800));

        kText = new JTextField("Enter k here: ");
        kText.setPreferredSize(new Dimension(textSize,textSize));
        kText.setMaximumSize(new Dimension(textSize,textSize));
        frame.add(kText);
        leftBoxPanel.add(kText);

        nText = new JTextField("Enter n here: ");
        nText.setPreferredSize(new Dimension(textSize,textSize));
        nText.setMaximumSize(new Dimension(textSize,textSize));

        frame.add(nText);
        leftBoxPanel.add(nText);

        frame.add(leftBoxPanel);

        frame.pack();

        setUpMenuBar(frame);

        frame.setVisible(true);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

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
    class playAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
            n = Integer.parseInt(nText.getText());
            k = Integer.parseInt(kText.getText());
            frame.setVisible(false);
            isInMainScreen = false;
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
