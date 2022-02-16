package sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class View extends JPanel implements MouseListener, KeyListener, MouseWheelListener {
	
	private static final long serialVersionUID = 1L;
	private Model model;
	private Controller controller;
	public int boardX = 0; //x coordinate for top left corner.
	public int boardY = 0; //y coordinate for top left corner.
	private boolean dragging = false;
	private int[] mouseBoardVector = new int[] {0, 0};
	private int fieldWidth = Field.DEFAULT_WIDTH;
	private int fieldHeight = Field.DEFAULT_HEIGHT;

    public int[] clickedPosition = new int[] {0, 0};
	
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
        addMouseWheelListener(this);
        (new Thread(new BoardDragger())).start();
    }
    
    public void setController(Controller controller) {
    	this.controller = controller;
    }

    //Draws everything on screen.
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw fields and numbers

        Color black = Color.BLACK;
        Color white = Color.WHITE;
        Color lightGray = Color.LIGHT_GRAY;
        Color gray = Color.GRAY;
        Color darkGray = Color.DARK_GRAY;
        int iss = model.innerSquareSize;
        for (int i = 0; i < model.getBoardSize(); i++) {
            for (int j = 0; j < model.getBoardSize(); j++) {
                if (clickedPosition[0] == i && clickedPosition[1] == j) {
                    g2.setColor(darkGray);
                } else if (clickedPosition[0] == i || clickedPosition[1] == j ||
                        (clickedPosition[0]/iss == i/iss && clickedPosition[1]/iss == j/iss)) {
                    g2.setColor(lightGray);
                } else if (model.getBoard()[i][j].getValue() ==                                 //marks other fields
                        model.getBoard()[clickedPosition[0]][clickedPosition[1]].getValue()     //with same value
                        && model.getBoard()[i][j].getValue() != 0) {                            //as gray color
                    g2.setColor(gray);
                } else {
                    g2.setColor(white);
                }
                g2.fillRect(boardX + j * fieldWidth, boardY + i * fieldHeight, fieldWidth, fieldHeight);
                g2.setColor(black);
                g2.drawRect(boardX + j * fieldWidth, boardY + i * fieldHeight, fieldWidth, fieldHeight);
                g2.setFont(new Font("TimesRoman", Font.BOLD, 30*fieldWidth/Field.DEFAULT_WIDTH));
                int value = model.board[i][j].value;
                if (value > 0 && value <= model.getBoardSize()) {
                	g2.drawString(""+value, boardX + j * fieldWidth + fieldWidth/2, boardY + i * fieldHeight + fieldHeight/2);
                }
            }
        }

        // Draw thick lines
        Stroke oldStroke = g2.getStroke();
        for (int i = 0; i < iss; i++) {
            for (int j = 0; j < iss; j++) {
                g2.setColor(black);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(boardX + j * fieldWidth * iss, boardY + i * fieldHeight * iss, fieldWidth*iss, fieldHeight*iss);
            }
        }
        g2.setStroke(oldStroke);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	if (e.getButton() == MouseEvent.BUTTON1 && e.getX() >= boardX && e.getY() >= boardY && e.getX() <= boardX+model.getBoardSize()*fieldWidth && e.getY() <= boardY+model.getBoardSize()*fieldHeight) {
            clickedPosition = new int[]{(e.getY()-boardY)/fieldHeight, (e.getX()-boardX)/fieldWidth};
            repaint();
    	}
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
		if (e.getKeyChar() == KeyEvent.VK_SPACE) {
			boardX = 0;
			boardY = 0;
			fieldWidth = Field.DEFAULT_WIDTH;
			fieldHeight = Field.DEFAULT_HEIGHT;
			repaint();
		}
		else {
			controller.keyTyped(e, clickedPosition);	
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0 || (fieldWidth > 20 && fieldHeight > 20)) {
			double fieldWidthDist = (e.getX()-boardX)/(double)fieldWidth;
			double fieldHeightDist = (e.getY()-boardY)/(double)fieldHeight;
			int w = e.getWheelRotation()*fieldWidth/20;
			int h = e.getWheelRotation()*fieldHeight/20;
			fieldWidth -= w == 0 ? e.getWheelRotation() : w;
			fieldHeight -= h == 0 ? e.getWheelRotation() : h;
			boardX -= (fieldWidthDist-(e.getX()-boardX)/(double)fieldWidth)*fieldWidth;
			boardY -= (fieldHeightDist-(e.getY()-boardY)/(double)fieldHeight)*fieldHeight;
			repaint();
		}
	}
	
	//Concurrent thread that moves the board when dragging.
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
