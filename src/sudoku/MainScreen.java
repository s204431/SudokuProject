package sudoku;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;


public class MainScreen extends JScrollPane implements MouseListener{
    private Font titleFont = new Font(Font.SERIF, Font.PLAIN,100);
    private String title = "Sudoku";
    private int btnSize = 100;
    private int spacing = 50;


    public MainScreen () {

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);

        frame.setLocationRelativeTo(null);
        JPanel btnCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton playBtn = new JButton("Play");

        playBtn.setSize(new Dimension(500,500));
        playBtn.setAlignmentX(100);
        playBtn.setAlignmentY(100);
        btnCenter.add(playBtn);

        setUpMenuBar(frame);

        frame.add(btnCenter);
        frame.pack();
        frame.setSize(1900,900);
        frame.setVisible(true);




        System.out.println("enter k: ");
        Scanner s = new Scanner(System.in);
        int k = s.nextInt();
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



    static class exitAction implements ActionListener {
        public void actionPerformed (ActionEvent e){
            System.exit(0);
        }
    }
}
