package multiplayer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseWheelEvent;

import org.jspace.ActualField;
import org.jspace.FormalField;

import MVC.View;
import sudoku.Field;
import sudoku.Main;

import javax.swing.*;

/*
	The MultiplayerView extends the View and contains mostly the same functionality,
	but with added functionality for multiplayer.

	Responsible: Jens & Michael
*/

public class MultiplayerView extends View {
	private JPanel opponentPanel;
	protected int opponentWindowWidth, opponentWindowHeight;
	private boolean updateAllowed = true;
	private String textAboveBoard;

	public MultiplayerView(MultiplayerModel model) {
		super(model);
		try {
			((MultiplayerModel) model).toOpponent.put("lock", "dragging");
			((MultiplayerModel) model).toOpponent.put("boardposition", (int)boardX, (int)boardY, (int)fieldWidth, (int)fieldHeight);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(new BoardDragger()).start();
		new Thread(new UpdateTimer()).start();
	}

	//Creates the opponent's board as a panel and adds it to the frame.
	public void addOpponentBoard() {
		opponentPanel = new OpponentBoard();
		opponentPanel.setBounds((getPreferredSize().width - buttonPanel.getWidth()) / 2, 0, (getPreferredSize().width - buttonPanel.getWidth()) / 2, getPreferredSize().height);
		opponentPanel.setFocusable(false);
		frame.remove(this);
		frame.add(opponentPanel);
		frame.add(this);
	}
	
	//Quits the multiplayer game.
    public void quitToMenu() {
    	close = true;
    	((MultiplayerModel) model).disconnect();
        frame.dispose();
        Main.restart();
    }
    
    //Paint method, that paints most of the graphics.
    public void paint(Graphics g) {
		clearPaint(g);
        Graphics2D g2 = (Graphics2D) g;
    	if (!((MultiplayerModel) model).started) {
        	g2.setFont(new Font("TimesRoman", Font.BOLD, Main.SCREEN_HEIGHT/12));
			String text = "Waiting for opponent...";
			int textWidth = g2.getFontMetrics().stringWidth(text);
			int textHeight = g2.getFontMetrics().getHeight();
        	g2.drawString(text, getPreferredSize().width / 2 - textWidth / 2, getPreferredSize().height / 2 - textHeight / 2);
        	return;
    	}
		opponentPanel.repaint();
        super.paint(g);

		// Text clarifying which board is which and the respective progress
		g2.setFont(new Font("Serif", Font.BOLD, windowWidth / 40));
		Color textColor;
		if (((MultiplayerModel) model).winner == 1) {
			textColor = new Color(0, 200, 0);
			textAboveBoard = "You win!";
		} else if (((MultiplayerModel) model).winner == 2) {
			textColor = Color.RED;
			textAboveBoard = "You lose!";
		} else {
			textColor = Color.BLACK;
			textAboveBoard = "You: " + model.computeFilledInFields(model.board) + "/" + (model.board.length * model.board[0].length);
		}
		int textWidth = (windowWidth-buttonPanel.getWidth()) / 4 - g2.getFontMetrics().stringWidth(textAboveBoard) / 2;
		int textHeight = g2.getFontMetrics().getHeight();
		int offSet = 10;
		g2.setColor(buttonPanel.getBackground());
		g2.fillRect(0, 0, windowWidth, textHeight + offSet);
		g2.setColor(textColor);
		g2.drawString(textAboveBoard, textWidth, textHeight - offSet);

		// Line seperating the two boards
		g2.setColor(Color.BLACK);
		g2.drawLine((getPreferredSize().width - buttonPanel.getWidth()) / 2 - 1, 0, (getPreferredSize().width - buttonPanel.getWidth()) / 2 - 1, getPreferredSize().height);
	}

    //Overwritten method from View that resets the position of the board to the default position.
	public void resetBoardPosition() {
    	fieldWidth = (int)((Field.DEFAULT_WIDTH*windowWidth / 1170.0) / 1.5);
    	fieldHeight = fieldWidth;
		boardX = (getPreferredSize().width - buttonPanel.getWidth()) / 4 - fieldWidth * model.board.length / 2;
		boardY = getPreferredSize().height / 2 - fieldHeight * model.board.length / 2;
		updateBoardPosition();
	}
    
	//Overwritten method, that additionally sends an update to the other player.
	public void mouseWheelMoved(MouseWheelEvent e) {
		super.mouseWheelMoved(e);
		updateBoardPosition();
	}
	
	//Sends an update to the other player with the current position and size of the board.
	private void updateBoardPosition() {
		try {
			if (updateAllowed) {
				((MultiplayerModel) model).toOpponent.get(new ActualField("lock"), new ActualField("dragging"));
				((MultiplayerModel) model).toOpponent.get(new ActualField("boardposition"), new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class));
				((MultiplayerModel) model).toOpponent.put("boardposition", (int)boardX, (int)boardY, (int)fieldWidth, (int)fieldHeight);
				((MultiplayerModel) model).toOpponent.put("lock", "dragging");
				updateAllowed = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//This thread insures that updates are not sent to the other player too often.
	private class UpdateTimer implements Runnable {
		public void run() {
			while (!close) {
				if (!updateAllowed) {
					try {
						Thread.sleep(10);
						updateAllowed = true;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
    
	//Overwritten parallel thread that moves the board when dragging.
    protected class BoardDragger implements Runnable {
  		public void run() {
  			while (!close) {
  				if (dragging && ((MultiplayerModel) model).started) {
  					Point mousePos = getMousePosition();
  					if (mousePos != null) {
  						boardX = mousePos.x + mouseBoardVector[0];
  						boardY = mousePos.y + mouseBoardVector[1];
  						updateBoardPosition();
  					}
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
	/*
		OpponentBoard is a class that extends JPanel, which allows showing
		the opponent's board in multiplayer.
	*/
    private class OpponentBoard extends JPanel {
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			super.paint(g);
			Object[] tuple = null;
			try {
				((MultiplayerModel) model).fromOpponent.get(new ActualField("lock"), new ActualField("dragging"));
				tuple = ((MultiplayerModel) model).fromOpponent.query(new ActualField("boardposition"), new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class));
				((MultiplayerModel) model).fromOpponent.put("lock", "dragging");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int[] opponentBoardPosition = convertCoordinate((int) tuple[1], (int) tuple[2], (int) tuple[3]);
			int opponentBoardX = opponentBoardPosition[0];
			int opponentBoardY = opponentBoardPosition[1];
			
			int opponentFieldWidth = opponentBoardPosition[2];

			// Draw fields and numbers
			Color red = new Color(175, 4, 4);
			Color black = Color.BLACK;
			Color white = Color.WHITE;
			Color lightGray = new Color(200, 200, 200);
			Color gray = new Color(130, 130, 130);
			Color darkGray = new Color(85, 85, 85);
			int iss = model.innerSquareSize;
			for (int i = 0; i < model.getBoardSize(); i++) {
				for (int j = 0; j < model.getBoardSize(); j++) {
					g2.setColor(white);
					g2.fillRect(opponentBoardX + j * opponentFieldWidth, opponentBoardY + i * opponentFieldWidth, opponentFieldWidth, opponentFieldWidth);
					g2.setColor(black);
					g2.drawRect(opponentBoardX + j * opponentFieldWidth, opponentBoardY + i * opponentFieldWidth, opponentFieldWidth, opponentFieldWidth);

					int value = ((MultiplayerModel) model).opponentBoard[i][j].value;
					int valueDigits = String.valueOf(value).length();
					int scaling = (valueDigits + 1) * 3;
					g2.setFont(new Font("Courier", Font.BOLD, (int)((40 - scaling) * opponentFieldWidth / Field.DEFAULT_WIDTH)));
					String text = "" + value;
					int fontHeight = g2.getFontMetrics().getHeight();
					int fontWidth = g2.getFontMetrics().stringWidth(text);
					if (value > 0 && value <= model.innerSquareSize * model.innerSquareSize) {
						int x = opponentBoardX + j * opponentFieldWidth + opponentFieldWidth / 2 - fontWidth / 2;
						int y = opponentBoardY + i * opponentFieldWidth + opponentFieldWidth / 2 + fontHeight / 3;
						if (model.board[i][j].interactable) {
							g2.drawString("?", x, y);
						}
						else {
							g2.drawString(value+"", x, y);
						}
					}
				}
			}

			// Draw thick lines
			Stroke oldStroke = g2.getStroke();
			for (int i = 0; i < model.numInnerSquares; i++) {
				for (int j = 0; j < model.numInnerSquares; j++) {
					g2.setColor(black);
					g2.setStroke(new BasicStroke(3));
					g2.drawRect(opponentBoardX + j * opponentFieldWidth * iss, opponentBoardY + i * opponentFieldWidth * iss, opponentFieldWidth * iss, opponentFieldWidth * iss);
				}
			}
			g2.setStroke(oldStroke);

			// Text clarifying which board is which and the respective progress
			g2.setFont(new Font("Serif", Font.BOLD, windowWidth / 40));
			Color textColor;
			if (((MultiplayerModel) model).winner == 2) {
				textColor = new Color(0, 200, 0);
				textAboveBoard = "You win!";
			} else if (((MultiplayerModel) model).winner == 1) {
				textColor = Color.RED;
				textAboveBoard = "You lose!";
			} else {
				textColor = Color.BLACK;
				textAboveBoard = "Opponent: " + model.computeFilledInFields(((MultiplayerModel) model).opponentBoard) + "/" + (model.board.length * model.board[0].length);
			}
			int textWidth = (windowWidth-buttonPanel.getWidth()) / 4 - g2.getFontMetrics().stringWidth(textAboveBoard) / 2;
			int textHeight = g2.getFontMetrics().getHeight();
			int offSet = 10;
			g2.setColor(buttonPanel.getBackground());
			g2.fillRect(0, 0, windowWidth, textHeight + offSet);
			g2.setColor(textColor);
			g2.drawString(textAboveBoard, textWidth, textHeight - offSet);
		}
		
		//Converts from coordinate in opponent window to coordinate in this window.
		private int[] convertCoordinate(int x, int y, int fieldWidth) {
			double px = ((double)windowWidth)/opponentWindowWidth;
			double py = ((double)windowHeight)/opponentWindowHeight;
			return new int[] {(int)(px * x), (int)(py * y), (int)(px * fieldWidth)};
		}
	}
}
