package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoadGameScreen extends JPanel {
    private String title = "Sudoku";
    private Font titleFont = new Font(Font.SERIF, Font.BOLD,40);
    private int btnHeight = 50;
    private int btnWidth = 200;
    private JFrame frame;
    private JLabel titleString;
    private JButton loadGameBtn;
    private JButton backBtn;
    private JList<String> loadList;

    public LoadGameScreen(JFrame frame) {
        this.frame = frame;

        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setLayout(null);

        // Add components
        addLists();
        addLabels();
        addButtons();

        // Set frame
        frame.add(this);
        frame.setVisible(true);
    }

    private void addLists() {
        ArrayList<String> savedSudokus = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            savedSudokus.add("My Saved Sudoku " + i);
        }
        loadList = new JList<String>(savedSudokus.toArray(new String[savedSudokus.size()]));

        // Add scroll function
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(loadList);
        loadList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setBounds(300, 200, 200, 300);
        add(scrollPane);
    }

    private void addLabels() {
        // Title
        titleString = new JLabel("Load Game");
        titleString.setFont(titleFont);
        //titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleString.setBounds(300, 50, 300, 100);
        add(titleString);
    }

    private void addButtons() {
        loadGameBtn = new JButton("Load Game");
        loadGameBtn.addActionListener(null);
        loadGameBtn.setBounds(300, 550, btnWidth, btnHeight);
        add(loadGameBtn);

        backBtn = new JButton("Back");
        backBtn.addActionListener(new backAction());
        backBtn.setBounds(300, 650, btnWidth, btnHeight);
        add(backBtn);
    }

    public void setUpMenuBar(JFrame frame) {
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

    private void changePanel() {
        frame.remove(this);
    }

    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new NewGameScreen(frame);
        }
    }

    static class exitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

}