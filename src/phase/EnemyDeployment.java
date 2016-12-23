package phase;

import game.Connection;
import game.GameState;
import gui.EnemyBoard;
import gui.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

// deployment of ships by enemy in multi player game
// connection dependent - if connection is closed, terminates
public class EnemyDeployment implements Runnable {
	private GUI gui;
	private EnemyBoard eBoard;
	private GameState state;
	private Connection connection;
	
	public EnemyDeployment(GUI aGUI, GameState aState, Connection aConnection) {
		gui = aGUI;
		eBoard = gui.getEBoard();
		state = aState;
		connection = aConnection;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		gui.message(2);
		
		eBoard.enableBoard(true); // enable enemy board
		
		
		LinkedList<Object> received = connection.getReceived();
		
		while (!Thread.currentThread().isInterrupted() && !connection.isClosed()) // reads data about deployment of enemy ships and updates state
			if (received.size() == 3) {
				state.setEnemyShipsSquares((HashMap<Integer, Integer>) received.remove());
				state.setEnemyShips((ArrayList<ArrayList<Integer>>) received.remove());
				state.setEnemyShipsHorizontal((HashMap<Integer, Boolean>) received.remove());
				break;
			}
		
		eBoard.enableBoard(false); // disable enemy board
	}
}
