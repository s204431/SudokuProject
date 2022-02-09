package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class View extends JScrollPane implements MouseListener {

	private Model model;
	private Controller controller;
	
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
    }
    
    public void setController(Controller controller) {
    	this.controller = controller;
    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw fields
        Color black = Color.BLACK;
        Color lightGray = Color.LIGHT_GRAY;
        for (int i = 0; i < model.getBoardSize(); i++) {
            for (int j = 0; j < model.getBoardSize(); j++) {
                Field field = model.board[i][j];
                g2.setColor(lightGray);
                g2.fillRect(model.boardX + i * Field.HEIGHT, model.boardY + j * Field.WIDTH, Field.WIDTH, Field.HEIGHT);
                g2.setColor(black);
                g2.drawRect(model.boardX + i * Field.HEIGHT, model.boardY + j * Field.WIDTH, Field.WIDTH, Field.HEIGHT);
            }
        }

        // Draw thick lines
        Stroke oldStroke = g2.getStroke();
        int iss = model.innerSquareSize;
        for (int i = 0; i < iss; i++) {
            for (int j = 0; j < iss; j++) {
                g.setColor(black);
                g2.setStroke(new BasicStroke(5));
                g.drawRect(model.boardX + i * Field.HEIGHT * iss, model.boardY + j * Field.WIDTH * iss, Field.WIDTH*iss, Field.HEIGHT*iss);
            }
        }
        g2.setStroke(oldStroke);


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.mouseClicked(e.getY() + model.boardY, e.getX() + model.boardX);
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
