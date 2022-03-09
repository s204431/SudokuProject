package sudoku;

import MVC.Controller;
import MVC.Model;
import MVC.Model.Mode;
import MVC.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

<<<<<<< HEAD
public class LoadGameScreen extends MenuScreen {
=======
public class LoadGameScreen extends JPanel {
    private String title = "Sudoku";
    private Font titleFont = new Font(Font.SERIF, Font.BOLD,40);
    private int btnHeight = 50;
    private int btnWidth = 200;
    private int spacing = 30;
    private JFrame frame;
>>>>>>> parent of 0139c6f (no message)
    private JLabel titleString;
    private JButton loadGameBtn;
    private JButton backBtn;
    //private JList<String> loadList;
    private JList<LoadListElement> loadList;
    private Mode mode;

    public LoadGameScreen(JFrame frame, Mode mode) {
<<<<<<< HEAD
        super(frame);
        this.mode = mode;
    }

    public void addComponents() {
        addLists();
        addLabels();
        addButtons();
=======
        this.frame = frame;
        this.mode = mode;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));


        // Add components
        addLists();
        addLabels();
        addButtons();

        // Set frame
        frame.add(this);
        frame.setVisible(true);
>>>>>>> parent of 0139c6f (no message)
    }

    private void addLists() {
        File folder = new File("savedsudokus");
        File[] matchingFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".su");
            }
        });

        LoadListElement[] savedSudokus = new LoadListElement[matchingFiles.length];
        for (int i = 0; i < matchingFiles.length; i++) {
        	String name = matchingFiles[i].getName().substring(0, matchingFiles[i].getName().length() - 3);
        	Object[] result = Model.load(name, Mode.play);
        	Field[][] board = (Field[][]) result[0];
            savedSudokus[i] = new LoadListElement(name, 0, board.length, board, (int) result[1], (int) result[2]);
        }
        
        loadList = new JList<LoadListElement>(savedSudokus);
        loadList.setCellRenderer(new LoadListRenderer(btnWidth*2));
        
        // Add scroll function
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(loadList);
        loadList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(btnWidth*2,300));
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0, spacing)));
    }

    private void addLabels() {
        // Title
        titleString = new JLabel("Load Game");
        titleString.setFont(titleFont);
        //titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleString.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleString);
        add(Box.createRigidArea(new Dimension(0, spacing)));
    }

    private void addButtons() {
        loadGameBtn = new JButton("Load Game");
        loadGameBtn.addActionListener(new loadAction());
        loadGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadGameBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        add(loadGameBtn);
        add(Box.createRigidArea(new Dimension(0, spacing)));

        backBtn = new JButton("Back");
        backBtn.addActionListener(new backAction());
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
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

<<<<<<< HEAD
=======
    private void changePanel() {
        frame.remove(this);
    }

>>>>>>> parent of 0139c6f (no message)
    private Model startGame(int k, int n, Mode mode) {
        Model model = new Model(k, n, mode);
        View view = new View(model);
        Controller controller = new Controller();
        model.setView(view);
        controller.setModel(model);
        controller.setView(view);
        view.setController(controller);
        return model;
    }

    class loadAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
            //startGame(1, 1, mode).load(loadList.getSelectedValue());
            startGame(1, 1, mode).loadAndUpdate(loadList.getSelectedValue().name);
        }
    }

    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            if (mode == Mode.play) {
                new NewGameScreen(frame);
            }
            else if (mode == Mode.create) {
                new CreateSudokuScreen(frame);
            }
            else {
                new SudokuSolverScreen(frame);
            }
        }
    }
<<<<<<< HEAD
=======

    static class exitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

>>>>>>> parent of 0139c6f (no message)
}