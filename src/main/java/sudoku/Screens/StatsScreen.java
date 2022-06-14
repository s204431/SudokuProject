package sudoku.Screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
	This class contains the menu screen when clicking stats.

	Responsible: Magnus
 */

public class StatsScreen extends MenuScreen {
	
	private int[] stats;
	private JLabel titleString;
	private JLabel[] attempts,
					 time,
					 name;
	private JButton backBtn;
	
	//Constructor taking the frame.
	public StatsScreen(JFrame frame) {
		super(frame);
	}
	
	//Adds the components to the screen.
	public void addComponents() {
		// Title
		titleString = new JLabel ("Statistics");
		setTitle(titleString);
		// 3 JPanels in one 'PANEL'
		JPanel PANEL = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel middlePanel = new JPanel();
		JPanel rightPanel = new JPanel();

		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		
		PANEL.setPreferredSize(new Dimension(400, 800));
		//sets size
		leftPanel.setPreferredSize(new Dimension(110, 500));
		middlePanel.setPreferredSize(new Dimension(130, 500));
		rightPanel.setPreferredSize(new Dimension(110, 500));
		
		// Sets up elements in the different 3 panels.

		attempts = new JLabel[5];//number of times completed
		time = new JLabel[5];//fastest time
		name = new JLabel[5];//difficulty
		stats = MVC.Model.loadStat();//loads from txt file
		
		String hours;
		String minutes;
		String seconds;
		
		name[0] = new JLabel ("Difficulty:");
		name[1] = new JLabel ("Easy");
		name[2] = new JLabel ("Medium");
		name[3] = new JLabel ("Hard");
		name[4] = new JLabel ("Extreme");
		
		time[0] = new JLabel("Fastest time:");
		attempts[0] = new JLabel("Completed:");
		for(int i = 1; i < 5; i++) {//convertes seconds to hour/minute/second
			seconds = String.valueOf(stats[i - 1] % 60);
			minutes = String.valueOf(stats[i - 1] / 60 % 60);
			hours = String.valueOf(stats[i - 1] / 3600);
			time[i] = new JLabel (hours + ":" + minutes + ":" + seconds);
			attempts[i] = new JLabel (String.valueOf(stats[i + 3]));
		}
		for(int i = 0; i < 5; i++) {//writes the labels
			setLabelFont(name[i]);
			setLabelFont(attempts[i]);
			setLabelFont(time[i]);
			leftPanel.add(name[i]);
			leftPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
			middlePanel.add(time[i]);
			middlePanel.add(Box.createRigidArea(new Dimension(0, spacing)));
			rightPanel.add(attempts[i]);
			rightPanel.add(Box.createRigidArea(new Dimension(0, spacing)));
		}
		setPanel(PANEL, new JComponent[] {leftPanel, middlePanel, rightPanel});
        backBtn = new JButton("Back");
        setButtons(new JButton[]{backBtn});
		setActionListeners();
	}
	
	//Sets the font for the given label.
	private void setLabelFont(JLabel label) {
		label.setFont(new Font("Serif", Font.BOLD, 20));
	}
	
	//Sets the action listeners for the buttons.
    private void setActionListeners(){//makes button pressable
        backBtn.addActionListener(new backAction());
    }
	
    //Action listener for the back button.
    class backAction implements ActionListener {//sets "Back" to go to Mainscreen
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }

}
