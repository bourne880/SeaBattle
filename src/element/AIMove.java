package element;

import game.GameState;
import gui.Board;
import gui.GUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

// makes a move for computer player, then displays appropriate effects on board
public class AIMove implements Runnable {
	private GUI gui;
	private GameState state;
	private Board pBoard;
	private int key;
	
	public AIMove(GUI aGUI, GameState aState) {
		gui = aGUI;
		state = aState;
		pBoard = gui.getPBoard();
	}
	
	public void run() {
		gui.message(5);
		pBoard.enableBoard(true);
				
		sleep();
		
		ArrayList<Integer> huntList = huntList(); // list of hit positions of a not sunk ship
		
		if (huntList.isEmpty())
			draw();
		else if (huntList.size() == 1)
			drawOutOf4(huntList);
		else // hit positions are in line
			drawOutOf2(huntList);
		
		state.setEnemyMoves(key); // update enemyMoves
		pBoard.selectSquare(key); // select square
		
		if (state.playerShipsSquaresContains(key)) { // if computer struck one of player's ships, display appropriate effects		
			
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
	
	// based on enemyMoves returns hit positions of a not sunk ship or empty list if there aren't any
	private ArrayList<Integer> huntList() {
		ArrayList<Integer> huntList = new ArrayList<>();
		ArrayList<Integer> hits = new ArrayList<>(); // list of computer player hit moves
		
		// clear list from sunk ships and missed moves
		ArrayList<Integer> toRemove = new ArrayList<>();
		
		for (Integer key : state.getEnemyMoves()) {
			if (state.playerShipsSquaresContains(key)) { // test if hit move
				ArrayList<Integer> ship = state.getPlayerShip(key);
				
				if (state.getEnemyMoves().containsAll(ship) && !toRemove.containsAll(ship)) // test if ship was sunk
					toRemove.addAll(ship); // if was, add to toRemove
			}
			else // if missed move, remove
				toRemove.add(key);
		}
		
		hits.addAll(state.getEnemyMoves());
		hits.removeAll(toRemove);
		
		
		// look for line
		if (!hits.isEmpty()) {
			int key = hits.get(0);
			huntList.add(key); // add element
			
			// look for a vertical hit line
			int dkey = key;
			while ((dkey -= 10) >= 0 && hits.contains(dkey))
				huntList.add(dkey);
			
			int ukey = key;
			while ((ukey += 10) <= 99 && hits.contains(ukey))
				huntList.add(ukey);
			
			Collections.sort(huntList);
			
			// if found a vertical line, but no move is possible on the line
			if (huntList.size() > 1 && (huntList.get(0) <= 9 || state.getEnemyMoves().contains(huntList.get(0) - 10))
				&& (huntList.get(huntList.size() - 1) >= 90 || state.getEnemyMoves().contains(huntList.get(huntList.size() - 1) + 10))) {
				if (huntList.size() == hits.size()) { // if these are all hits
					huntList.clear();
					huntList.add(key);
					return huntList; // return huntList with the original key - computer will assume he hit not one but a few ships
				}
				else { // else will look for horizontal line
					huntList.clear();
					huntList.add(key);
				}
			}
			else if (huntList.size() > 1) // if found a line and moves are possible, return
				return huntList;
			
			
			// look for a horizontal line
			int[] digits = numberToDigits(key);
			
			int lkey = key;
			int lbound = Integer.parseInt(digits[0] + "" + 0);
			while ((lkey -= 1) >= lbound && hits.contains(lkey))
				huntList.add(lkey);
				
			int rkey = key;
			int rbound = Integer.parseInt(digits[0] + "" + 9);
			while ((rkey += 1) <= rbound && hits.contains(rkey))
				huntList.add(rkey);
			
			Collections.sort(huntList);
			
			// if found a horizontal line, but no move is possible on the line
			if (huntList.size() > 1 && (huntList.get(0) == lbound || state.getEnemyMoves().contains(huntList.get(0) - 1))
				&& (huntList.get(huntList.size() - 1) == rbound || state.getEnemyMoves().contains(huntList.get(huntList.size() - 1) + 1))) {
					huntList.clear();
					huntList.add(key);
					return huntList; // return huntList with the original key - computer will assume he hit not one but a few ships
			}
			else if (huntList.size() > 1) // if found a line and moves are possible, return
				return huntList;
		}
		
		return huntList; // return empty huntList or with one element if found
	}
	
	// draws random key that is not selected
	private void draw() {
		key = (int) (Math.random() * 100);
		
		while (!doubleTest(key))
			key = (int) (Math.random() * 100);
	}
	
	// draws out of 4 possible neighbours a key that is not selected
	private void drawOutOf4(ArrayList<Integer> huntList) {
		ArrayList<Integer> drawList = new ArrayList<>();
		
		int huntKey = huntList.get(0);
		int[] digits = numberToDigits(huntKey);
		
		if (digits[0] != 0)
			drawList.add(huntKey - 10);
		if (digits[0] != 9)
			drawList.add(huntKey + 10);
		if (digits[1] != 0)
			drawList.add(huntKey - 1);
		if (digits[1] != 9)
			drawList.add(huntKey + 1);
		
		int limit = drawList.size();
		int index = (int) (Math.random() * limit);
		key = drawList.get(index);
		
		while (!doubleTest(key)) {
			index = (int) (Math.random() * limit);
			key = drawList.get(index);
		}
	}
	
	// draws out of 2 possible ends of line a key that is not selected
	private void drawOutOf2(ArrayList<Integer> huntList) {
		ArrayList<Integer> drawList = new ArrayList<>();
		
		int fHuntKey = huntList.get(0);
		int lHuntKey = huntList.get(huntList.size() - 1);
		
		if (fHuntKey > lHuntKey) {
			fHuntKey = lHuntKey;
			lHuntKey = huntList.get(0);
		}
		
		int[] fHuntKeyDigits = numberToDigits(fHuntKey);
		int[] lHuntKeyDigits = numberToDigits(lHuntKey);
		
		if (lHuntKey - fHuntKey < 10) {
			
			if (fHuntKeyDigits[1] != 0)
				drawList.add(fHuntKey - 1);
			if (lHuntKeyDigits[1] != 9)
				drawList.add(lHuntKey + 1);
			
			if (drawList.size() == 1)
				key = drawList.get(0);
			else {
				int index = (int) (Math.random() * 2);
				key = drawList.get(index);
				
				while (!doubleTest(key)) {
					index = (int) (Math.random() * 2);
					key = drawList.get(index);
				}
			}
		}
		else {
			
			if (fHuntKeyDigits[0] != 0)
				drawList.add(fHuntKey - 10);
			if (lHuntKeyDigits[0] != 9)
				drawList.add(lHuntKey + 10);
			
			if (drawList.size() == 1)
				key = drawList.get(0);
			else {
				int index = (int) (Math.random() * 2);
				key = drawList.get(index);
				
				while (!doubleTest(key)) {
					index = (int) (Math.random() * 2);
					key = drawList.get(index);
				}
			}
		}
	}
	
	// returns true if key is not selected
	private boolean doubleTest(int key) {
		if (state.getEnemyMoves().contains(key))
			return false;
		return true;
	}
	
	// sleeps thread for 1 sec
	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
			Thread.currentThread().interrupt();
			return;
		}	
	}
	
	// given number of max 2 digits returns array of 2 digits
	private static int[] numberToDigits(int number) {
		String ij = String.valueOf(number);
		char[] digits = ij.toCharArray();
		int[] array = new int[2];
		
		if (number > 9) {
			array[0] = Character.getNumericValue(digits[0]);;
			array[1] = Character.getNumericValue(digits[1]);
		}
		else {
			array[0] = 0;
			array[1] = Character.getNumericValue(digits[0]);
		}

		return array;
	}
	
}
