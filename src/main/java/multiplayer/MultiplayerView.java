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

import MVC.Model;
import MVC.View;
import sudoku.Field;
import sudoku.Main;

import javax.swing.*;

public class MultiplayerView extends View {
	private JPanel opponentPanel;

	public MultiplayerView(MultiplayerModel model) {
		super(model);

		try {
			((MultiplayerModel) model).toOpponent.put("lock", "dragging");
			((MultiplayerModel) model).toOpponent.put("boardposition", boardX, boardY, fieldWidth, fieldHeight);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(new BoardDragger()).start();
	}

	public void addOpponentBoard() {
		opponentPanel = new OpponentBoard();
		opponentPanel.setBounds((getPreferredSize().width-200) / 2, 0, (getPreferredSize().width-200) / 2, getPreferredSize().height);
		opponentPanel.setFocusable(false);
		frame.remove(this);
		frame.add(opponentPanel);
		frame.add(this);
	}
	
    public void quitToMenu() {
    	close = true;
    	((MultiplayerModel) model).disconnect();
        frame.dispose();
        Main.main(null);
    }
    
    public void paint(Graphics g) {
		clearPaint(g);
        Graphics2D g2 = (Graphics2D) g;
    	if (!((MultiplayerModel) model).started) {
        	g2.setFont(new Font("TimesRoman", Font.BOLD, 50));
        	g2.drawString("Waiting for opponent...", getPreferredSize().width/2-getPreferredSize().height/4, getPreferredSize().height/2-getPreferredSize().height/10);
        	buttonPanel.repaint();
        	return;
    	}
		opponentPanel.repaint();
        super.paint(g);

		g2.drawLine((getPreferredSize().width - 200) / 2 - 1, 0, (getPreferredSize().width - 200) / 2 - 1, getPreferredSize().height);
    }

	public void resetBoardPosition() {
		fieldWidth = (int)(Field.DEFAULT_WIDTH / 1.5);
		fieldHeight = (int)(Field.DEFAULT_HEIGHT / 1.5);
		boardX = (getPreferredSize().width - 200) / 4 - fieldWidth * model.board.length / 2;
		boardY = getPreferredSize().height / 2 - fieldHeight * model.board.length / 2;
		updateBoardPosition();
	}
    
	public void mouseWheelMoved(MouseWheelEvent e) {
		super.mouseWheelMoved(e);
		updateBoardPosition();
	}
	
	private void updateBoardPosition() {
		try {
			((MultiplayerModel) model).toOpponent.get(new ActualField("lock"), new ActualField("dragging"));
			((MultiplayerModel) model).toOpponent.get(new ActualField("boardposition"), new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class));
			((MultiplayerModel) model).toOpponent.put("boardposition", boardX, boardY, fieldWidth, fieldHeight);
			((MultiplayerModel) model).toOpponent.put("lock", "dragging");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
  //Concurrent thread that moves the board when dragging.
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

    private class OpponentBoard extends JPanel {
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;

			super.paint(g);

			//setVisible(true);
			Object[] tuple = null;
			try {
				((MultiplayerModel) model).fromOpponent.get(new ActualField("lock"), new ActualField("dragging"));
				tuple = ((MultiplayerModel) model).fromOpponent.query(new ActualField("boardposition"), new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class));
				((MultiplayerModel) model).fromOpponent.put("lock", "dragging");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int opponentBoardX = (int) tuple[1];
			int opponentBoardY = (int) tuple[2];
			int opponentFieldWidth = (int) tuple[3];
			int opponentFieldHeight = (int) tuple[4];

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
					g2.fillRect(opponentBoardX + j * opponentFieldWidth, opponentBoardY + i * opponentFieldHeight, opponentFieldWidth, opponentFieldHeight);
					g2.setColor(black);
					g2.drawRect(opponentBoardX + j * opponentFieldWidth, opponentBoardY + i * opponentFieldHeight, opponentFieldWidth, opponentFieldHeight);
					g2.setFont(new Font("TimesRoman", Font.BOLD, 30 * opponentFieldWidth / Field.DEFAULT_WIDTH));
					int value = ((MultiplayerModel) model).opponentBoard[i][j].value;
					if (value > 0 && value <= model.innerSquareSize * model.innerSquareSize) {
						if (model.board[i][j].interactable) {
							g2.drawString("?", opponentBoardX + j * opponentFieldWidth + opponentFieldWidth / 2, opponentBoardY + i * opponentFieldHeight + opponentFieldHeight/2);
						}
						else {
							g2.drawString(value+"", opponentBoardX + j * opponentFieldWidth + opponentFieldWidth / 2, opponentBoardY + i * opponentFieldHeight + opponentFieldHeight/2);
						}
					}
				}
			}

			// Draw thick lines
			Stroke oldStroke = g2.getStroke();
			for (int i = 0; i < model.numInnerSquares; i++) {
				for (int j = 0; j < model.numInnerSquares; j++) {
					//g2.setColor(model.sudokuSolved(model.board, model.innerSquareSize) ? new Color(0, 200, 0) : black);
					g2.setColor(black);
					g2.setStroke(new BasicStroke(3));
					g2.drawRect(opponentBoardX + j * opponentFieldWidth * iss, opponentBoardY + i * opponentFieldHeight * iss, opponentFieldWidth*iss, opponentFieldHeight*iss);
				}
			}
			g2.setStroke(oldStroke);

			// Draw components
			//buttonPanel.repaint();
			//setVisible(true);
		}
	}

}
