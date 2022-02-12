package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class View extends JPanel implements MouseListener, KeyListener {

	private Model model;
	private Controller controller;
	public int boardX = 0; //x coordinate for top left corner.
	public int boardY = 0; //y coordinate for top left corner.
	private boolean dragging = false;
	private int[] mouseBoardVector = new int[] {0, 0};

    private int[] clickedPosition = new int[] {0, 0};
	
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
        
        setFocusable(true);
        requestFocus();
        addMouseListener(this);
        addKeyListener(this);
        (new Thread(new BoardDragger())).start();
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
        int iss = model.innerSquareSize;
        for (int i = 0; i < model.getBoardSize(); i++) {
            for (int j = 0; j < model.getBoardSize(); j++) {
                Field field = model.board[i][j];
                if (clickedPosition[0] == i && clickedPosition[1] == j) {
                    g2.setColor(gray);
                } else if (clickedPosition[0] == i || clickedPosition[1] == j || (clickedPosition[0]/iss == i/iss && clickedPosition[1]/iss == j/iss)) {
                    g2.setColor(lightGray);
                } else {
                    g2.setColor(white);
                }
                g2.fillRect(boardX + i * Field.HEIGHT, boardY + j * Field.WIDTH, Field.WIDTH, Field.HEIGHT);
                g2.setColor(black);
                g2.drawRect(boardX + i * Field.HEIGHT, boardY + j * Field.WIDTH, Field.WIDTH, Field.HEIGHT);
                g2.setFont(new Font("TimesRoman", Font.BOLD, 30));
                int value = model.board[i][j].value;
                if (value > 0 && value <= model.getBoardSize()) {
                	g2.drawString(""+value, boardX + i * Field.HEIGHT + Field.HEIGHT/2, boardY + j * Field.WIDTH + Field.WIDTH/2);
                }
            }
        }

        // Draw thick lines
        Stroke oldStroke = g2.getStroke();
        for (int i = 0; i < iss; i++) {
            for (int j = 0; j < iss; j++) {
                g2.setColor(black);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(boardX + i * Field.HEIGHT * iss, boardY + j * Field.WIDTH * iss, Field.WIDTH*iss, Field.HEIGHT*iss);
            }
        }
        g2.setStroke(oldStroke);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	if (e.getX() >= boardX && e.getY() >= boardY && e.getX() <= boardX+model.getBoardSize()*Field.WIDTH && e.getY() <= boardY+model.getBoardSize()*Field.HEIGHT)
        clickedPosition = new int[]{(e.getX()-boardX)/Field.WIDTH, (e.getY()-boardY)/Field.HEIGHT};
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    	Point mousePos = getMousePosition();
    	mouseBoardVector = new int[] {boardX-mousePos.x, boardY-mousePos.y};
    	dragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    	dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    	dragging = false;
    }

	@Override
	public void keyTyped(KeyEvent e) {
		controller.keyTyped(e, clickedPosition);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	private class BoardDragger implements Runnable {
		@Override
		public void run() {
			while (true) {
				if (dragging) {
					Point mousePos = getMousePosition();
					boardX = mousePos.x+mouseBoardVector[0];
					boardY = mousePos.y+mouseBoardVector[1];
					repaint();
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
