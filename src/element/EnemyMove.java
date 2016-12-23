package element;

import game.Connection;
import game.GameState;
import gui.Board;
import gui.GUI;

import java.awt.Color;
import java.util.ArrayList;

// reads and updates move made by enemy in multi player game, then displays appropiate effects on board
// connection dependent - if connection is closed, terminates
public class EnemyMove implements Runnable {
	private GUI gui;
	private GameState state;
	private Board pBoard;
	private Connection connection;
	private int key;
	
	public EnemyMove(GUI aGUI, GameState aState, Connection aConnection) {
		gui = aGUI;
		pBoard = gui.getPBoard();
		state = aState;
		connection = aConnection;
	}
	
	public void run() {
		gui.message(5);
		pBoard.enableBoard(true);
		
		while (!Thread.currentThread().isInterrupted() && !connection.isClosed()) // reads move made by enemy
			if (connection.getReceived().size() > 0) {
				key = (int) connection.getReceived().remove();
				break;
			}
		
		state.setEnemyMoves(key); // update enemyMoves
		pBoard.selectSquare(key); // select square
		
		 // if enemy struck one of player's ships, display appropriate effects
		if (!Thread.currentThread().isInterrupted() && !connection.isClosed() && state.playerShipsSquaresContains(key)) {
			
			if (state.getEnemyMoves().containsAll(state.getPlayerShip(key))) { // sunk
				ArrayList<Integer> ship = state.getPlayerShip(key);
				pBoard.displayPositions(ship, state.getPlayerShipHorizontal(ship), Color.ORANGE);	
				gui.message(8);
				sleep();
				pBoard.displayShip(ship, state.getPlayerShipHorizontal(ship));	
			}
			else {  // hit
				pBoard.displayHit(key, true);		
				gui.message(7);
				sleep();
				pBoard.displayHit(key, false);
			}
		}
		else // miss
			gui.message(6);
		
		sleep();
		
		pBoard.enableBoard(false);
	}
	
	// sleeps thread for 1 sec
	private void sleep() {
		if (!connection.isClosed())
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				Thread.currentThread().interrupt();
				return;
			}	
	}
}
