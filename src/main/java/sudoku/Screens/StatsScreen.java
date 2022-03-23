package sudoku.Screens;

import MVC.Model.Mode;
import sudoku.Screens.NewGameScreen.backAction;
import sudoku.Screens.NewGameScreen.generateNewAction;
import sudoku.Screens.NewGameScreen.loadGameAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatsScreen extends MenuScreen {
	
	private int[] stats;
	private JLabel titleString;
	private JLabel[] attempts;
	private JLabel[] time;
	private JLabel[] name;
	private JLabel completeString;
	private JLabel timeString;
	private JLabel difficultyString;
	private JButton backBtn;
	
	public StatsScreen(JFrame frame) {
		super(frame);
	}
	
	public void addComponents() {
		titleString = new JLabel ("Statistics");
		setTitle(titleString);
		
		JPanel PANEL = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel middlePanel = new JPanel();
		JPanel rightPanel = new JPanel();
		
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		
		PANEL.setPreferredSize(new Dimension(300, 800));
	
		leftPanel.setPreferredSize(new Dimension(110, 500));
		middlePanel.setPreferredSize(new Dimension(110, 500));
		rightPanel.setPreferredSize(new Dimension(110, 500));
		
	
		attempts = new JLabel[5];
		time = new JLabel[5];
		name = new JLabel[5];
		stats = MVC.Model.loadStat();
		
		String hours;
		String minutes;
		String seconds;
		
		name[0] = new JLabel ("Difficulty:");
		name[1] = new JLabel ("Easy");
		name[2] = new JLabel ("Medium");
		name[3] = new JLabel ("Hard");
		name[4] = new JLabel ("Extreme");
		
		time[0] = new JLabel("Records:");
		attempts[0] = new JLabel("Completed:");
		for(int i = 1; i < 5; i++) {
			seconds = String.valueOf(stats[i - 1] % 60);
			minutes = String.valueOf(stats[i - 1] / 60 % 60);
			hours = String.valueOf(stats[i - 1] / 3600);
			time[i] = new JLabel (hours + ":" + minutes + ":" + seconds);
			attempts[i] = new JLabel (String.valueOf(stats[i + 3]));
			

		}
		
		for(int i = 0; i < 5; i++) {
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
		
		setPanel(PANEL, new JComponent[] {leftPanel,middlePanel,rightPanel});
		
        backBtn = new JButton("Back");
        setButtons(new JButton[]{backBtn});
		
		setActionListeners();
		
	}
	
	private void setLabelFont(JLabel label) {
		label.setFont(new Font("Serif", Font.BOLD, 20));
	}
	
    private void setActionListeners(){
        backBtn.addActionListener(new backAction());
    }
	
    class backAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            changePanel();
            new MainScreen(frame);
        }
    }

}
