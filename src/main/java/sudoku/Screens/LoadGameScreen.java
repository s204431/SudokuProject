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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;

	//This class contains the menu screen when clicking load existing sudoku.

public class LoadGameScreen extends MenuScreen {
    private JLabel titleString;
    private JButton loadGameBtn, backBtn;
    private JList<LoadListElement> loadList;
    private boolean isHost, assistMode;

    //Constructor taking the frame and the selected mode.
    public LoadGameScreen(JFrame frame, Mode mode) {
        super(frame, mode);
    }

    //Constructor used for multiplayer mode.
    public LoadGameScreen(JFrame frame, Mode mode, boolean isHost) {
        super(frame, mode);
        this.isHost = isHost;
    }

    //Constructor allowing to set assist mode.
    public LoadGameScreen(JFrame frame, Mode mode, boolean isHost, boolean assistMode) {
        super(frame, mode);
        this.isHost = isHost;
        this.assistMode = assistMode;
    }

    //Adds the components to the screen.
    public void addComponents() {
        // Title
        titleString = new JLabel("Load Game");
        setTitle(titleString);

        // Box of loadable sudoku's
        addLists();

        // Buttons
        loadGameBtn = new JButton("Load Game");
        backBtn = new JButton("Back");
        setButtons(new JButton[]{loadGameBtn, backBtn});

        // Action listeners
        setActionListeners();
    }

    /*
    addLists() takes the ".su" files from the savedsudokus folder, and creates small viewable images
    inside a box of them. These sudoku files can then be accessed from this frame and are continuable
    if desired.
     */
    
    //Adds the list containing the loadable sudokus.
    private void addLists() {//reads the loadable sudokus
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
        loadList.setCellRenderer(new LoadListRenderer(btnWidth * 2));
        loadList.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    new loadAction().actionPerformed(new ActionEvent(this,0,""));
                }
            }
        });
        
        // Add scroll function
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(loadList);
        loadList.setLayoutOrientation(JList.VERTICAL);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(btnWidth * 2,300));
        add(scrollPane);
        add(Box.createRigidArea(new Dimension(0, spacing)));
    }
    
    //Set the action listeners for the buttons.
    private void setActionListeners(){//makes buttons pressable
        loadGameBtn.addActionListener(new loadAction());
        backBtn.addActionListener(new backAction());
    }

    //Starts the game.
    private Model startGame(int k, int n, Mode mode) {
        return getModel(k, n, mode, assistMode);
    }

    //Action listener for the load button.
    class loadAction implements ActionListener {//goes to multiplayer load manu, depending on if you are host or not
        public void actionPerformed(ActionEvent e) {
        	if (loadList.getSelectedValue() == null) {
        		return;
        	}
            frame.dispose();
            if (isHost) {
                setMultiplayerInstance(isHost,"").loadAndUpdate(loadList.getSelectedValue().name);
            } else {
                startGame(1, 1, mode).loadAndUpdate(loadList.getSelectedValue().name);
            }
        }
    }

    //Action listener for the back button.
    private class backAction implements ActionListener {//goes back 1 menu
        public void actionPerformed(ActionEvent e) {
            backAction();
        }
    }
}