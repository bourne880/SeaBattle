package run;

import game.GameState;
import gui.Board;
import gui.GUI;
import phase.LoadedGameplay;

import java.util.ArrayList;

// runs through phases that make up loaded game run
public class LoadedGame implements Runnable {
	private GUI gui;
	private GameState state;
	private int draw;
	private ThreadGroup loadGame;
	
	public LoadedGame(GUI aGUI, GameState aState, int aDraw, ThreadGroup parentTG) {
		gui = aGUI;
		state = aState;
		draw = aDraw;
		loadGame = new ThreadGroup(parentTG, "LoadedGame");
	}
	
	public void run() {		
		loadedState();

		Thread thread = new Thread(loadGame, new LoadedGameplay(gui, state, draw, loadGame));
		thread.start();
	}
	
	// brings back boards to state in the loaded game
	private void loadedState() {
		Board pBoard = gui.getPBoard();	
		for (ArrayList<Integer> list : state.getPlayerShips()) // display ships
			pBoard.displayShip(list, state.getPlayerShipHorizontal(list));
		for (int key : eHitList()) // display hits
			pBoard.displayHit(key, false);
		for (int key : state.getEnemyMoves()) // select squares
			pBoard.selectSquare(key);
		
		
		Board eBoard = gui.getEBoard();
		ArrayList<Integer> moves = new ArrayList<>(); // list of player moves
		moves.addAll(state.getPlayerMoves());
		
		for (int key : moves) // select squares
			eBoard.selectSquare(key);
		
		// display effects on board
		while (!moves.isEmpty()) {
			Integer key = moves.get(0);
			
			if (state.getEnemyShipsSquares().contains(key)) {
				ArrayList<Integer> ship = state.getEnemyShip(key);
				
				if (moves.containsAll(ship)) { // if ship was sunk
					eBoard.displayShip(ship, state.getEnemyShipHorizontal(ship));
					moves.removeAll(ship);
				}
				else { // if hit
					eBoard.displayHit(key, false);
					moves.remove(key);
				}
			}
			else // if miss, no effects
				moves.remove(key);
		}
	}
	
	// returns list with player hits
	private ArrayList<Integer> eHitList() {
		ArrayList<Integer> hitList = new ArrayList<>();
		ArrayList<Integer> moves = new ArrayList<>();
		moves.addAll(state.getEnemyMoves());
		
		while (!moves.isEmpty()) {
			Integer key = moves.get(0);
			
			if (state.playerShipsSquaresContains(key)) { // test if hit move
				ArrayList<Integer> ship = state.getPlayerShip(key);
				
				if (moves.containsAll(ship)) // test if ship was sunk
					moves.removeAll(ship); // if was, remove
				else { // if not, take all hit moves on this ship, add them on the list and remove from search list
					for (Integer el : moves)
						if (ship.contains(el))
							hitList.add(el);
					moves.removeAll(ship);
				}
			}
			else // if missed move, remove
				moves.remove(key);
		}
		
		return hitList;
	}

}
