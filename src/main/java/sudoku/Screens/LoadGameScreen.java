package sudoku.Screens;

import MVC.Controller;
import MVC.Model;
import MVC.Model.Mode;
import MVC.View;
import sudoku.Field;
import sudoku.LoadListElement;
import sudoku.LoadListRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

public class LoadGameScreen extends MenuScreen {
    private JLabel titleString;
    private JButton loadGameBtn;
    private JButton backBtn;
    //private JList<String> loadList;
    private JList<LoadListElement> loadList;

    public LoadGameScreen(JFrame frame, Mode mode) {
        super(frame, mode);
    }

    public void addComponents() {
        addLists();

        // Title
        titleString = new JLabel("Load Game");
        setTitle(titleString);

        loadGameBtn = new JButton("Load Game");
        backBtn = new JButton("Back");
        setButtons(new JButton[]{loadGameBtn, backBtn});

        setActionListeners();
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
            savedSudokus[i] = new LoadListElement(name, (int) result[3], board.length, board, (int) result[1], (int) result[2]);
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

    private void setActionListeners(){
        loadGameBtn.addActionListener(new loadAction());
        backBtn.addActionListener(new backAction());
    }

    private Model startGame(int k, int n, Mode mode) {
        return getModel(k, n, mode);
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
}