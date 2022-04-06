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
	public double boardX = 0.0; //x coordinate for top left corner.
	public double boardY = 0.0; //y coordinate for top left corner.
	protected boolean dragging = false;
	protected int[] mouseBoardVector = new int[] {0, 0};
	public int windowWidth;
	public int windowHeight;
	public double fieldWidth = Field.DEFAULT_WIDTH;
	public double fieldHeight = Field.DEFAULT_HEIGHT;
	protected boolean close = false;
	protected JFrame frame;
	protected JPanel buttonPanel;
	protected JButton saveButton;
	protected JButton loadButton;
    protected JButton notesButton;
	protected JButton exitButton;
    private JButton hintButton;
    private JButton solveButton;
    private JButton stepSolveButton;
	public JTextField textField;
	protected JLabel timerLabel;
	protected boolean inFocus = true;
    public boolean notesOn = false;
    private boolean infoButtonClicked = false;
    private int savedDifficulty;

    public int[] clickedPosition = new int[] {0, 0};
	
	public int currentSecond;
	public int currentMinute;
	public int currentHour;
    
    //Constructor taking a references to the model.
    public View(Model model) {
    	this.model = model;
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	windowWidth = screenSize.width-screenSize.width/10;
    	windowHeight = screenSize.height-screenSize.height/10;
    	setPreferredSize(new Dimension(windowWidth, windowHeight));
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
    
    //Sets the reference to the controller.
    public void setController(Controller controller) {
    	this.controller = controller;
    }
    
    //Clears everything drawn on the screen.
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
                } else {
                    g2.setColor(white);
                }
                if (model.board[i][j].value ==                                               //marks other fields
                        model.getBoard()[clickedPosition[0]][clickedPosition[1]].getValue()     //with same value
                        && model.getBoard()[i][j].getValue() != 0                               //as gray color
                        && !(clickedPosition[0] == i && clickedPosition[1] == j)) {
                    g2.setColor(gray);
                }
                g2.fillRect((int)(boardX + j * (int)fieldWidth), (int)(boardY + i * (int)fieldHeight), (int)fieldWidth, (int)fieldHeight);
                g2.setColor(black);
                g2.drawRect((int)(boardX + j * (int)fieldWidth), (int)(boardY + i * (int)fieldHeight), (int)fieldWidth, (int)fieldHeight);

                int value = model.board[i][j].value;
                if (value > 0 && value <= model.innerSquareSize*model.innerSquareSize) {
                    int valueDigits = String.valueOf(value).length();
                    int scaling = (valueDigits + 1) * 3;
                    g2.setFont(new Font("Courier", Font.BOLD, (int)((40 - scaling) * (int)fieldWidth / (int)Field.DEFAULT_WIDTH)));
                    String text = "" + value;
                    int fontHeight = g2.getFontMetrics().getHeight();
                    int fontWidth = g2.getFontMetrics().stringWidth(text);
                    if(!Model.canBePlaced(model.board, model.innerSquareSize, i, j, value)) {
                        g2.setColor(red);
                        g2.drawString(text, (int)(boardX + j * (int)fieldWidth + (int)fieldWidth/2 - fontWidth/2), (int)(boardY + i * (int)fieldHeight + (int)fieldHeight/2 + fontHeight/3));
                    } else {
                        g2.drawString(text, (int)(boardX + j * (int)fieldWidth + (int)fieldWidth/2 - fontWidth/2), (int)(boardY + i * (int)fieldHeight + (int)fieldHeight/2 + fontHeight/3));
                    }
                } else if (value == 0) {
                    g2.setFont(new Font("Courier", Font.BOLD, (int)(15 * (int)fieldWidth / (int)Field.DEFAULT_WIDTH)));
                    for (int k = 0; k < 9; k++) {
                        int note = model.board[i][j].notes[k];
                        String text = note == 0 ? "" : "" + note;
                        int fontHeight = g2.getFontMetrics().getHeight();
                        int fontWidth = g2.getFontMetrics().stringWidth(text);
                        g2.drawString(text, (int)(boardX + j * (int)fieldWidth + (int)fieldWidth/3 * ((note-1) % 3) + fontWidth), (int)(boardY + i * (int)fieldHeight - fontHeight/3 + (int)fieldHeight/3 * ((note-1)/3 + 1)));
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
                g2.drawRect((int)(boardX + j * (int)fieldWidth * iss), (int)(boardY + i * (int)fieldHeight * iss), (int)fieldWidth*iss, (int)fieldHeight*iss);
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
    
    //Add all components to the button panel (buttons etc.).
    public void addComponentsToButtonPanel(Mode mode) {
        notesButton = new JButton("Notes off");
        notesButton.setBounds(50, 400, 100, 25);
        notesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (notesOn) {
                    notesButton.setText("Notes off");
                    notesOn = false;
                } else {
                    notesButton.setText("Notes on");
                    notesOn = true;
                }
            }
        });
        buttonPanel.add(notesButton);
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

    //Shows the popup menu for saving a sudoku.
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
        	//Handles clicking the "Show additional information" button.
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
    
    //Quits to the menu without showing any message.
    public void quitToMenu() {
    	quitToMenu("");
    }
    
    //Quits to the menu and shows a popup message on the screen.
    public void quitToMenu(String message) {
    	close = true;
        frame.dispose();
        Main.restart(message);
    }
    
    //Resets the position and size of the sudoku to the default position and size.
    public void resetBoardPosition() {
    	fieldWidth = Field.DEFAULT_WIDTH*windowWidth/1170.0;
    	fieldHeight = fieldWidth;
		boardX = getPreferredSize().width/2-model.getBoardSize()*fieldWidth/2;
		boardY = 0;
    }

    //Handles mouse clicks from the user.
    @Override
    public void mouseClicked(MouseEvent e) {
    	inFocus = true;
    	if (e.getButton() == MouseEvent.BUTTON1 && e.getX() >= boardX && e.getY() >= boardY && e.getX() <= boardX+model.getBoardSize()*fieldWidth && e.getY() <= boardY+model.getBoardSize()*fieldHeight) {
            clickedPosition = new int[]{(int)((e.getY()-boardY)/fieldHeight), (int)((e.getX()-boardX)/fieldWidth)};
            repaint();
    	}
    }

    //Handles mouse presses from the user (for dragging).
    @Override
    public void mousePressed(MouseEvent e) {
    	inFocus = true;
    	Point mousePos = getMousePosition();
    	if (mousePos != null) {
        	mouseBoardVector = new int[] {(int)(boardX-mousePos.x), (int)(boardY-mousePos.y)};
        	dragging = true;
    	}
    }

    //Stops the dragging when user releases mouse.
    @Override
    public void mouseReleased(MouseEvent e) {
    	dragging = false;
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        //Does nothing, but required by implemented interface.
    }

    //Stops dragging when user moves mouse out of the screen.
    @Override
    public void mouseExited(MouseEvent e) {
    	dragging = false;
    }

    //Handles a key typed by the user.
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
			controller.keyTyped(e, clickedPosition); //Let controller handle the key typed.
		}
	}

	//Turns notes on when pressing alt.
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
            notesButton.setText("Notes on");
            notesOn = true;
        }
	}

	//Turns notes off when releasing alt.
	@Override
	public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ALT) {
            notesButton.setText("Notes off");
            notesOn = false;
        }
	}

	//Zooms in/out when scrolling.
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0 || (fieldWidth > 20 && fieldHeight > 20)) {
			System.out.println(boardX+" "+boardY+" "+fieldWidth+" "+fieldHeight);
			double fieldWidthDist = (e.getX()-boardX)/(double)fieldWidth;
			double fieldHeightDist = (e.getY()-boardY)/(double)fieldHeight;
			double w = e.getWheelRotation()*fieldWidth/20;
			double h = e.getWheelRotation()*fieldHeight/20;
			fieldWidth -= w == 0.0 ? e.getWheelRotation() : w;
			fieldHeight -= h == 0.0 ? e.getWheelRotation() : h;
			boardX -= (fieldWidthDist-(e.getX()-boardX)/fieldWidth)*fieldWidth;
			boardY -= (fieldHeightDist-(e.getY()-boardY)/fieldHeight)*fieldHeight;
			repaint();
		}
	}
	
	//Used to update the timer.
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
	
	//Shows the win popup when sudoku is solved.
	public void winPopup(int difficulty) {
		String diff = SudokuSolver.getDifficultyString(difficulty);	
		switch (diff) {
		case "Easy":
			difficulty = 0;
			break;
		case "Medium":
			difficulty = 1;
			break;
		case "Hard":
			difficulty = 2;
			break;
		case "Extreme":
			difficulty = 3;
			break;
		}	
		
		int time = model.elapsedTime();
		
		Model.saveStat(model.elapsedTime(),difficulty);
		int hour = time / 3600;
		int minute = time / 60 % 60;
		int second = time % 60;
		JOptionPane.showMessageDialog(frame, "Sudoku solved! your time was: " + hour + ":" + minute + ":" + second);
		//return to menu, save time
		
	}
	
}
