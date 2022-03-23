package MVC;

import solvers.EfficientSolver;
import solvers.SudokuSolver;
import sudoku.Field;
import sudoku.Main;
import MVC.Model.Mode;
import multiplayer.MultiplayerView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View extends JPanel implements MouseListener, KeyListener, MouseWheelListener {
	
	private static final long serialVersionUID = 1L;
	public Model model;
	protected Controller controller;
	public int boardX = 0; //x coordinate for top left corner.
	public int boardY = 0; //y coordinate for top left corner.
	protected boolean dragging = false;
	protected int[] mouseBoardVector = new int[] {0, 0};
	public int fieldWidth = Field.DEFAULT_WIDTH;
	public int fieldHeight = Field.DEFAULT_HEIGHT;
	protected boolean close = false;
	protected JFrame frame;
	protected JPanel buttonPanel;
	protected JButton saveButton;
	protected JButton loadButton;
	protected JButton exitButton;
    private JButton hintButton;
    private JButton solveButton;
    private JButton stepSolveButton;
	public JTextField textField;
	protected JLabel timerLabel;
	protected boolean inFocus = true;
    private boolean infoButtonClicked = false;
    private int savedDifficulty;

    public int[] clickedPosition = new int[] {0, 0};
	
	public int currentSecond;
	public int currentMinute;
	public int currentHour;
    
    
    public View(Model model) {
    	this.model = model;
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setPreferredSize(new Dimension(screenSize.width-screenSize.width/10, screenSize.height-screenSize.height/10));
        boardX = getPreferredSize().width/2-model.getBoardSize()*fieldWidth/2;
        setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);
        // Create frame
        frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(getPreferredSize());
        frame.setLayout(null);
        
        buttonPanel = new JPanel(null);
        buttonPanel.setFocusable(true);
        buttonPanel.setBounds(getPreferredSize().width-200, -1, 200, getPreferredSize().height);
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addComponentsToButtonPanel(model.mode);
        
        frame.add(buttonPanel);
        frame.add(this);
        frame.pack();
        setFocusable(true);
        requestFocus();
        frame.setLocationByPlatform(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        addMouseListener(this);
        addKeyListener(this);
        addMouseWheelListener(this);
        (new Thread(new BoardDragger())).start();
        (new Thread(new TimerUpdater())).start();
    }
    
    public void setController(Controller controller) {
    	this.controller = controller;
    }
    
    protected void clearPaint(Graphics g) {
    	super.paint(g);
    }

    //Draws everything on screen.
    public void paint(Graphics g) {
    	setVisible(false);
    	if (!(this instanceof MultiplayerView)) {
            super.paint(g);
    	}

        Graphics2D g2 = (Graphics2D) g;

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
                if (clickedPosition[0] == i && clickedPosition[1] == j) {
                    g2.setColor(darkGray);
                } else if (clickedPosition[0] == i || clickedPosition[1] == j ||
                        (clickedPosition[0]/iss == i/iss && clickedPosition[1]/iss == j/iss)) {
                    g2.setColor(lightGray);
                }  else {
                    g2.setColor(white);
                }  if (model.board[i][j].value ==                                               //marks other fields
                        model.getBoard()[clickedPosition[0]][clickedPosition[1]].getValue()     //with same value
                        && model.getBoard()[i][j].getValue() != 0                               //as gray color
                        && !(clickedPosition[0] == i && clickedPosition[1] == j)) {
                    g2.setColor(gray);
                }
                g2.fillRect(boardX + j * fieldWidth, boardY + i * fieldHeight, fieldWidth, fieldHeight);
                g2.setColor(black);
                g2.drawRect(boardX + j * fieldWidth, boardY + i * fieldHeight, fieldWidth, fieldHeight);
                g2.setFont(new Font("TimesRoman", Font.BOLD, 30*fieldWidth/Field.DEFAULT_WIDTH));
                int value = model.board[i][j].value;
                if (value > 0 && value <= model.innerSquareSize*model.innerSquareSize) {
                    if(!Model.canBePlaced(model.board, model.innerSquareSize, i, j, value)) {
                        g2.setColor(red);
                        g2.drawString("" + value, boardX + j * fieldWidth + fieldWidth/2, boardY + i * fieldHeight + fieldHeight/2);
                    } else {
                        g2.drawString("" + value, boardX + j * fieldWidth + fieldWidth/2, boardY + i * fieldHeight + fieldHeight/2);
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
                g2.drawRect(boardX + j * fieldWidth * iss, boardY + i * fieldHeight * iss, fieldWidth*iss, fieldHeight*iss);
            }
        }
        g2.setStroke(oldStroke);

        // Draw components

        buttonPanel.repaint();
        setVisible(true);
        if (timerLabel != null) {
        	timerLabel.setText(currentHour + ":"+ (currentMinute < 10 ? "0" : "") + currentMinute + ":"+ (currentSecond < 10 ? "0" : "") + currentSecond);
        }
        if (inFocus) {
            requestFocus();
        }
        else {
        	textField.requestFocus();
        }
    }
    

    public void addComponentsToButtonPanel(Mode mode) {
        exitButton = new JButton("Exit");
        exitButton.setBounds(50, 450, 100, 25);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitToMenu();
            }
        });
        buttonPanel.add(exitButton);

        if (mode == Mode.play) {
            if (Main.DEBUG_MODE) {
                textField = new JTextField();
                textField.setBounds(50, 550, 100, 25);
                textField.addMouseListener(new MouseListener() {
            		public void mousePressed(MouseEvent e) {}
            		public void mouseReleased(MouseEvent e) {}
            		public void mouseEntered(MouseEvent e) {}
            		public void mouseExited(MouseEvent e) {}
            		public void mouseClicked(MouseEvent e) {
            			inFocus = false;
            		}});
            	loadButton = new JButton("Load");
                loadButton.setBounds(50, 580, 100, 25);
                loadButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.loadAndUpdate(textField.getText());
                    }
                });
            }
            timerLabel = new JLabel("0");
            timerLabel.setFont(new Font("Serif", Font.BOLD, 20));
            timerLabel.setBounds(70, 0, 200, 150);
            buttonPanel.add(timerLabel);
            if (Main.DEBUG_MODE) {
                buttonPanel.add(textField);
                buttonPanel.add(loadButton);
            }
        } else if (mode == Mode.create) {
            saveButton = new JButton("Save");
            saveButton.setBounds(50, 250, 100, 25);
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showSavePopup();
                }
            });
            buttonPanel.add(saveButton);

        } else if (mode == Mode.solver) {
            solveButton = new JButton("Solve");
            solveButton.setBounds(50, 200, 100, 25);
            solveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.solve();
                }
            });
            stepSolveButton = new JButton("Step-solve");
            stepSolveButton.setBounds(50, 250, 100, 25);
            stepSolveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.giveHint();
                    // TODO: model.solveStep();
                }
            });
            buttonPanel.add(solveButton);
            buttonPanel.add(stepSolveButton);
        }

        if (model.assistMode) {
            hintButton = new JButton("Hint");
            hintButton.setBounds(50, 200, 100, 25);
            hintButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.giveHint();
                }
            });
            buttonPanel.add(hintButton);
        }
    }

    public void showSavePopup() {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(350, 100));

        savedDifficulty = -1;

        JLabel boardSizeLabel = new JLabel("Size: " + model.getBoardSize() + "x" + model.getBoardSize());
        JLabel solutionsLabel = new JLabel("Solutions: Unknown");
        JLabel difficultyLabel = new JLabel("Difficulty: Unknown");
        JButton infoButton = new JButton("Show additional information");

        boardSizeLabel.setFont(new Font("Serif", Font.BOLD, 15));
        solutionsLabel.setFont(new Font("Serif", Font.BOLD, 15));
        difficultyLabel.setFont(new Font("Serif", Font.BOLD, 15));

        boardSizeLabel.setBounds(100, 10, 200, 20);
        solutionsLabel.setBounds(100, 40, 200, 20);
        difficultyLabel.setBounds(100, 70, 200, 20);
        infoButton.setBounds(25, 40, 250, 30);
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoButtonClicked = true;
                EfficientSolver es = new EfficientSolver(model.board, model.innerSquareSize);
                int solutions = es.solve(6).size();
                savedDifficulty = es.difficulty;
                String difficultyString = SudokuSolver.getDifficultyString(savedDifficulty);

                solutionsLabel.setText("Solutions: " + (solutions > 5 ? ">5" : solutions));
                difficultyLabel.setText("Difficulty: " + savedDifficulty + " (" + difficultyString + ")");
                panel.add(boardSizeLabel);
                panel.add(solutionsLabel);
                panel.add(difficultyLabel);
                panel.remove(infoButton);
                panel.repaint();
            }
        });
        panel.add(infoButton);
        String a = (String)JOptionPane.showInputDialog(frame, panel, "Save", JOptionPane.QUESTION_MESSAGE, null, null, model.fileName);
        if (a != null) {
            if (infoButtonClicked) {
                model.save(a, savedDifficulty);
            } else {
                model.save(a);
            }
        }
    }
    
    public void quitToMenu() {
    	quitToMenu("");
    }
    
    public void quitToMenu(String message) {
    	close = true;
        frame.dispose();
        Main.restart(message);
    }
    
    public void resetBoardPosition() {
		fieldWidth = Field.DEFAULT_WIDTH;
		fieldHeight = Field.DEFAULT_HEIGHT;
		boardX = getPreferredSize().width/2-model.getBoardSize()*fieldWidth/2;
		boardY = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    	inFocus = true;
    	if (e.getButton() == MouseEvent.BUTTON1 && e.getX() >= boardX && e.getY() >= boardY && e.getX() <= boardX+model.getBoardSize()*fieldWidth && e.getY() <= boardY+model.getBoardSize()*fieldHeight) {
            clickedPosition = new int[]{(e.getY()-boardY)/fieldHeight, (e.getX()-boardX)/fieldWidth};
            repaint();
    	}
    }

    @Override
    public void mousePressed(MouseEvent e) {
    	inFocus = true;
    	Point mousePos = getMousePosition();
    	if (mousePos != null) {
        	mouseBoardVector = new int[] {boardX-mousePos.x, boardY-mousePos.y};
        	dragging = true;
    	}
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
		if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
			quitToMenu();
		}
		if (e.getKeyChar() == KeyEvent.VK_SPACE) {
			resetBoardPosition();
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
	
	private class TimerUpdater implements Runnable {
		@Override
		public void run() {
			int currentTime = 0;
			while (!close) {
				if (currentTime != model.elapsedTime()) {
					currentTime = model.elapsedTime();
					currentSecond = model.elapsedTime() % 60;
					currentMinute = model.elapsedTime() / 60 % 60;
					currentHour = model.elapsedTime() / 3600;
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
	//Concurrent thread that moves the board when dragging.
	protected class BoardDragger implements Runnable {
		@Override
		public void run() {
			while (!close) {
				if (dragging) {
					Point mousePos = getMousePosition();
					if (mousePos != null) {
						boardX = mousePos.x+mouseBoardVector[0];
						boardY = mousePos.y+mouseBoardVector[1];
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
	
	public static void winPopup() {
		final JFrame winPop = new JFrame();

		int hour = Model.elapsedTime() / 3600;
		int minute = Model.elapsedTime() / 60 % 60;
		int second = Model.elapsedTime() % 60;
		JOptionPane.showMessageDialog(winPop, "Sudoku solved! your time was: " + hour + ":" + minute + ":" + second);
		//return to menu, save time
		
	}
	
}
