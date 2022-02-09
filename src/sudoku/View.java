package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class View extends JScrollPane implements MouseListener {

	private Model model;
	private Controller controller;

    private int[] clicked = new int[]{0,0};
	
    public View(Model model) {
    	this.model = model;
        setPreferredSize(new Dimension(630, 630));
        // Create frame
        JFrame frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addMouseListener(this);
    }
    
    public void setController(Controller controller) {
    	this.controller = controller;
    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw fields and numbers
        Color black = Color.BLACK;
        Color white = Color.WHITE;
        Color lightGray = Color.LIGHT_GRAY;
        Color gray = Color.GRAY;
        for (int i = 0; i < model.getBoardSize(); i++) {
            for (int j = 0; j < model.getBoardSize(); j++) {
                Field field = model.board[i][j];
                if (clicked[0] == i && clicked[1] == j) {
                    g2.setColor(gray);
                } else if (clicked[0] == i || clicked[1] == j || (clicked[0]/3 == i/3 && clicked[1]/3 == j/3)) {
                    g2.setColor(lightGray);
                } else {
                    g2.setColor(white);
                }
                g2.fillRect(model.boardY + j * Field.WIDTH, model.boardX + i * Field.HEIGHT, Field.WIDTH, Field.HEIGHT);
                g2.setColor(black);
                g2.drawRect(model.boardY + j * Field.WIDTH, model.boardX + i * Field.HEIGHT, Field.WIDTH, Field.HEIGHT);
                g2.setFont(new Font("TimesRoman", Font.BOLD, 30));
                g2.drawString("9", model.boardY + j * Field.WIDTH + Field.WIDTH/2, model.boardX + i * Field.HEIGHT + Field.HEIGHT/2);
            }
        }

        // Draw thick lines
        Stroke oldStroke = g2.getStroke();
        int iss = model.innerSquareSize;
        for (int i = 0; i < iss; i++) {
            for (int j = 0; j < iss; j++) {
                g2.setColor(black);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(model.boardX + i * Field.HEIGHT * iss, model.boardY + j * Field.WIDTH * iss, Field.WIDTH*iss, Field.HEIGHT*iss);
            }
        }
        g2.setStroke(oldStroke);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clicked = new int[]{e.getY()/Field.HEIGHT, e.getX()/Field.WIDTH};
        repaint();

        System.out.println(e.getY()/Field.HEIGHT + " " + e.getX()/Field.WIDTH);
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
}
