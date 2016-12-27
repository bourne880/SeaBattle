package element;

import game.GameState;
import gui.EnemyBoard;
import gui.GUI;

import java.awt.Color;
import java.util.ArrayList;

// lets player to make a move, processes only a proper one, then displays effects on board
public class PlayerMove implements Runnable {
	private GUI gui;
	protected GameState state;
	private EnemyBoard eBoard;
	private int keyCTemp;
	private int key;
	
	public PlayerMove(GUI aGUI, GameState aState) {
		gui = aGUI;
		state = aState;
		eBoard = gui.getEBoard();		
		keyCTemp = eBoard.getKeyC();
	}
	
	public void run() {
		gui.message(4);
		eBoard.enableBoard(true);
		
		while (!Thread.interrupted() && optionalCondition()) {
			if (!keyCTempTest() && !doubleTest()) { // if player made a click move that is not a double
				key = eBoard.getKeyC();
				state.setPlayerMoves(key); // update playerMoves
				eBoard.selectSquare(key); // select square
				
				optionalAction();
					
				if (state.enemyShipsSquaresContains(key)) // if player struck one of enemy's ships, display appropriate effects
					if (state.getPlayerMoves().containsAll(state.getEnemyShip(key))) { // sunk
						ArrayList<Integer> ship = state.getEnemyShip(key);
						eBoard.displayPositions(ship, state.getEnemyShipHorizontal(ship), Color.ORANGE);
						gui.message(8);
						sleep();
						eBoard.displayShip(ship, state.getEnemyShipHorizontal(ship));
					}
					else {  // hit
						eBoard.displayHit(key, true);
						gui.message(7);
						sleep();				
						eBoard.displayHit(key, false);
					}
				else // miss
					gui.message(6);
					
				break;
			}
		
			try {
				Thread.sleep(1);
			} catch (Exception ex) { 
				Thread.currentThread().interrupt();
				return;
			}

		}

		sleep();
		
		eBoard.enableBoard(false);
		
		// to prevent square blocking if player clicked another square after making a move
		if (!state.getPlayerMoves().isEmpty())
			eBoard.setKeyC(state.getPlayerMoves().get(state.getPlayerMoves().size() - 1));
	}
	
	// returns true/false whether click position differs from recent (did player made a click move)
	private boolean keyCTempTest() {
		return eBoard.getKeyC() == keyCTemp;
	}
	
	// returns true if player repeated one of previous moves
	private boolean doubleTest() {
		if (state.getPlayerMoves().contains(eBoard.getKeyC()))
			return true;
		return false;
	}
	
	// sleeps thread for 1 sec
	protected void sleep() {
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
			Thread.currentThread().interrupt();
			return;
		}
	}
	
	// no optional condition
	protected boolean optionalCondition() {
		return true;
	}
	
	// no optional action
	protected void optionalAction() {
		
	}
	
}
