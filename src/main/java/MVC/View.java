package MVC;

import Generators.SudokuGenerator;
import solvers.EfficientSolver;
import solvers.SudokuSolver;
import sudoku.Field;
import sudoku.Main;
import MVC.Model.Mode;
import multiplayer.MultiplayerView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/*
    The View of our MVC-module defines the UI display.
    This class gets data from the MVC.Controller and
    uses this data change the UI for the user depending
    on that data.
*/

public class View extends JPanel implements MouseListener, KeyListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	public Model model;
	protected Controller controller;
	public double boardX, boardY = 0.0; //x- and y coordinate for top left corner.
	protected boolean dragging = false;
	protected int[] mouseBoardVector = new int[] {0, 0};
	public int windowWidth, windowHeight;
	public double fieldWidth = Field.DEFAULT_WIDTH;
	public double fieldHeight = Field.DEFAULT_HEIGHT;
	protected boolean close = false;
	protected JFrame frame;
	protected JPanel buttonPanel;
	protected JButton saveButton, loadButton, notesButton, exitButton;
    private JButton cancelButton;
    private JLabel hintText;
	public JTextField textField;
	protected JLabel timerLabel;
	protected boolean inFocus = true;
    public boolean notesOn = false;
    private boolean infoButtonClicked = false;
    private int savedDifficulty;
    private int buttonYReference = Main.SCREEN_HEIGHT/3;
    private Font buttonFont = new Font("Arial", Font.BOLD, Main.SCREEN_HEIGHT / 50);
    private Color buttonColor = new Color(180, 180, 180);
    private Color hoverButtonColor = new Color(120, 120, 120);

    private boolean helpInfoShown = false;
    public int[] clickedPosition = new int[] {0, 0};
    public ArrayList<int[]> marked1;
    public ArrayList<int[]> marked2;
    public String hintName;

	public int currentSecond;
	public int currentMinute;
	public int currentHour;

    //Constructor taking a references to the model.
    public View(Model model) {
    	this.model = model;
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	windowWidth = screenSize.width - screenSize.width / 10;
    	windowHeight = screenSize.height - screenSize.height / 10;
    	setPreferredSize(new Dimension(windowWidth, windowHeight));
        boardX = getPreferredSize().width / 2 - model.getBoardSize() * fieldWidth / 2;
        setBounds(0, 0, getPreferredSize().width, getPreferredSize().height);

        // Create frame
        frame = new JFrame("Sudoku");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(getPreferredSize());
        frame.setLayout(null);

        //frame.add(label, BorderLayout.CENTER);

        // Create button panel
        buttonPanel = new JPanel(null);
        buttonPanel.setFocusable(true);
        buttonPanel.setBounds(getPreferredSize().width - getPreferredSize().width/5, - 1, getPreferredSize().width/5, getPreferredSize().height);
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        addComponentsToButtonPanel(model.mode);

        this.setLayout(null);
        addCancelButtonToView();

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

        if (!model.generatingSudokuDone) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Courier", Font.BOLD, 100));
            String text = "Generating Sudoku";
            int fontHeight = g2.getFontMetrics().getHeight();
            int fontWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, windowWidth / 2 - fontWidth / 2, windowHeight / 2 - fontHeight / 2);
            buttonPanel.setVisible(false);
            cancelButton.setVisible(true);
            setVisible(true);
        } else {
            // Draw fields and numbers
            Color red = new Color(175, 4, 4);
            Color black = Color.BLACK;
            Color white = Color.WHITE;
            Color yellow = Color.YELLOW;
            Color darkYellow = new Color(120, 120, 0);
            Color green = Color.GREEN;
            Color darkGreen = new Color(0, 120, 0);
            Color lightGray = new Color(200, 200, 200);
            Color gray = new Color(130, 130, 130);
            Color darkGray = new Color(85, 85, 85);
            int iss = model.innerSquareSize;
            for (int i = 0; i < model.getBoardSize(); i++) {
                for (int j = 0; j < model.getBoardSize(); j++) {
                    if (clickedPosition[0] == i && clickedPosition[1] == j) {
                        g2.setColor(darkGray);
                    } else if (clickedPosition[0] == i || clickedPosition[1] == j ||
                            (clickedPosition[0] / iss == i / iss && clickedPosition[1] / iss == j / iss)) {
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
                    if (marked2 != null && containsPosition(marked2, i, j) && clickedPosition[0] == i && clickedPosition[1] == j) {
                        g2.setColor(darkGreen);
                    } else if (marked2 != null && containsPosition(marked2, i, j)) {
                        g2.setColor(green);
                    } else if (marked1 != null && containsPosition(marked1, i, j) && clickedPosition[0] == i && clickedPosition[1] == j) {
                        g2.setColor(darkYellow);
                    } else if (marked1 != null && containsPosition(marked1, i, j)) {
                        g2.setColor(yellow);
                    }
                    g2.fillRect((int) (boardX + j * (int) fieldWidth), (int) (boardY + i * (int) fieldHeight), (int) fieldWidth, (int) fieldHeight);
                    g2.setColor(black);
                    g2.drawRect((int) (boardX + j * (int) fieldWidth), (int) (boardY + i * (int) fieldHeight), (int) fieldWidth, (int) fieldHeight);

                    int value = model.board[i][j].value;
                    if (value > 0 && value <= model.innerSquareSize * model.innerSquareSize) {
                        int valueDigits = String.valueOf(value).length();
                        int scaling = (valueDigits + 1) * 3;
                        g2.setFont(new Font("Courier", Font.BOLD, (int) ((40 - scaling) * (int) fieldWidth / (int) Field.DEFAULT_WIDTH)));
                        String text = "" + value;
                        int fontHeight = g2.getFontMetrics().getHeight();
                        int fontWidth = g2.getFontMetrics().stringWidth(text);
                        if (!Model.canBePlaced(model.board, model.innerSquareSize, i, j, value)) {
                            g2.setColor(red);
                            g2.drawString(text, (int) (boardX + j * (int) fieldWidth + (int) fieldWidth / 2 - fontWidth / 2), (int) (boardY + i * (int) fieldHeight + (int) fieldHeight / 2 + fontHeight / 3));
                        } else {
                            g2.drawString(text, (int) (boardX + j * (int) fieldWidth + (int) fieldWidth / 2 - fontWidth / 2), (int) (boardY + i * (int) fieldHeight + (int) fieldHeight / 2 + fontHeight / 3));
                        }
                    } else if (value == 0) {
                        g2.setFont(new Font("Courier", Font.BOLD, (int) (15 * (int) fieldWidth / (int) Field.DEFAULT_WIDTH)));
                        for (int k = 0; k < 9; k++) {
                            int note = model.board[i][j].notes[k];
                            String text = note == 0 ? "" : "" + note;
                            int fontHeight = g2.getFontMetrics().getHeight();
                            int fontWidth = g2.getFontMetrics().stringWidth(text);
                            g2.drawString(text, (int) (boardX + j * (int) fieldWidth + (int) fieldWidth / 3 * ((note - 1) % 3) + fontWidth), (int) (boardY + i * (int) fieldHeight - fontHeight / 3 + (int) fieldHeight / 3 * ((note - 1) / 3 + 1)));
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
                    g2.setStroke(new BasicStroke((int) (2 + fieldWidth / 30)));
                    g2.drawRect((int) (boardX + j * (int) fieldWidth * iss), (int) (boardY + i * (int) fieldHeight * iss), (int) fieldWidth * iss, (int) fieldHeight * iss);
                }
            }
            g2.setStroke(oldStroke);
            // Draw components
            buttonPanel.repaint();
            buttonPanel.setVisible(true);
            cancelButton.setVisible(false);

            // Draw timer
            if (timerLabel != null && !model.isSolved()) {
                timerLabel.setText(currentHour + ":" + (currentMinute < 10 ? "0" : "") + currentMinute + ":" + (currentSecond < 10 ? "0" : "") + currentSecond);
            }
            // Set focus
            setVisible(true);
            if (inFocus) {
                requestFocus();
            } else {
                textField.requestFocus();
            }

        }
    }
    
    private boolean containsPosition(ArrayList<int[]> list, int x, int y) {
    	for (int[] array : list) {
    		if (array[0] == x && array[1] == y) {
    			return true;
    		}
    	}
    	return false;
    }

    //Add cancel button to view (visible when generating sudoku)
    public void addCancelButtonToView() {
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(windowWidth / 2 - windowHeight / 6, windowHeight * 3 / 4,windowHeight / 3,windowHeight / 12);
        styleButton(cancelButton);
        cancelButton.setFont(new Font("Arial", Font.BOLD, windowHeight / 40));
        cancelButton.addActionListener(   new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitToMenu();
                model.cancelGenerator();
            }
        });
        add(cancelButton);
    }
    
    //Add all components to the button panel (buttons etc.).
    public void addComponentsToButtonPanel(Mode mode) {
        JButton helpButton = new JButton("Help");
        helpButton.setBounds(buttonPanel.getWidth()/4, buttonYReference,buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
        styleButton(helpButton);
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HelpPopup();
            }
        });
        buttonPanel.add(helpButton);
        notesButton = new JButton("Notes off");
        notesButton.setBounds(buttonPanel.getWidth()/4, buttonYReference + buttonPanel.getWidth()/5, buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
        styleButton(notesButton);
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
        exitButton.setBounds(buttonPanel.getWidth()/4, buttonYReference + (buttonPanel.getWidth()/5) * 2, buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
        styleButton(exitButton);
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
                textField.setBounds(buttonPanel.getWidth()/4, buttonYReference + (buttonPanel.getWidth()/5) * 3, buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
                textField.addMouseListener(new MouseListener() {
            		public void mousePressed(MouseEvent e) {}
            		public void mouseReleased(MouseEvent e) {}
            		public void mouseEntered(MouseEvent e) {}
            		public void mouseExited(MouseEvent e) {}
            		public void mouseClicked(MouseEvent e) {
            			inFocus = false;
            		}});
            	loadButton = new JButton("Load");
                loadButton.setBounds(buttonPanel.getWidth()/4, buttonYReference + (buttonPanel.getWidth()/5) * 4, buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
                styleButton(loadButton);
                loadButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.loadAndUpdate(textField.getText());
                    }
                });
            }
            timerLabel = new JLabel("0");
            timerLabel.setFont(new Font("Serif", Font.BOLD, buttonPanel.getWidth()/10));
            timerLabel.setBounds((int)(buttonPanel.getWidth() * 0.35), 0, buttonPanel.getWidth(), buttonPanel.getHeight()/5);
            buttonPanel.add(timerLabel);
            if (Main.DEBUG_MODE) {
                buttonPanel.add(textField);
                buttonPanel.add(loadButton);
            }
        } else if (mode == Mode.create) {
            saveButton = new JButton("Save");
            saveButton.setBounds(buttonPanel.getWidth()/4, buttonYReference - (buttonPanel.getWidth()/5), buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
            styleButton(saveButton);
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showSavePopup();
                }
            });
            buttonPanel.add(saveButton);

        } else if (mode == Mode.solver) {
            JButton solveButton = new JButton("Solve");
            solveButton.setBounds(buttonPanel.getWidth()/4, buttonYReference - (buttonPanel.getWidth()/5) * 2 , buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
            styleButton(solveButton);
            solveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.solve();
                }
            });
            JButton stepSolveButton = new JButton("Step-solve");
            stepSolveButton.setBounds(buttonPanel.getWidth()/4, buttonYReference - (buttonPanel.getWidth()/5), buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
            styleButton(stepSolveButton);
            stepSolveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.stepSolve();
                }
            });
            buttonPanel.add(solveButton);
            buttonPanel.add(stepSolveButton);
        }

        if (model.assistMode) {
            hintText = new JLabel();
            hintText.setFont(new Font("Serif", Font.BOLD, buttonPanel.getWidth()/15));
            hintText.setFocusable(false);
            hintText.setBounds(buttonPanel.getWidth()/5, buttonYReference - (buttonPanel.getWidth()/4), buttonPanel.getWidth(),buttonPanel.getWidth()/9);
            buttonPanel.add(hintText);

            JButton hintButton = new JButton("Hint");
            hintButton.setBounds(buttonPanel.getWidth()/4, buttonYReference - (buttonPanel.getWidth()/5) * 2, buttonPanel.getWidth()/2,buttonPanel.getWidth()/9);
            styleButton(hintButton);
            hintButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    model.giveHint();
                    hintText.setText("Use " + hintName);
                }
            });
            buttonPanel.add(hintButton);
        }
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(buttonColor);
        button.setForeground(Color.BLACK);
        button.setFont(buttonFont);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverButtonColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(buttonColor);
            }
        });
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
    	fieldWidth = Field.DEFAULT_WIDTH * windowWidth / 1170.0;
    	fieldHeight = fieldWidth;
		boardX = getPreferredSize().width / 2 - model.getBoardSize() * fieldWidth / 2 - buttonPanel.getWidth() / 2;
		boardY = 0;
    }

    //Handles mouse clicks from the user.
    @Override
    public void mouseClicked(MouseEvent e) {
    	inFocus = true;
    	if (e.getButton() == MouseEvent.BUTTON1 && e.getX() > boardX && e.getY() > boardY && e.getX() < boardX+model.getBoardSize()*fieldWidth && e.getY() < boardY+model.getBoardSize()*fieldHeight) {
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
	}

	//Turns notes on when pressing shift.
	@Override
	public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            quitToMenu();
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            resetBoardPosition();
            repaint();
        }
        else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            notesButton.setText("Notes on");
            notesOn = true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP) {
            if (clickedPosition[0] > 0) {
                clickedPosition[0]--;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN) {
            if (clickedPosition[0] < model.getBoardSize() - 1) {
                clickedPosition[0]++;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT) {
            if (clickedPosition[1] > 0) {
                clickedPosition[1]--;
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT) {
            if (clickedPosition[1] < model.getBoardSize() - 1) {
                clickedPosition[1]++;
            }
        }
        else {
            controller.keyTyped(e, clickedPosition); //Let controller handle the key typed.
        }
	}

	//Turns notes off when releasing shift.
	@Override
	public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            notesButton.setText("Notes off");
            notesOn = false;
        }
	}

	//Zooms in/out when scrolling.
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getPreciseWheelRotation() < 0.0 || (fieldWidth > 20.0 && fieldHeight > 20.0)) {
			double fieldWidthDist = (e.getX()-boardX)/fieldWidth;
			double fieldHeightDist = (e.getY()-boardY)/fieldHeight;
			double w = e.getPreciseWheelRotation()*fieldWidth/25.0;
			double h = e.getPreciseWheelRotation()*fieldHeight/25.0;
			fieldWidth -= w == 0.0 ? e.getPreciseWheelRotation() : w;
			fieldHeight -= h == 0.0 ? e.getPreciseWheelRotation() : h;
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
	private class BoardDragger implements Runnable {
		@Override
		public void run() {
			while (!close) {
				if (dragging) {
					Point mousePos = getMousePosition();
					if (mousePos != null) {
						boardX = mousePos.x + mouseBoardVector[0];
						boardY = mousePos.y + mouseBoardVector[1];
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
		
		model.saveStat(model.elapsedTime(),difficulty);
		int hour = time / 3600;
		int minute = time / 60 % 60;
		int second = time % 60;
		JOptionPane.showMessageDialog(frame, "Sudoku solved! your time was: " + hour + ":" + minute + ":" + second);
		//return to menu, save time
	}

    public void unsolvablePopup() {
        JOptionPane.showMessageDialog(frame, "The current sudoku is unsolvable.");
    }

    //Inserts a button that has an
    //explanation of every command.
    public void HelpPopup(){
        if (Main.DEBUG_MODE){
            JOptionPane.showMessageDialog(frame,
                    "Enter a digit in the empty fields" +
                            "\nHold 'shift' and press a number from 1-9 to set or remove notes." +
                            "\nUse arrow keys to navigate." +
                            "\n'backspace' deletes the chosen field if interactable." +
                            "\nUse your mouse to either scroll or drag the board." +
                            "\n's' solves the sudoku." +
                            "\n'enter' saves sudoku to a specific file." +
                            "\n'l' loads that specific file." +
                            "\n'n' generates a new sudoku with the same difficulty." +
                            "\n'm' pops up your score as if solved." +
                            "\n't' solves different sudokus and prints the time." +
                            "\n'y' generates different sudokus and prints the time." +
                            "\n'h' gives a hint if assist mode is active.",
                    "Help",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame,
                    "Enter a digit in the empty fields" +
                            "\nHold 'shift' and press a number from 1-9 to set or remove notes." +
                            "\nUse arrow keys to navigate." +
                            "\n'backspace' deletes the chosen field if interactable." +
                            "\nUse your mouse to either scroll or drag the board.",
                    "Help",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
