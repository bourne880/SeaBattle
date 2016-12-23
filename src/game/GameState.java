package game;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("serial")

// keeps information about course of the game
public class GameState implements Serializable {
	private HashMap<Integer, Integer> playerShipsSquares; // keys - squares (positions) of player ships, values - sizes of ships - 1
	private ArrayList<ArrayList<Integer>> playerShips; // index = size of ship - 1, element contains squares of a player ship
	private HashMap<Integer, Boolean> playerShipsHorizontal; // index = size of ship - 1, element contains horizontal value of a player ship
	private ArrayList<Integer> playerMoves; // contains moves of player from gameplay phase (selected squares)
	private HashMap<Integer, Integer> enemyShipsSquares; // keys - squares (positions) of enemy ships, values - sizes of ships - 1
	private ArrayList<ArrayList<Integer>> enemyShips; // index = size of ship - 1, element contains squares of an enemy ship
	private HashMap<Integer, Boolean> enemyShipsHorizontal; // index = size of ship - 1, element contains horizontal value of an enemy ship
	private ArrayList<Integer> enemyMoves; // contains moves of enemy from gameplay phase (selected squares)
	
	public GameState() {
		playerShipsSquares = new HashMap<>();
		playerShips = new ArrayList<>();
		playerShipsHorizontal = new HashMap<>();
		playerMoves = new ArrayList<>();
		enemyShipsSquares = new HashMap<>();
		enemyShips = new ArrayList<>();
		enemyShipsHorizontal = new HashMap<>();
		enemyMoves = new ArrayList<>();
	}

	public Set<Integer> getPlayerShipsSquares() {
		return playerShipsSquares.keySet();
	}

	public ArrayList<ArrayList<Integer>> getPlayerShips() {
		return playerShips;
	}	

	public ArrayList<Integer> getPlayerShip(int key) {
		return playerShips.get(playerShipsSquares.get(key));
	}

	public boolean getPlayerShipHorizontal(ArrayList<Integer> list) {
		return playerShipsHorizontal.get(list.size() - 1);
	}

	public ArrayList<Integer> getPlayerMoves() {
		return playerMoves;
	}
	
	public boolean playerShipsSquaresContains(int key) {
		return playerShipsSquares.containsKey(key);
	}

	public void setPlayerShipsSquares(ArrayList<Integer> list) {
		int value = list.size() - 1;
		
		for (int el : list)
			playerShipsSquares.put(el, value);
	}

	public void constructPlayerShips(int number) {
		for (int i = 0; i < number; i++)
			playerShips.add(new ArrayList<Integer>());
	}

	public void setPlayerShips(ArrayList<Integer> list, boolean horizontal) {
		playerShips.get(list.size() - 1).addAll(list);
		playerShipsHorizontal.put(list.size() - 1, horizontal);
	}

	public void setPlayerMoves(int field) {
		playerMoves.add(field);
	}

	public Set<Integer> getEnemyShipsSquares() {
		return enemyShipsSquares.keySet();
	}

	public ArrayList<ArrayList<Integer>> getEnemyShips() {
		return enemyShips;
	}	

	public ArrayList<Integer> getEnemyShip(int key) {
		return enemyShips.get(enemyShipsSquares.get(key));
	}

	public boolean getEnemyShipHorizontal(ArrayList<Integer> list) {
		return enemyShipsHorizontal.get(list.size() - 1);
	}

	public ArrayList<Integer> getEnemyMoves() {
		return enemyMoves;
	}

	public boolean enemyShipsSquaresContains(int key) {
		return enemyShipsSquares.containsKey(key);
	}

	public void setEnemyShipsSquares(ArrayList<Integer> list) {
		int value = list.size() - 1;
		
		for (int el : list)
			enemyShipsSquares.put(el, value);
	}

	public void constructEnemyShips(int number) {
		for (int i = 0; i < number; i++)
			enemyShips.add(new ArrayList<Integer>());
	}

	public void setEnemyShips(ArrayList<Integer> list, boolean horizontal) {
		enemyShips.get(list.size() - 1).addAll(list);
		enemyShipsHorizontal.put(list.size() - 1, horizontal);
	}

	public void setEnemyMoves(int field) {
		enemyMoves.add(field);
	}
	
	// for multi player game data transfer
	
	public Object getPlayerShipsSquaresMap() {
		return playerShipsSquares;
	}
	
	public Object getPlayerShipsHorizontal() {
		return playerShipsHorizontal;
	}
	
	public void setEnemyShipsSquares(HashMap<Integer, Integer> map)	{
		enemyShipsSquares.putAll(map);
	}
	
	public void setEnemyShips(ArrayList<ArrayList<Integer>> list) {
		enemyShips.addAll(list);
	}
	
	public void setEnemyShipsHorizontal(HashMap<Integer, Boolean> map) {
		enemyShipsHorizontal.putAll(map);
	}

}

	
	