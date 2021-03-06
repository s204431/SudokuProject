package multiplayer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.Space;
import org.jspace.SpaceRepository;

import MVC.Model;
import sudoku.Field;

/*
	The MultiplayerModel extends the Model and contains mostly the same functionality,
	but with added functionality for multiplayer.

	Responsible: Jens
*/


public class MultiplayerModel extends Model implements Runnable {
	
	public boolean isServer,
			       started  = false;
	private boolean close = false;
	private SpaceRepository repository;
	public Space toOpponent,
				 fromOpponent;
	public Field[][] opponentBoard;
	public int winner = 0; // 0 = no winner, 1 = you won, 2 = opponent won
	
	//Constructor for host of the game.
	public MultiplayerModel(int numInnerSquares, int innerSquareSize) {
		super(numInnerSquares, innerSquareSize, Mode.multiplayer);
		isServer = true;
		startServer();
	}
	
	//Constructor for client (not host).
	public MultiplayerModel(int numInnerSquares, int innerSquareSize, String address) throws IOException {
		super(numInnerSquares, innerSquareSize, Mode.multiplayer);
		isServer = false;
		connectToServer(address);
	}
	
	//Starts the game. Used by both host and client. It runs in a parallel thread to allow blocking the thread.
	public void start() {
		new Thread(new DisconnectedReader()).start();

		if (isServer) {
			//Wait for other player to join.
			try {
				Object[] tuple = fromOpponent.get(new ActualField("joined"), new FormalField(Integer.class), new FormalField(Integer.class));
				toOpponent.put("ok", view.windowWidth, view.windowHeight);
				((MultiplayerView) view).opponentWindowWidth = (int) tuple[1];
				((MultiplayerView) view).opponentWindowHeight = (int) tuple[2];
				if (close) {
					return;
				}
				System.out.println("Opponent joined!");
				(new Thread(new JoinedReader())).start();
				toOpponent.put(board, numInnerSquares, innerSquareSize);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			//Inform host that this client has joined.
			try {
				toOpponent.put("joined", view.windowWidth, view.windowHeight);
				//Wait For response from server.
				Object[] response = fromOpponent.get(new FormalField(String.class), new FormalField(Integer.class), new FormalField(Integer.class));
				if (response[0].equals("full")) { //Server is full. Disconnect.
					System.out.println("Server full!");
					view.quitToMenu("Server is full");
					return;
				}
				((MultiplayerView) view).opponentWindowWidth = (int) response[1];
				((MultiplayerView) view).opponentWindowHeight = (int) response[2];
				//Joined successfully.
				System.out.println("Successfully joined server!");
				Object[] tuple = fromOpponent.get(new FormalField(Field[][].class), new FormalField(Integer.class), new FormalField(Integer.class));
				board = (Field[][]) tuple[0];
				numInnerSquares = (Integer) tuple[1];
				innerSquareSize = (Integer) tuple[2];
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		opponentBoard = new Field[getBoardSize()][getBoardSize()];
		for (int i = 0; i < getBoardSize(); i++) {
			for (int j = 0; j < getBoardSize(); j++) {
				opponentBoard[i][j] = new Field(board[i][j].value, board[i][j].interactable);
			}
		}
		new Thread(new UpdateReader()).start();
		((MultiplayerView)view).addOpponentBoard();
		started = true;
		view.resetBoardPosition();
		view.repaint();
	}
	
	//Starts the server (host only).
	private void startServer() {
		repository = new SpaceRepository();
		repository.addGate("tcp://"+getIP()+":9001/?keep");
		toOpponent = new SequentialSpace();
		fromOpponent = new SequentialSpace();
		repository.add("hosttoclient", toOpponent);
		repository.add("clienttohost", fromOpponent);
	}
	
	//Connects to the server (not host).
	private void connectToServer(String address) throws IOException {
		toOpponent = new RemoteSpace("tcp://" + address + ":9001/clienttohost?keep");
		fromOpponent = new RemoteSpace("tcp://" + address + ":9001/hosttoclient?keep");
	}
	
	//Disconnects from the server.
	public void disconnect() {
		close = true;
		try {
			fromOpponent.put("joined", 0, 0);
			fromOpponent.put("disconnected");
			fromOpponent.put(0, 0, 0);
			toOpponent.put("disconnected");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!isServer) {
			/*try {
				//((RemoteSpace) toOpponent).close();
				//((RemoteSpace) fromOpponent).close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		else {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repository.closeGates();
			repository.shutDown();
		}
	}
	
	//Modified method from Model. Additionally send an update to the other player.
	public void setField(int x, int y, Field field) {
		super.setField(x, y, field);
		if (Model.sudokuSolved(board, innerSquareSize) && winner != 2) {
			winner = 1;
		}
		if (!view.notesOn) {
			try {
				toOpponent.put(x, y, field.value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Overloaded setField that takes a value instead of a Field.
	public void setField(int x, int y, int value) {
		super.setField(x, y, value);
		if (Model.sudokuSolved(board, innerSquareSize) && winner != 2) {
			winner = 1;
		}
		if (!view.notesOn) {
			try {
				toOpponent.put(x, y, value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Sets the join IP-address option to the users own IP, otherwise localhost.
	public static String getIP(){
		String address = "localhost";
		try {
			address = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return address;
	}
	
	//Checks for updated fields from opponent.
	private class UpdateReader implements Runnable {
		public void run() {
			while (!close) {
				try {
					Object[] tuple = fromOpponent.get(new FormalField(Integer.class), new FormalField(Integer.class), new FormalField(Integer.class)); //(x, y, value)
					opponentBoard[(int)tuple[0]][(int)tuple[1]].value = (int)tuple[2];
					if (Model.sudokuSolved(opponentBoard, innerSquareSize) && winner != 1) {
						winner = 2;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}
	}
	
	//Checks for new players joining when server is full.
	private class JoinedReader implements Runnable {
		public void run() {
			while (!close) {
				try {
					fromOpponent.get(new ActualField("joined"), new FormalField(Integer.class), new FormalField(Integer.class));
					toOpponent.put("full", 0, 0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//Checks if the other player has disconnected.
	private class DisconnectedReader implements Runnable {
		public void run() {
			try {
				fromOpponent.get(new ActualField("disconnected"));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!close) {
				view.quitToMenu("Opponent disconnected");	
			}
		}
	}

	public void run() {
		start();
	}
	
	
}
