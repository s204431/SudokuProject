package sudoku;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import MVC.Model;
import solvers.SudokuSolver;

/*
   This class renders files from the 'savedsudokus' folder
   and uses these files to upload viewable saved sudoku
   files that can be selected and continued after selection.
*/

public class LoadListRenderer extends JPanel implements ListCellRenderer<LoadListElement> {
	private JPanel panel = new JPanel(null);
    private JLabel nameLabel = new JLabel();
    private JLabel difficultyLabel = new JLabel();
    private JLabel sizeLabel = new JLabel();
    private PreviewPanel previewPanel = new PreviewPanel();
 
    public LoadListRenderer(int width) {
		//Initializes the size and layout of the list renderer.
    	setLayout(new BorderLayout(5, 5));
    	 
        panel.setPreferredSize(new Dimension(width - 20, 100));
        nameLabel.setBounds(0, 0, width / 2, 20);
        difficultyLabel.setBounds(0, 50, width / 3, 20);
        sizeLabel.setBounds(width / 3, 50, width / 3, 20);
        previewPanel.setBounds(width - 115, 5, 90, 90);
        
        nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.BOLD, 15));
        difficultyLabel.setForeground(Color.GRAY);
        sizeLabel.setForeground(Color.GRAY);
        panel.add(nameLabel);
        panel.add(difficultyLabel);
        panel.add(sizeLabel);
        panel.add(previewPanel);
        add(panel, BorderLayout.CENTER);
    }
 	//Returns all the elements in an intuitive way.
    public Component getListCellRendererComponent(JList<? extends LoadListElement> list, LoadListElement element,
												  int index, boolean isSelected, boolean cellHasFocus) {
    	nameLabel.setText(element.name);
    	if (element.difficulty >= 0) {
        	difficultyLabel.setText("Difficulty: " + SudokuSolver.getDifficultyString(element.difficulty));
    	}
    	else {
        	difficultyLabel.setText("Difficulty: -");
    	}
    	sizeLabel.setText("Size: " + element.size + "x" + element.size);
    	previewPanel.setElement(element);
    	
    	nameLabel.setOpaque(true);
    	difficultyLabel.setOpaque(true);
    	sizeLabel.setOpaque(true);
    	previewPanel.setOpaque(true);

		//Makes selected sudoku files more noticeable.
    	if (isSelected) {
    		nameLabel.setBackground(list.getSelectionBackground());
    		difficultyLabel.setBackground(list.getSelectionBackground());
    		sizeLabel.setBackground(list.getSelectionBackground());
    		previewPanel.setBackground(list.getSelectionBackground());
    		panel.setBackground(list.getSelectionBackground());
    		setBackground(list.getSelectionBackground());
    	}
    	else {
    		nameLabel.setBackground(list.getBackground());
    		difficultyLabel.setBackground(list.getBackground());
    		sizeLabel.setBackground(list.getBackground());
    		previewPanel.setBackground(list.getBackground());
    		panel.setBackground(list.getBackground());
    		setBackground(list.getBackground());
    	}
        return this;
    }

	/*
		The PreviewPanel is a small ui of how the sudoku looks.
		If the LoadGameScreen is active these small boxes of a sudoku
		instance are presented in the right end of the list element.
	*/

    private static class PreviewPanel extends JPanel {
    	private LoadListElement element;
    	
    	public void setElement(LoadListElement element) {
    		this.element = element;
    	}
    	//Paints a small version of the actual UI.
	    public void paint(Graphics g) {
	        super.paint(g);
	
	        Graphics2D g2 = (Graphics2D) g;
	        
	        // Draw fields and numbers
	        Color black = Color.BLACK;
	        Color white = Color.WHITE;
	        int fieldWidth = 10;
	        int fieldHeight = 10;
	        int iss = element.innerSquareSize;
	        for (int i = 0; i < iss*element.numInnerSquares; i++) {
	            for (int j = 0; j < iss*element.numInnerSquares; j++) {
                    g2.setColor(white);
	                g2.fillRect(j * fieldWidth, i * fieldHeight, fieldWidth, fieldHeight);
	                g2.setColor(black);
	                g2.drawRect(j * fieldWidth, i * fieldHeight, fieldWidth, fieldHeight);
	                g2.setFont(new Font("TimesRoman", Font.BOLD, (int) (0.86 * fieldWidth)));
	                int value = element.board[i][j].value;
	                if (value > 0 && value <= element.innerSquareSize*element.innerSquareSize) {
	                	g2.drawString("" + value, j * fieldWidth + fieldWidth/2, i * fieldHeight + fieldHeight/2+3);
	                }
	            }
	        }
	        // Draw thick lines
	        Stroke oldStroke = g2.getStroke();
	        for (int i = 0; i < element.numInnerSquares; i++) {
	            for (int j = 0; j < element.numInnerSquares; j++) {
	                //g2.setColor(model.sudokuSolved(model.board, model.innerSquareSize) ? new Color(0, 200, 0) : black);
	            	g2.setColor(black);
	                g2.setStroke(new BasicStroke(2));
	                g2.drawRect(j * fieldWidth * iss, i * fieldHeight * iss, fieldWidth*iss, fieldHeight*iss);
	            }
	        }
	        g2.setStroke(oldStroke);
	    }
    }
}



