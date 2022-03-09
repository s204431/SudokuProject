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
import MVC.Model.Mode;
import sudoku.Field;
import sudoku.Main;

public class MultiplayerModel extends Model implements Runnable {
	
	public boolean isServer = false;
	public boolean started = false;
	private boolean close = false;
	private SpaceRepository repository;
	public Space toOpponent;
	public Space fromOpponent;
	public Field[][] opponentBoard;
	
	//Constructor for host of the game.
	public MultiplayerModel(int numInnerSquares, int innerSquareSize) {
		super(numInnerSquares, innerSquareSize, Mode.play);
		isServer = true;
		startServer();
	}
	
	//Constructor for client (not host).
	public MultiplayerModel(int numInnerSquares, int innerSquareSize, String address) {
		super(numInnerSquares, innerSquareSize, Mode.play);
		isServer = false;
		connectToServer(address);
	}
	
	public void start() {
		new Thread(new DisconnectedReader()).start();
		if (isServer) {
			//Wait for other player to join.
			try {
				fromOpponent.get(new ActualField("joined"));
				toOpponent.put("ok");
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
				toOpponent.put("joined");
				//Wait For response from server.
				Object[] response = fromOpponent.get(new FormalField(String.class));
				if (response[0].equals("full")) { //Server is full. Disconnect.
					System.out.println("Server full!");
					view.quitToMenu();
					return;
				}
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
		started = true;
		view.resetBoardPosition();
		view.repaint();
	}
	
	private void startServer() {
		repository = new SpaceRepository();
		repository.addGate("tcp://"+getIP()+":9001/?keep");
		toOpponent = new SequentialSpace();
		fromOpponent = new SequentialSpace();
		repository.add("hosttoclient", toOpponent);
		repository.add("clienttohost", fromOpponent);
	}
	
	private void connectToServer(String address) {
		try {
			toOpponent = new RemoteSpace("tcp://" + address + ":9001/clienttohost?keep");
			fromOpponent = new RemoteSpace("tcp://" + address + ":9001/hosttoclient?keep");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		close = true;
		try {
			fromOpponent.put("joined");
			fromOpponent.put("disconnected");
			fromOpponent.put(0, 0, 0);
			toOpponent.put("disconnected");
<<<<<<< HEAD
=======
			Thread.sleep(500);
>>>>>>> parent of 0139c6f (no message)
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
<<<<<<< HEAD
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
=======
>>>>>>> parent of 0139c6f (no message)
			repository.closeGates();
			repository.shutDown();
		}
	}
	
	public void setField(int x, int y, Field field) {
		super.setField(x, y, field);
		try {
			toOpponent.put(x, y, field.value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setField(int x, int y, int value) {
		super.setField(x, y, value);
		try {
			toOpponent.put(x, y, value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
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
					fromOpponent.get(new ActualField("joined"));
					toOpponent.put("full");
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
				view.quitToMenu();	
			}
		}
	}

	public void run() {
		start();
	}
	
	
}
